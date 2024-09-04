-- Create the bookings schema
CREATE DATABASE IF NOT EXISTS booking;

-- Create the theatres schema
CREATE DATABASE IF NOT EXISTS theatre;

CREATE USER 'dev'@'%' IDENTIFIED BY 'dev';
GRANT ALL PRIVILEGES ON *.* TO 'dev'@'%' WITH GRANT OPTION;
flush privileges;