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

def rollback() {
    echo "roll back to previous version"
    if (currentBuild?.getPreviousBuild()?.result == 'FAILURE') {
    if (currentBuild.resultIsBetterOrEqualTo(
    currentBuild.getPreviousBuild().result)) {
    echo 'build has been fixed'
   }
  }
}
def tag version() {
    void gitTag(Version releaseVersion) {
      sshagent(['devops_deploy_DEV']) {
        shell 'git tag -d \$(git tag)'
        shell 'git fetch --tags'
        echo "New release version ${releaseVersion.normalVersion}"
        shell "git tag -fa ${releaseVersion.normalVersion} -m 'Release version ${releaseVersion.normalVersion}'"
      }
    }
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
