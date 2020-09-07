#!/bin/bash

IMG="jsreport/jsreport:2.9.0"
NAME="javaclient.jsreport"
NAME_AUTH="javaclient.auth.jsreport"

docker pull ${IMG}
docker run --rm --name ${NAME} -d -p 9080:5488 ${IMG}
docker run --rm --name ${NAME_AUTH} -d -p 10080:5488          \
  -e "extensions_authentication_admin_username=admin"         \
  -e "extensions_authentication_admin_password=xxx"           \
  -e "extensions_authentication_cookieSession_secret=123456"  \
 ${IMG}
