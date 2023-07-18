//in Dockerfile jar matching with wild card *
//another way is to pass the version number as a parameter and add it to jar name


def gv
def EC2_PUBLIC_IP = '11.111.111.245'
def EC2_USER = 'ec2-user'
def DOCKER_REPO = 'miltosdev/my-private-repo'


pipeline{
    agent any
    tools{
        maven 'maven386'
    }

    environment{
        IMAGE_NAME = "5.0.11-build-19"
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
            steps{
                script{
                    echo "testing...."
                    echo "Testing branch $BRANCH_NAME"
                }
            }
        }

        stage('incr. version') {
            steps {
                script {
                    echo 'incrementing app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def versionMatcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = versionMatcher[0][1]
                    env.IMAGE_NAME = "$version-build-$BUILD_NUMBER"
                }
            }
        }

        stage("buildApp"){           
            steps{
                script{
                    echo "building..."
                    gv.buildApp()
                }
                
            }
        }        

        stage ('buildImage'){
            steps{
                script{
                    echo "building the docker image..."
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
                    }
                }
            }
        }

        stage('jenkins commit pom modification'){
            steps{
                script{
                    sh 'git status'
                    sh 'git branch'
                    sh 'git config --list'
                    sh 'git remote set-url origin git@github.com:miltozz/jenkins-java-demo.git'
                    sh 'git add .'
                    sh 'git commit -m "ci jenkins:version bump, pom.xml"'
                    sh 'git push origin HEAD:av-dock-compose'
                }
            }
        }


        // stage ('deploy on ec2'){
        //     environment{
        //         DOCKER_CREDS = credentials('dockerhub-private-repo')
        //     }
        //     steps{
        //         script{
        //             def dockerLogin = "bash ./docker-creds.sh ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"
        //             def dockerCmd = "docker run -dp 3000:8080 ${DOCKER_REPO}:${IMAGE_NAME}"
        //             sshagent(['paris-test-key']) {
        //                 sh "scp -o StrictHostKeyChecking=no docker-creds.sh ${EC2_USER}@${EC2_PUBLIC_IP}:/home/${EC2_USER}"
        //                 sh "ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_PUBLIC_IP} ${dockerLogin}"
        //                 sh "ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_PUBLIC_IP} ${dockerCmd}"
        //             }
        //         }

        //     }
        // } 

        // stage ('deploy ec2 compose'){
        //     environment{
        //         DOCKER_CREDS = credentials('dockerhub-private-repo')
        //     }
        //     steps{
        //         script{
        //             echo "deploy on ec2 remote server using docker compose..."
        //             def dockerLogin = "bash ./docker-creds.sh ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"
        //             def dockerCompCmd = "docker-compose -f docker-compose-explicit.yml up --detach"

        //             sshagent(['paris-test-key']) {
        //                 sh "scp -o StrictHostKeyChecking=no docker-creds.sh ${EC2_USER}@${EC2_PUBLIC_IP}:/home/${EC2_USER}"
        //                 sh "scp docker-compose-explicit.yml ${EC2_USER}@${EC2_PUBLIC_IP}:/home/${EC2_USER}"
        //                 sh "ssh ${EC2_USER}@${EC2_PUBLIC_IP} ${dockerLogin}"
        //                 sh "ssh ${EC2_USER}@${EC2_PUBLIC_IP} ${dockerCompCmd}"
        //             }
        //         }

        //     }
        // } 

        stage ('deploy compose ext.script'){
            environment{
                DOCKER_CREDS = credentials('dockerhub-private-repo')
            }
            steps{
                script{
                    echo "deploy on ec2 remote server using docker compose, by running an external shell script..."
                    def shellCmds = "bash ./server-commands.sh ${DOCKER_REPO}:${IMAGE_NAME}"
                    def dockerLogin = "bash ./docker-creds.sh ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"

                    sshagent(['paris-test-key']) {
                        sh "scp -o StrictHostKeyChecking=no docker-creds.sh server-commands.sh docker-compose.yml ${EC2_USER}@${EC2_PUBLIC_IP}:/home/${EC2_USER}"
                        sh "ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_PUBLIC_IP} ${dockerLogin}"
                        //sh "chmod 744 server-commands.sh"
                        sh "ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_PUBLIC_IP} ${shellCmds}"
                    }
                }
            }
        }

/*         stage('commit with username and pass') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        // git config here for the first time run
                        sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'
                        //or SSH into the jenkins server and set the git configuration


                        sh "git remote set-url origin https://${USER}:${PASS}@github.com/miltozz/jenkins-java-demo.git"
                        sh 'git add .'
                        sh 'git commit -m "named ci: version bump"'
                        sh 'git push origin HEAD:av-dock-compose'
                    }
                }
            }
        } */

    }
}
