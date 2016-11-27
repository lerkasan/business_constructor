#!/bin/bash
cd brdo && mvn clean && nohup mvn -e spring-boot:run 2>&1 &