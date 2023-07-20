#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        maven 'maven386'
    }
    environment {
        AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
    }
    stages {
        stage('tform provision cluster') {
            environment {
                TF_VAR_depl_env_prefix = "test"
                TF_VAR_k8s_version = "1.26.6"
            }
            steps {
                script {
                    dir('terraform') {
                        echo "terraform - creating ECR repo..."
                        echo "terraform - creating EKS cluster..."
                        sh "terraform init"
                        sh "terraform apply --auto-approve"
                        env.DOCKER_REPO_URL = sh(
                            script: "terraform output repo_url",
                            returnStdout: true
                        ).trim()
                        env.K8S_CLUSTER_URL = sh(
                            script: "terraform output cluster_url",
                            returnStdout: true
                        ).trim()
                        env.REPO_USER = sh(
                            script: "terraform output ecr_user_name",
                            returnStdout: true
                        ).trim()
                        env.REPO_PWD = sh(
                            script: "terraform output ecr_user_password",
                            returnStdout: true
                        ).trim()
                    }
                    env.KUBECONFIG="terraform/kubeconfig.yaml"
                    sh "kubectl get node"
                }
            }
        }
        stage('increment version') {
            steps {
                script {
                    echo 'incrementing app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
                }
            }
        }
        stage('build app') {
            steps {
               script {
                   echo "building the application..."
                   sh 'mvn clean package'
               }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo "building the docker image..."
                    sh "docker build -t ${DOCKER_REPO_URL}:${IMAGE_NAME} ."
                    sh "echo ${REPO_PWD} | docker login -u ${REPO_USER} --password-stdin ${DOCKER_REPO_URL}"
                    sh "docker push ${DOCKER_REPO_URL}:${IMAGE_NAME}"
                }
            }
        }
        stage('deploy') {
            environment {
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    echo 'deploying docker image...'
                    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
                    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
                }
            }
        }
        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh 'git config user.email "jenkins@example.com"'
                        sh 'git config user.name "Jenkins"'
                        sh "git remote set-url origin https://${USER}:${PASS}@gitlab.com/miltozz/java-maven-app.git"
                        sh 'git add .'
                        sh 'git commit -m "ci: version bump"'
                        //${BRANCH_NAME} works in multibranch pipelines
                        sh "git push origin HEAD:${BRANCH_NAME}"
                    }
                }
            }
        }
    }
}
