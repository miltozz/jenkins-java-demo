def buildJar() {
    echo "gv building the app jar..."
    sh "mvn clean package"
} 

def deployApp() {
    echo 'gv deploying the app...'
} 

def buildImageDocker(String imageName) {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t $imageName ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push miltosdev/my-private-repo:jma-1.0.0'
    }
}  

def buildImageNexus(String imageName) {
    echo "building the nexus image..."
    withCredentials([usernamePassword(credentialsId: 'nx-cont-docker-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t $imageName ."
        sh "echo $PASS | docker login 164.92.250.242:8083 -u $USER --password-stdin"
        sh "docker push $imageName"
    }
}



return this
