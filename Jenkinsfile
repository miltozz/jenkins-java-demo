#!/usr/bin/env groovy

//define external script groovy handler var
def gv

// repo and image names
def dockerRepoImageName = 'miltosdev/my-private-repo:jma-1.0.0'
def nexusRepoImageName = '111.11.111.111:8083/nx-java-mv-app:1.0.0-nx'

pipeline{
    agent any

    tools{
        maven 'maven386'
    }

    environment{
        ENV_VAR = 'env-value'
    }

    stages{
        stage("Init") {
            steps {
                echo "Stage: Init"
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage("Build jar") {
            steps {
                echo "Stage: Build jar"

                //multibranch pipeline only
                echo "Building on branch $BRANCH_NAME"

                script {
                    echo "Debug: Script block echo build jar"
                    gv.buildJar()
                }
            }
        }
        
        // stage("Build and push to Nexus"){           
        //     steps{
        //         echo "Stage: Build and push to Nexus "
        //         script{
        //             echo "Debug: Script block build and push to Nexus"
        //             gv.buildImageNexus("$nexusRepoImageName")
        //         }
        //     }
        // }

        stage("Build and push to DockerHub") {
            steps {
                echo "Stage : Build and push to docker"
                script {
                    echo "Debug: Script Block build and push to docker"
                    gv.buildImageDocker("$dockerRepoImageName")
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
