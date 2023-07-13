# jenkins-java-demo/jenkins%2Ftest-ssh-agent

- Testing Jenkins plugin [SSH Agent](https://plugins.jenkins.io/ssh-agent/)
- Build an image for the java demo app
- Push the image to private docker repo
- Needs a remote target node, with ec2-user (amz linux 2)
- Use SSH Agent plugin in Jenkinsfile to connect to target node with private key creds created in Jenkins UI - Creds Manager. Copy files with scp and run commands with ssh.
- Target Node needs docker and docker-compose installed
- Update IMAGE_NAME and ec2Instance values in Jennkinsfile. 
- Update inbound SG rules in AWS for remote target
- Jenkinsfile has shared library config and various stages but only the stage `deploy ssh-agent` actually does something :) Jenkinsfile needs cleaning.

- A better way to pass Docker creds in the remote host can be found on the Jenkinsfile of [this repo](https://github.com/miltozz/jenkins-java-demo/-/tree/jenkins/ssh-agent-terraform) using `DOCKER_CREDS = credentials('dockerhub-private-repo')`
