def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def counter = 1
def data = "Version" + counter
writeFile(file: 'version.txt', text: counter.toString())

def qualityanalysis() {
    echo "sonarQube code quality check"
    withSonarQubeEnv(credentialsId: 'maven_java', installationName: 'maven_java'){
    sh 'mvn clean package sonar:sonar' 
    sh 'mvn verify sonar:sonar -Dsonar.host.url=http://20.193.128.173:9000 -Dsonar.login=e9d388747edb4fb1218b048bbe671cd772aa7492 -Dsonar.login=admin -Dsonar.password=admin'
                  }
         }

def testReport(){
    echo 'Generated Test report...'
    sh 'mvn site'
}


def rollback() {
    echo "roll back to previous version"
    if (currentBuild?.getPreviousBuild()?.result == 'FAILURE') {
    if (currentBuild.resultIsBetterOrEqualTo(
    currentBuild.getPreviousBuild().result)) {
        sh "scp -o StrictHostKeyChecking=no target/demo-2.0-SNAPSHOT.jar_backup azureuser@20.219.92.67:/opt/tomcat/apache-tomcat-10.0.26/webapps"
    echo 'build has been fixed'
   }
  }
}

def uploadArtifactToNexus() {
    echo "uploading artifact to nexus"
    nexusArtifactUploader artifacts: [
             [
                 artifactId: 'demo',
                 classifier: '',
                 file: '/var/lib/jenkins/workspace/java-maven/target/demo-2.0-SNAPSHOT.jar',
                 type: 'jar'
                 ]
            ],
            credentialsId: 'Nexus',
            groupId: 'com.example',
            nexusUrl: '20.219.92.67:8081',
            nexusVersion: 'nexus3',
            protocol: 'http',
            allowInsecureProtocol: 'true',
            repository: 'maven-snapshots',
            version: '2.0-SNAPSHOT'
}

def buildImage() {
    echo "building the docker image"
    docker.build("defsloc.azurecr.io/" + "demojava.${env.BUILD_NUMBER}")
    dockerImage = docker.build registry + ":$BUILD_NUMBER"
    }

def pushImage() {
    withCredentials([usernameColonPassword(credentialsId: 'ACR', variable: 'ACR')]) {
    sh "docker login -u defsloc -p ${ACR}"
    docker.withRegistry('', registryCredential) {
    dockerImage.push()
    }
    }
}

def deployApp() {
    echo 'deploying the application...'
} 

return this
