#!/bin/bash

mvn clean install -DskipTests
scp crawlers/target/crawlers-1.0-SNAPSHOT.war root@voxxrin:/data/crawlers/jetty/webapps/ROOT.war