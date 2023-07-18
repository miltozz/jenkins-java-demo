//in Dockerfile jar matching with wild card *
//another way is to pass the version number as a parameter and add it to jar name

//we should also build the jar with mvn clean package, so the older jars are deleted.
//Dockerfile needs this because it builds the image with a * wildcard in the jar name. 

def gv
def EC2_PUBLIC_IP = '11.11.11.131'
def EC2_USER = 'ec2-user'
def DOCKER_REPO = 'miltosdev/my-private-repo'

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
                    //increments app version on the pom.xml file
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def versionMatcher = readFile('pom.xml') =~ '<version>(.+)</version>' //returns array

                    // get first array element e.g. <version>2.0.3</version>
                    // then get second child from child array e.g. 2.0.3
                    def version = versionMatcher[0][1]
                    env.IMAGE_NAME = "$version-build-$BUILD_NUMBER" //2.0.3-4, version-pipeline build number
                }
            }
        }
        stage("buildApp"){           
            steps{
                script{
                    echo "building..."
                    //pom.xml is updated so we build the jar with updated version
                    gv.buildApp()
                }         
            }
        }        

        stage ('buildImage push to dockerhub'){
            steps{
                script{
                    echo "building the docker image..."
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t miltosdev/my-private-repo:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push miltosdev/my-private-repo:${IMAGE_NAME}"
                    }
                }
            }
        }
        // we commit the updated pom.xml file to the remote repo
        // github has jenkins public key
        stage('commit pom modification'){
            steps{
                script{
                    sh 'git status'
                    sh 'git branch'
                    sh 'git config --list'
                    sh 'git remote set-url origin git@github.com:miltozz/jenkins-java-demo.git'
                    sh 'git add .'
                    sh 'git commit -m "Jenkins ci : version bump on pom.xml"'
                    sh 'git push origin HEAD:auto-versioning'
                }
            }
        }

        stage ('deploy on ec2'){
            environment{
                DOCKER_CREDS = credentials('dockerhub-private-repo')
            }
            steps{
                script{
                    def ec2Instance = "${EC2_USER}@${EC2_PUBLIC_IP}"
                    def dockerLogin = "bash ./docker_creds.sh ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"
                    def dockerCmd = "docker run -dp 3000:8080 miltosdev/my-private-repo:${IMAGE_NAME}"
                                  
                    sshagent(['paris-test-key']) {
                        //sh "ssh -o StrictHostKeyChecking=no ec2-user@11.111.11.111 ${dockerCmd}"
                        sh "scp -o StrictHostKeyChecking=no docker_creds.sh ${ec2Instance}:/home/${EC2_USER}"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${dockerLogin}"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} docker ps"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${dockerCmd}"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} docker ps"                       
                    }
                }
            }
        }
    }
}





        
    //   stage('commit with username and pass') {
    //         steps {
    //             script {
    //                 withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    //                     // git config here for the first time run
    //                     sh 'git config --global user.email "jenkins@example.com"'
    //                     sh 'git config --global user.name "jenkins"'
    
    //                     //or SSH into the jenkins server and set the git configuration
    //                     sh "git remote set-url origin https://${USER}:${PASS}@gitlab.com/miltozz/jenkins-java-demo.git"
    //                     sh 'git add .'
    //                     sh 'git commit -m "ci: version bump"'
    //                     sh 'git push origin HEAD:auto-versioning'
    //                 }
    //             }
    //         }
    //     }