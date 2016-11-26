#!/bin/bash
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections
sudo apt-get -qq -y --force-yes install oracle-java8-set-default

echo 'JAVA_HOME="/usr/lib/jvm/java-8-oracle"' >> /etc/environment
source /etc/environment

sudo apt-get -y --force-yes install maven
sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password password 68Nj8Ap5V7'
sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password_again password 68Nj8Ap5V7'
sudo apt-get -y install mysql-server-5.5

echo 'M2_HOME="/usr/share/maven"' >> /etc/environment
source /etc/environment

sudo echo '<?xml version="1.0" encoding="UTF-8"?>
  <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <pluginGroups>
    <pluginGroup>org.eclipse.jetty</pluginGroup>
  </pluginGroups>
</settings>' > /usr/share/maven/conf/settings.xml

mysql -u root -p'68Nj8Ap5V7' < /home/vagrant/brdo/src/main/resources/sql/create-db-mysql.sql
mysql -u brdo -p'4En!W83*aM#i' -D business_constructor < /home/vagrant/brdo/src/main/resources/sql/init-mysql.sql

echo "*********** INSTALLATION FINISHED ***********."
cd brdo && mvn spring-boot:run