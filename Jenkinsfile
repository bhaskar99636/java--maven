def gv

pipeline{
    agent any
    environment {
        registry = "webapp"
        registryCredential = 'ACR'
        dockerImage = ''
        registryUrl = 'defsloc.azurecr.io'
    }
    
    stages{
          stage('code quality check via sonarQube') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'dev' || 
                        env.BRANCH_NAME == 'QA' ) {
                        echo 'I execute on the DEV and QA branch'
                        gv = load "pipeline_config.groovy"
                        echo "sonarQube code quality check"
                        gv.qualityanalysis()
                    } else {
                        echo 'I execute elsewhere' 
                  }
                }
            }
        }
           stage('Generate Junit Test Report') {
            steps {
              script {
                  if (env.BRANCH_NAME == 'dev' || 
                      env.BRANCH_NAME == 'QA' ) {
                      gv = load "pipeline_config.groovy"
                      echo "Generated Test report..."
                      gv.testReport()
                } else {
                        echo 'I execute elsewhere' 
                  }
              }
            }
         }
        stage('generate HTML Report') {
          steps {
            script {
                publishHTML (target: [
          allowMissing: false,
          alwaysLinkToLastBuild: false,
          keepAll: true,
          reportDir: 'target',
          reportFiles: 'TEST-com.example.demo.DemoApplicationTests.xml',
          reportName: "RCov Report"
    ])

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
        stage('nexusArtifactsUploader') {
            steps {
                script { 
                    echo "uploading artifact to nexus"
                    gv.uploadArtifactToNexus()
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
      stage('Building Docker image') {
         steps{
           script {
             withCredentials([usernamePassword(credentialsId: 'ACR', passwordVariable: 'PASS', usernameVariable: 'USER')])
             sh "echo $PASS | docker login -u $USER --password-stdin"
             dockerImage = docker.build registry + ":$BUILD_NUMBER"
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
 }
