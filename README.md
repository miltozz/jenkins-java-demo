# WIP

<!-- # Jenkins pipeline to deploy java demo app on k8s cluster
Branch: jenkins/k8s

## Info
- Automatic version update in pom.xml
- Jenkins increments patch version and updates pom.xml with the new version
- Build the jar using the new pom.xml
- Build the new image and push it to dockerhub private repo
- Jenkins commits the version change(pom.xml modification) in the remote(origin) repo
- Using [withKubeConfig](https://plugins.jenkins.io/kubernetes-cli/) for kubectl
- Create a k8s secret (of type _docker-registry_) for use by the deployment. 
- Deployment logins to docker-private-repo by using the secret in `spec:template:spec:imagePullSecrets`
- envsubst replaces env vars with their values in deployment.yaml and pipes the result to `k apply -f -`


## Pre 
- Jenknins needs kubectl installed to run commands on the k8s cluster
- Jenkins needs Github access (ssh key) - Check Jenkins container /var/jenkins_home/.ssh -->
