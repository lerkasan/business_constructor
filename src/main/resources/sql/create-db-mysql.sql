DROP DATABASE business_constructor;
CREATE DATABASE business_constructor
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON business_constructor.* TO 'brdo'@'localhost';