# Jenkins Shared Library Demo

## Info
- Building the demo java app image and pushing to docker private repo
- We use a Jenkins Shared Library we created [here](https://github.com/miltozz/jenkins-shared-library)
- We define the shared librabry for project only. We DO NOT use a Global Shared Library defined in the Jenkins GUI: Manage Jenkins->System Configuration->Configure System->Global Pipelines.  
- The shared lib config is done on the Jenkinsfile.
- Check Jenkinsfile for comments and notes

## Repos
- [This](https://github.com/miltozz/jenkins-java-demo/tree/jenkins-shared-lib-branch) repo is using/calls the library in the Jenkinsfile
- The actual shared library implementation [repo](https://github.com/miltozz/jenkins-shared-library) 

