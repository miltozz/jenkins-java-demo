def externalPre() {
    echo "External pre echo from script.groovy.. So nice!"
    echo "Version information printed by script.groovy: ${params.VERSION}"
}

def checkPreExecution(){
    return params.doExecutePre
}

def testStage() {
    echo 'testing by testStage()....'
    echo "Echo SOME_STRING: ${SOME_STRING}"
    // !!!Only in MULTIBRANCH pipelines
    //echo "Testing branch $BRANCH_NAME"
    echo "The value of environmental var, SOME_VAR is: ${SOME_VAR}"    
}

return this
