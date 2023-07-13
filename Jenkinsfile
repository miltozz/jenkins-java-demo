def gv

MY_BOOL = true
SOME_STRING = 'somestring'

pipeline{
    agent any

    //Supported: gradle, maven and jdk 
    tools{ 
        maven 'maven386'
    }

    environment{
        SOME_VAR = 'somevalue'
    }

    parameters{
        string(name:'VERSION', defaultValue:'1.0.0', description:'Provide version info. (Default: 1.0.0)')
        choice(name:'DEPLOY_ENV', choices:['test', 'dev', 'prod'], description:'Please select deployment environment.')
        booleanParam(name:'doExecutePre', defaultValue:true, description:'Execute pre stage?')
    }

    stages{
        stage ("init"){
            steps{
                script{
                    gv = load "script.groovy"
                    echo 'Init...'
                    sh 'mvn --version'
                }             
            }
        }        
        stage ("pre"){
            when{
                expression{
                    //params.doExecutePre
                    gv.checkPreExecution()
                }
            }
            steps{
                script{
                    echo 'Pre...'
                    echo "Deployment env selected: ${params.DEPLOY_ENV}"
                    gv.externalPre()
                }             
            }
        }        
        stage ("test"){
            steps{
                script{
                    gv.testStage()
                }
            }
        }

        stage("build"){         
            steps{
                script{
                    echo 'building...'
                    withCredentials([usernamePassword(credentialsId: 'dummy-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        // available as an env variable, but will be masked if you try to print it out any which way
                        // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
                        sh 'echo $PASSWORD'
                        // also available as a Groovy variable
                        echo USERNAME
                        // or inside double quotes for string interpolation
                        echo "username is $USERNAME"
                    }
                }             
            }
        }
        stage("deploy"){
            when {
                expression{
                    //BRANCH_NAME only works in multibranch pipelines
                    //env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'jenkins-jobs-simple' && MYBOOL == true
                    MY_BOOL == true
                }
            }            
            steps{
                echo 'deploying...'
            }
        }
    }

    post{
        always{
            echo 'Always print this!'
        }
        failure{
            echo 'Build failed'
        }
        success{
            echo 'Build success'
        }
    }
}
