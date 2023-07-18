# Automatic version bump in pom.xml
Full project name: jenkins-java-demo/auto-versioning

## Info
- Automatic version update in pom.xml
- Jenkins increments patch version and updates pom.xml with the new version
- Build the jar using the new pom.xml
- Build the new image and push it to dockerhub private repo
- Jenkins commits the version change(pom.xml modification) in the remote(origin) repo
- Pull and deploy container from the new image in provisioned EC2 Linux2 server, using SSH Agent plugin


## Pre 
- EC2 remote host needs docker installed and SG inbound 22 from Jenkins and Inbound 22,3000 from MyIp
- Jenkins needs Github access (ssh key) - Check Jenkins container /var/jenkins_home/.ssh

## Install docker on EC2 with AmzLinux2
- [Official](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/create-container-image.html#create-container-image-install-docker) Not working?
- `sudo yum install docker` 

## Note for Docker login
When no password is passed in docker login the output error is: `Error: Cannot perform an interactive login from a non TTY device`