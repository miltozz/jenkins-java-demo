def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def dockerImageBP() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t miltosdev/my-private-repo:jda-v1.0.0 .'
        sh 'echo Docker Login..'
        sh "echo $PASS | docker login -u $USER --password-stdin" //plus repo host when not DockerHub
        sh 'echo Docker push image...'
        sh 'docker push miltosdev/my-private-repo:jda-v1.0.0'
    }
} 

def deployApp() {
    echo 'deploying the application...'
} 

return this
