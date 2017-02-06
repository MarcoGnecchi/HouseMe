#!/bin/bash
aws ecr get-login --region eu-west-2
docker build -t houseme .
docker tag houseme:latest 312827138267.dkr.ecr.eu-west-2.amazonaws.com/houseme:latest
docker push 312827138267.dkr.ecr.eu-west-2.amazonaws.com/houseme:latest