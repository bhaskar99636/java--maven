def gv

pipeline{
    agent any
    
    parameters {
        string(defaultValue: "0.0-SNAPSHOT", description: 'Enter your Version number ', name: 'VERSION')
        string(defaultValue: "${BUILD_NUMBER}", description: 'Enter your Version number ', name: 'version')
    }
    environment {
        registry = "webapp"
        registryCredential = ''
        dockerImage = ''
        registryUrl = ''
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
           stage ('Build and Package') {
                steps {
                 script {
                     if (fileExists()) {
                     def readcounter =    readFile(file: 'version.txt')
                     readcounter = readcounter.toInteger() +1
                     def version= "Version" + readcounter
                     println(version)
                     sh 'mvn package -Dartifactversion=' + "${version}"
                       }
                     } 
                     echo "Build and Package Completed" 
          } 
      } 
        stage('Upload Artifact to Nexus') {
            steps {
                script { 
                    echo "uploading artifact to nexus"
                    gv.uploadArtifactToNexus()
                }
            }
        }
        stage('deploy to tomcat') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sshagent(['deploy']) {
                    echo "taking a backup of existing running version and deploying new version"
                    sh "cp target/demo-2.0-SNAPSHOT.jar /var/lib/jenkins/workspace/javatest/target/demo-2.0-SNAPSHOT.jar_backup"
                    sh "scp -o StrictHostKeyChecking=no target/demo-${params.VERSION}.jar azureuser@20.219.92.67:/opt/tomcat/apache-tomcat-10.0.26/webapps"
                 }
             }
            }
          post{
                    always{
                        echo "always"
                        }
                    failure {
                        echo "failure"
                        }
                    }
            }
       stage("Roll Back") {
             steps {
                script{ 
                      echo "roll back to previous version"
                      gv.rollback()
                }
            }
             }
         stage('Build Docker image') {
         steps{
           script {
             echo "building the docker image"
             gv.buildImage()
     }
  }
}       
  }
 }
