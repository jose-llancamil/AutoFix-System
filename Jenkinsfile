pipeline{
    agent any
    tools{
        maven "maven"

    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/jose-llancamil/AutoFix-System.git']])
                dir("repair-management-system"){
                    bat "mvn clean install"
                }
            }
        }
        stage("Test"){
            steps{
                dir("repair-management-system"){
                    bat "mvn test"
                }
            }
        }        
        stage("Build and Push Docker Image"){
            steps{
                dir("repair-management-system"){
                    script{
                         withDockerRegistry(credentialsId: 'docker-credentials'){
                            bat "docker build -t josellancamil/backend-image ."
                            bat "docker push josellancamil/backend-image"
                        }
                    }                    
                }
            }
        }
    }
}
