@Library('java-maven') _

def gv

pipeline{
    agent any
    tools {
        jdk "JDK"
    }
    stages{
        stage('SCM Checkout') {
            steps {
                git branch: '',
                credentialsId: '',
                url: 'https://github.com/bhaskar99636/java--maven.git'
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
         stage("releaseVersion"){
             steps {
                 script {
                   echo "git tag"
                   gv.tag version()
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
