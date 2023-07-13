def gv

pipeline{
    agent any
    tools{
        maven 'maven386'
    }


    stages{
        stage ("init"){
            steps{
                script{
                    echo "Init...."
                    gv = load "script.groovy"
                }
            }
        }
        stage ("test"){
            when {
                expression{
                    env.GIT_BRANCH == 'origin/jenkins-buildJar-pushDocker'
                }
            }            
            steps{
                echo "testing..."
                echo "GIT_BRANCH: ${env.GIT_BRANCH}"
                echo "BUILD_NUMBER: ${env.BUILD_NUMBER}"
                echo env.BUILD_NUMBER   
                script{
                    sh 'echo $BUILD_NUMBER'
                    echo BUILD_NUMBER
                    echo "BUILD_NUMBER is $BUILD_NUMBER"
                }             
            }
        }        
        stage("build jar"){           
            steps{
                script{
                    echo "building jar..."
                    gv.buildJar()
                }
                
            }
        }
        stage("build and push docker image"){            
            steps{
                script{
                    echo "building and pushing docker image..."
                    gv.dockerImageBP()
                }
                
            }
        }        
        stage("deploy"){
            steps{
                echo 'deploying..'
            }
        }
    }
}
