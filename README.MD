

# Java SDK for jsreport
[![build status](https://github.com/hedonCZ/jsreport-javaclient/actions/workflows/build.yml/badge.svg)](https://github.com/hedonCZ/jsreport-javaclient/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hedoncz/jsreport-javaclient/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.hedoncz/jsreport-javaclient)

**Java client for [jsreport](https://jsreport.net) reporting server**

## Get started

Install [maven](https://maven.apache.org/) dependency:
```xml
<dependency>
    <groupId>io.github.hedoncz</groupId>
    <artifactId>jsreport-javaclient</artifactId>
    <version>1.0.10</version>
</dependency>
```

Assuming your [jsreport server](https://jsreport.net/) is reachable on `https://localhost:5488`, you can render a report this way:
```java
JsReportService reportingService = new JsReportServiceImpl("http://localhost:5488");

Template template = new Template();
template.setContent("Hello world from {{message}}");
template.setEngine(Engine.HANDLEBARS);
template.setRecipe(Recipe.CHROME_PDF);

HashMap data = new HashMap();
data.put("message", "java");

RenderRequest renderRequest = new RenderRequest(template, data);

Report report = reportingService.render(renderRequest);
Files.copy(report.getContent(), Paths.get("report.pdf"), StandardCopyOption.REPLACE_EXISTING);
```

The persisted template can be rendered using `render` accepting template name (or path) as the first argument.

```java
Report report = reportingService.render("invoice-main", myData);
Files.copy(report.getContent(), Paths.get("report.pdf"), StandardCopyOption.REPLACE_EXISTING);
```

Additionally you can also dynamicaly create template

```java
Template template = new Template("myTemplate");
template.setContent("Hello world from {{message}}");
template.setEngine(Engine.HANDLEBARS);
template.setRecipe(Recipe.CHROME_PDF);
reportingService.puTemplate(template)
// or detele
reportingService.removeTemplate(template._id)
```

## Examples

Here is a dedicated repository with a few examples:    
https://github.com/hedonCZ/jsreport-java-examples

## Development

* to run integration tests:
```
mvn install
docker run -d -p 9080:5488 jsreport/jsreport:2.9.0
docker run -d -p 10080:5488 -e "extensions_authentication_admin_username=admin" -e "extensions_authentication_admin_password=xxx" -e "extensions_authentication_cookieSession_secret=123456" jsreport/jsreport:2.9.0
mvn integration-test -B -P allTests
```

* to run integration tests with JaCoCo coverage report: 
```
mvn install
docker run -d -p 9080:5488 jsreport/jsreport:2.9.0
docker run -d -p 10080:5488 -e "extensions_authentication_admin_username=admin" -e "extensions_authentication_admin_password=xxx" -e "extensions_authentication_cookieSession_secret=123456" jsreport/jsreport:2.9.0
mvn verify -B -P allTests
```

I accept contributions, don't hesitate to fill an issue and discuss PRs.
