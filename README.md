# Jenkins - SSH Agent - Terraform AWS

## Info
- Jenkins pulls repo in `jenkins-java-demo/jenkins/ssh-agent-terraform`
- Jenkins runs Terraform and provisions an EC2 Linux2 server and basic AWS resources on eu-west-3
- Terraform executes user-data script on EC2, which installs Docker and docker-compose
- Using Jenkins Tools (maven) build the jar using function from Jenkins Shared Library
- Build image, login to docker, push image
- Use Jenkins plugin [SSHAgent](https://plugins.jenkins.io/ssh-agent/) to copy files from Jenkins to the provisioned EC2 remote host and then run docker-compose to _up_ containers, from 2 Docker Images(java-maven-app, postgres) on the EC2 Instance.

## Pre
- Using Jenkins Shared Library from repo: [JSL](https://github.com/miltozz/jenkins-shared-library)
- Terraform  on Jenkins container
- Terraform is installed and run on the Jenkins container
- Terraform does not have a .tfars file, all values are defaulted in `variables.tf`
- Update jenkins_ip on `Jenkinsfile` and on Terraform `variables.tf` for SG Ingress
- Jenkins Container debug
    - `docker exec -it -u 0 bold_meitner /bin/bash`
    - `chmod -v 777 /var/run/docker.sock`

## Run Jenkins container on ec2
`docker run -p 8080:8080 -p 50000:^C000 -d -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):/usr/bin/docker jenkins/jenkins:lts-jdk11`

## Provisioned AWS resources
```
$ terraform state list

data.aws_ami.amazon-linux-latest
aws_instance.myapp-server
aws_internet_gateway.myapp-igw
aws_key_pair.myapp-ssh-key
aws_route_table.myapp-route-table
aws_route_table_association.a-rtb-subnet
aws_security_group.myapp-sg
aws_vpc.myapp-vpc
```

- More comments on Jenkisfile



