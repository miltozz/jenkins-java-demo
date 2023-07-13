#!/usr/bin/env groovy

/*
1. Global scope
Globally(Jenkins) scoped shared library, with name as defined in Jenkins GUI:
Manage Jenkins->System Configuration->Configure System->Global Pipelines

@Library('JenkinsSharedLib')_

If we don't have a variable definition or import,
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

/* 
GROOVY elvis / JAVA ternary operator
def varName = System.getenv("BUILD_NUMBER") ? : "0"
 means if varName is null then varName = 0
 */

//SPECIFY IMAGE NAME
def dockerRepoImageName = 'miltosdev/my-private-repo:jda-class-1.0.0'
def nexusRepoImageName = '111.11.111.111:8083/nx-java-demo-app:1.5.0-nx'


pipeline{
    agent any

    tools{
        maven 'maven386'
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

        stage("BP docker image") {
            steps {
                echo "stage: build and push docker image"
                script {
                    echo "stage:script: build and push docker image"

                    // // Example 1:
                    // // Build, login, push, all in one block, no params provided.
                    // buildImageSimple() 

                    // // Example 2:
                    // // Build, login, push, all in one block, with param image name.
                    // buildImageParams("$dockerRepoImageName")

                    // Example 3: 
                    // Extract logic and Groovy classes
                    buildImage "$dockerRepoImageName"
                    dockerLogin()
                    dockerPush "$dockerRepoImageName"
                } 
            }
        }

        stage("deploy"){           
            steps{
                script{
                    echo "deploying..."
                    gv.deployApp()
                }

            }
        }
    }
}
