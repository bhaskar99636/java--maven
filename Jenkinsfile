@Library('java-maven') _
def gv

pipeline{
    agent any
    stages{
        stage("Sonarqube analysis"){
            steps{
                script{
                withSonarQubeEnv(credentialsId: 'mysorarqube') {
                     sh 'mvn sonar:sonar' 
                  }
                }
            }
        stage("Build jar") {
            steps {
                script {
                    gv = load "pipeline_config.groovy"
                    echo "building jar"
                    gv.buildJar()
                }
            }
         }
        }
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}
