#!/bin/sh
java ${JVM_OPTS} -Dlogback.configurationFile=./logback-console.xml -jar app.jar -conf config.json