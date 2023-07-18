def buildJar() {
    echo "building the application with mvn package..."
    sh 'mvn package'
} 

def buildImage() {
    echo "gv building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t miltosdev/my-private-repo:jda-v1.0.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push miltosdev/my-private-repo:jda-v1.0.0'
    }
}

def deployApp() {
    echo 'deploying the application...'
} 

return this
