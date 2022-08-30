pipeline{
    agent any
    stages{
        stage("init") {
            steps {
                script {
                    gv = load "pipeline_config.groovy"
                }
            }
        }
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
