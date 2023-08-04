#!/usr/bin/env groovy

/*

1. Global scope
Globally(Jenkins) scoped shared library, with name as defined in Jenkins GUI:
Manage Jenkins->System Configuration->Configure System->Global Pipelines

@Library('JenkinsSharedLib')_

If we don't have a variablr definition or import,
directly after libraby definition then we have 
to put the underscore at the end.(Groovy syntax)

2. Override global version
We can also override the default globally available 
shared library version per Jenkinsfile by specifying 
tag, branch name or commit hash

@Library('global-jenkins-shared-library@main')

*/


// define shared librabry for project only. For this we DO NOT need a Global Shared Library defined in the Jenkins GUI:
// Manage Jenkins->System Configuration->Configure System->Global Pipelines, the whole config is done here.
library identifier: 'JenkinsSharedLib@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'git@github.com:miltozz/jenkins-shared-library.git',
         credentialsId: 'my_github_id'
        ]
)

//define external script handler var
def gv

/* note on ternary operator

GROOVY elvis / JAVA ternary
def varName = System.getenv("BUILD_NUMBER") ? : "0"
 means if varName is null then varName = 0
*/

//SPECIFY IMAGE NAME as global var
def dockerRepoImageName = 'miltosdev/my-private-repo:jda-v1.0.0'
def nexusRepoImageName = '111.11.111.111:8083/nx-java-demo-app:1.5.0-nx'
def jenkinsIp = '13.36.39.83/32'


pipeline{
    agent any

    tools{
        maven 'maven386'
    }

    //image name as pipeline environment variable
    environment{
        IMAGE_NAME = 'miltosdev/my-private-repo:jda-v1.0.0'
    }

    stages{
        stage("init") {
            steps {
                echo "stage: init"
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage("build jar") {
            steps {
                echo "stage: build jar"
                script {
                    echo "sb build jar"
                    //buildJar()
                    buildJarClean()
                }
            }
        }

        stage("Build Push docker image") {
            steps {
                echo "stage: build and push docker image"
                script {
                    echo "Debug:stage:script: build and push docker image"
                    // // JSL
                    // // Example 1:
                    // // Build, login, push, all in one block, no params provided.
                    // buildImageSimple() 

                    // // JSL
                    // // Example 2:
                    // // Build, login, push, all in one block, with param image name.
                    // buildImageParams("$dockerRepoImageName")

                    // // JSL
                    // Example 3: 
                    // Extract logic and Groovy classes
                    buildImage "$dockerRepoImageName"
                    dockerLogin()
                    dockerPush "$dockerRepoImageName"
                } 
            }
        }

        /* Stage ("provision server with tf")

        env 
        1. provide env vars for AWS auth using Jenkins AWS credentials,
        2. so that Terraform can connect to AWS
        3. using TF_VAR_ to change default depl_env_prefix value to 'test'
        4. using TF_VAR_ to provide Jenkins IP to Terraform, for SG SSH                    
        
        script
        1. dir to navigate to terraform folder
        2. assign env var EC2_PUBLIC_IP with Terraform output, to get provisioned EC2 instance public IP. trim spaces.
        3. terraform also installs docker and compose with user-data
        */
        stage ("provision server with tf") {
            environment{
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                TF_VAR_depl_env_prefix = 'test'
                //TF_VAR_jenkins_ip = '11.111.111.202/32'
                TF_VAR_jenkins_ip = "$jenkinsIp"
            }
            steps{
                script{
                    dir('terraform') {
                        sh "terraform init"
                        sh "terraform apply --auto-approve"
                        sh "terraform state list"
                        // sh "terraform plan"
                        EC2_PUBLIC_IP = sh(
                            script: "terraform output instance-myapp-server-public-IP",
                            returnStdout: true
                        ).trim()
                         //sh "terraform destroy --auto-approve"
                        // sh "terraform state list"

                    }
                }
            }
        }

        /* Stage('deploy') 

        1. DOCKER_CREDS env var takes docker creds object from Jenkins creds
        2. creds object provides DOCKER_CREDS_USR and DOCKER_CREDS_PSW vars by default
        3. sleep for 90 seconds to allow EC2 instance to initiate fully
        4. pass ${IMAGE_NAME} ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW} arguments to server-cmds.sh
        5. use sshagent jenkins plugin to execute ssh commands to the provisioned EC2 instance
        6. using ssh-key that was previously created on AWS region, and then created on Jenkins creds
        7. scp server-cmds.sh and docker-compose.yaml to the EC2 instance
        8. ssh to EC2 instance and execute server-cmds.sh
        9. server-cmds.sh logins to docker and runs docker-compose

        */
        stage('deploy') {
            environment{
                    DOCKER_CREDS = credentials('dockerhub-private-repo')
                }
            steps {
                script {
                    sleep(time: 90, unit: "SECONDS")
                    echo 'deploy on ec2 remote target...'

                    def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME} ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"
                    def ec2Instance = "ec2-user@${EC2_PUBLIC_IP}"

                    //ssh onto the proviioned ec2 host from Jenkins, copy files and run the shellCmd
                    sshagent(['paris-test-key']) {
                        sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user"
                        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
                        //sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} pwd"
                   }
                }
            }
        }


        // stage("build and push Nexus image"){           
        //     steps{
        //         echo "stage : build and push nexus image"
        //         script{
        //             echo "sb build and push nexus image"
        //             //gv.buildApp()
        //             //buildImage 'miltosdev/my-private-repo:jda-3.0'                   
        //             //gv.buildImageForNexus "$nexusRepoImageName"               
        //         }
        //     }
        // }

    }
}
