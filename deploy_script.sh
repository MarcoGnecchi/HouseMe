#!/bin/bash
docker_login_cmd='aws ecr get-login --region eu-west-2 --profile HouseMe';
#echo "$docker_login"
eval $($docker_login_cmd);
if [[ $? -ne 0 ]] ; then
    echo "Docker login failed"
    exit 1	
else 
	echo "Docker login successful"
fi
docker build -t houseme .
docker tag houseme:latest 312827138267.dkr.ecr.eu-west-2.amazonaws.com/houseme:latest
docker push 312827138267.dkr.ecr.eu-west-2.amazonaws.com/houseme:latest