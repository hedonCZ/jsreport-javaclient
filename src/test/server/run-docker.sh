#!/bin/bash

IMG="jsreport/jsreport:2.9.0"
NAME="javaclient.jsreport"

docker pull ${IMG}
docker run --rm --name ${NAME} -d -p 9080:5488 ${IMG}

