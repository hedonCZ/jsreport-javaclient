
# Java SDK for jsreport
[![Build Status](https://travis-ci.org/hedonCZ/jsreport-javaclient.png?branch=master)](https://travis-ci.org/hedonCZ/jsreport-javaclient)

**Java client for [jsreport](https://jsreport.net) reporting server**

## Get started
TODO


## Development

```
mvn install
docker run -d -p 9080:5488 jsreport/jsreport:2.9.0
docker run -d -p 10080:5488 -e "extensions_authentication_admin_username=admin" -e "extensions_authentication_admin_password=xxx" -e "extensions_authentication_cookieSession_secret=123456" jsreport/jsreport:2.9.0
mvn integration-test -B -P allTests
```