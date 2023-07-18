//def gv

pipeline{
    agent any

    stages{
        stage ("init"){
            steps{
                script{
                    echo "Init.."
                }
            }
        }
        stage ("test"){
            steps{
                script{
                    echo "testing.."
                    //MULTIBRANCH ONLY
                    //echo "Testing branch $BRANCH_NAME"
                    echo "testing automatic trigger/webhooks/integrations"
                }
            }
        }        
        stage("build"){
            // when {
            //     expression{
            //         BRANCH_NAME == 'main'
            //     }
            // }            
            steps{
                script{
                    echo "building.."
                    //gv.buildApp()
                }
                
            }
        }
        stage("deploy"){
            // when {
            //     expression{
            //         BRANCH_NAME == 'main'
            //     }
            // }             
            steps{
                echo "deploying..."
            }
        }
    }
}
