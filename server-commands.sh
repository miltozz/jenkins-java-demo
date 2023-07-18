#!/usr/bin/env bash 

export MYENVVAR="myenvvar_value"
# export the provided argument image name as an environment variable in the remote host
export IMAGE=$1
#compose reads .env files for values but host env vars precede the .env file
docker-compose -f docker-compose.yml up --detach
echo "start debug echo. first MYENVVAR then IMAGE "
echo $MYENVVAR
echo $IMAGE
echo "end debug echo"