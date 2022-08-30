pipeline{
    agent any
    stages{
        stage ('cleanWs & checkout scm') {
          steps {
            script {
               deleteDir()
               cleanWs()
               checkout scm
          }
        }
      }
        stage("Sonarqube analysis"){
            steps{
                script{
                withSonarQubeEnv(credentialsId: 'new_sonar') {
                     sh 'mvn sonar:sonar' 
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
