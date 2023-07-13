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
        stage ("user-input"){
            // !!! input PAUSES build execution for user input
            input{
                message "Select some input parameter"
                ok "Submit"
                //same as global params but block scoped
                parameters{
                    choice(name:'SOME_INPUT_PARAM_1', choices:['ip1A', 'ip1B', 'ip1C'], description:'Please select input param1.')
                    choice(name:'SOME_INPUT_PARAM_2', choices:['ip2A', 'ip2B', 'ip2C'], description:'Please select input param2.')
                }
            }
            steps{
                script{
                    //Assign input to a globally accessible variable. Inside script block, Different, inline, syntax
                    env.INPUT_VAR = input message:"Select input for INPUT_VAR", ok:"Submit", parameters:[choice(name:'INPUT_VAR', choices:['iv1', 'iv2', 'iv3'], description:'Please select input var.')]
                    
                    echo "Selected input parameter 1: ${SOME_INPUT_PARAM_1}"
                    echo "Selected input parameter 2: ${SOME_INPUT_PARAM_2}"
                    echo "Selected input var: ${INPUT_VAR}"
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
                    //BRANCH_NAME only works in MULTIBRANCH pipelines
                    //env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'jenkinsfile-tutorial' && MYBOOL == true
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
