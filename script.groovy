/* def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub-private-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t miltosdev/my-private-repo:jda-2.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push miltosdev/my-private-repo:jda-2.0'
    }
}  

def buildImageForNexus(String imageName) {
    echo "building the nexus image..."
    withCredentials([usernamePassword(credentialsId: 'nx-cont-docker-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t $imageName ."
        sh "echo $PASS | docker login 111.11.111.242:8083 -u $USER --password-stdin"
        sh "docker push $imageName"
    }
} */


def deployApp() {
    echo 'script.groovy deploying the application...'
} 

return this
