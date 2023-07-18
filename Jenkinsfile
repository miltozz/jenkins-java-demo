//in Dockerfile jar matching with wild card *
//another way is to pass the version number as a parameter and add it to jar name

//we should build the jar with mvn clean package, so the older jars are deleted.
//Dockerfile needs this because it builds the image with a * wildcard in the jar name. 

def gv
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
                    echo "building jar..."
                    //pom.xml is updated so we build the jar with updated version
                    gv.buildApp()
                }         
            }
        }        
        stage ('build image & push to dockerhub'){
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
        stage('deploy on k8s cluster') {
            steps {
                script {
                    //https://plugins.jenkins.io/kubernetes-cli/
                    withKubeConfig([credentialsId: 'k8s-credentials', serverUrl: 'https://250DBC7854234J2H3G42J3H4.gr7.eu-west-3.eks.amazonaws.com']) {
                        //get dockerhub private repo creds from Jenkins
                        withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                            // create k8s secret of type'docker-resistry' to use in deployment's 'spec:imagePullSecrets'
                            // so the deployment can pull images from the private repo 
                            sh "kubectl create secret docker-registry docker-registry-secret --docker-server=docker.io --docker-username=$USER --docker-password=$PASS"
                        }
                        // envsubst replaces the env vars of `deployment.yaml' with actual values 
                        // the result is piped to be read from `kubectl apply -f` (dash syntax in the end)
                        sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
                    }
                }
            }
        }
        // we commit the updated pom.xml file to the remote repo
        // gitlab has jenkins public key
        stage('commit pom modification'){
            steps{
                script{
                    sh 'git status'
                    sh 'git branch'
                    sh 'git config --list'
                    sh 'git remote set-url origin git@github.com:miltozz/jenkins-java-demo.git'
                    sh 'git add .'
                    sh 'git commit -m "Jenkins ci : version bump on pom.xml"'
                    sh 'git push origin HEAD:jenkins/k8s'
                }
            }
        }
    }
}
    
    //   stage('commit with username and pass') {
    //         steps {
    //             script {
    //                 withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    //                     // git config here for the first time run
    //                     sh 'git config --global user.email "jenkins@example.com"'
    //                     sh 'git config --global user.name "jenkins"'
    
    //                     //or SSH into the jenkins server and set the git configuration
    //                     sh "git remote set-url origin https://${USER}:${PASS}@github.com/miltozz/jenkins-java-demo.git"
    //                     sh 'git add .'
    //                     sh 'git commit -m "ci: version bump"'
    //                     sh 'git push origin HEAD:jenkins/k8s'
    //                 }
    //             }
    //         }
    //     }