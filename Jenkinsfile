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
             steps {
                 script {
                              if (currentBuild?.getPreviousBuild()?.result == 'FAILURE') {
  if (currentBuild.resultIsBetterOrEqualTo(
  currentBuild.getPreviousBuild().result)) {
    echo 'build has been fixed'
  }
}
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
