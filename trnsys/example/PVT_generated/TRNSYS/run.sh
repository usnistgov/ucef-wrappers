#!/bin/bash
java -Djava.net.preferIPv4Stack=true -Dlog4j.configurationFile=conf/log4j2.xml -jar trnsys-wrapper-0.1.0-SNAPSHOT.jar conf/TRNSYS.json
