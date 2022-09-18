def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def qualityanalysis() {
    echo "sonarQube code quality check"
    withSonarQubeEnv(credentialsId: 'mysorarqube', installationName: 'sample_java') {
    sh 'mvn sonar:sonar' 
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
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'ACR', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t maven--java/demo-app:jma-2.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push maven--java/demo-app:jma-2.0'
    }
} 

def deployApp() {
    echo 'deploying the application...'
} 

return this
