#!/bin/bash

ssh -v oroblam@178.62.109.195 << EOF

echo '1. Updating sources'
cd webmonitor
git checkout master
git pull

mvn spring-boot:run

EOF