def gv

pipeline{
    agent any
    
    stages{
        stage('SCM Checkout') {
            steps {
                git branch: 'master',
                credentialsId: 'b48e0e8b-8db0-4f49-911d-d3e5778511b3',
                url: 'https://github.com/venkatd901/java--maven.git'
            }
        }
        // stage("Sonarqube analysis"){
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
                    gv = load "pipeline_config.groovy"
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
