def gv

pipeline{
    agent any
    
    stages{
        stage('SCM Checkout') {
            steps {
                git branch: '${params.BRANCH_NAME}',
                credentialsId: '${params.GIT_CREDS}',
                    url: '${params.REPO_URL}'
            }
        }
        stage("Sonarqube analysis"){
            steps{
                script {
                    gv = load "pipeline_config.groovy"
                    echo "sonarQube code quality check"
                    gv.qualityanalysis() 
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
       stage("Roll Back"){
             steps {
                 script {
                   echo "roll back to previous version"
                   gv.rollback()
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
