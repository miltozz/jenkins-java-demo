def buildApp() {
    echo "building the app..."
    // mvn clean package to delete older jars
    // Dockerfile builds with regexp(*) so it needs one jar only
    sh "mvn clean package"
}

def testApp() {
    echo "testing the app..."
}

def deployApp() {
    echo "deploying the app..."    
}

return this
