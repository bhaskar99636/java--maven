def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def qualityanalysis() {
    echo "sonarQube code quality check"
    withSonarQubeEnv(credentialsId: 'mysorarqube', installationName: 'sample_java') {
    sh 'mvn sonar:sonar' 
    sh 'mvn verify sonar:sonar'
                  }
         }

def testReport(){
    echo 'Generated Test report...'
    sh 'mvn site'
    sh 'mvn surefire-report:report'
}


def rollback() {
    echo "roll back to previous version"
    if (currentBuild?.getPreviousBuild()?.result == 'FAILURE') {
    if (currentBuild.resultIsBetterOrEqualTo(
    currentBuild.getPreviousBuild().result)) {
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
