### A url-shortner to be developed using spring-boot backend, mysql, redis, react-js frontend

#### Updates -> 
- Done with url modelling
- Done with the db repo,service
- Done with the redis config,service for caching

#### Pending->
- Configuration of the db server
- Configuration of the redis server
- UI
- Dockerization


##### Pull the mysql docker image
``` docker pull mysql:latest

##### Create and Run the container for the mysql_image with suitable credentials
``` docker run -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD some_password --name some_container_name mysql:latest

##### Go into the mysql container to create database
``` docker exec -it some_container_name mysql -u root -p (enter the password and go into the mysql_container to use mysql)

##### Create a database
``` create database db_name;
