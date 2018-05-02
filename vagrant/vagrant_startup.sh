#!/bin/bash
cd /home/vagrant/brdo && mvn clean && nohup mvn -e spring-boot:run 2>&1 &