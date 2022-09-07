@Library('java-maven') _

def gv

pipeline{
    agent any
    tools {
        jdk "JDK"
    }
    stages{
        stage("Sonarqube analysis"){
            steps{
                withSonarQubeEnv(credentialsId: 'mysorarqube', installationName: 'sample_java') {
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
         stage("Roll Back"){
              when{
                expression {
                    currentBuild.result = 'FAILURE'
                  //gv.rollback()
                    !("SUCCESS".equals(currentBuild.previousBuild.result))
                }
              }
             steps {
                 script {
                   gv = load "pipeline_config.groovy"
                    echo "building jar"
                    gv.buildJar()
             }
          }
        }
     stage ("post build action"){ 
       steps {
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
 }
 }
}
