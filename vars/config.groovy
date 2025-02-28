def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def rollback() {
    echo "roll back to previous version"
    hudson.model.Result.SUCCESS.equals(currentBuild.rawBuild.getPreviousBuild()?.getResult()) == true
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
