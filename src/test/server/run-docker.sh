#!/bin/bash

docker pull jsreport/jsreport

# EXTRA_ARGS="-i -t --entrypoint /bin/bash"
EXTRA_ARGS="-i -t"
NAME="javaclient.jsreport"
DATA_DIR="./jsreport"

pushd .
cd ${DATA_DIR}
APPDATA=`pwd`
popd

echo "DATA_DIR=${DATA_DIR}"
echo "APPDATA=${APPDATA}"

docker run ${EXTRA_ARGS} --rm --name ${NAME} --hostname ${NAME}  \
    -p 9080:5488                                              \
    -v "${APPDATA}:/jsreport    "                                \
    jsreport/jsreport

