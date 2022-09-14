def gv

pipeline{
    agent any
    
    stages{
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
       stage("Roll Back") {
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
