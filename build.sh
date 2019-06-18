#!/bin/bash
rootdir=`pwd`

cd $rootdir/trnsys-wrapper
mvn clean install

cd $rootdir/example/PVT_generated/PVT-java-federates/ExternalJava
mvn clean install

cd $rootdir/example/PVT_generated/TRNSYS
mvn clean install

cd $rootdir/example/PVT_deployment
mvn clean install

