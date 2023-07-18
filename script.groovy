def buildApp() {
    echo "building the app with mvn clean package..."
    sh "mvn clean package"
}

def testApp() {
    echo "testing the app..."
    echo "groovy script testApp() debug echo"
}

def deployApp() {
    echo "deplloying the app..."    
}

return this
