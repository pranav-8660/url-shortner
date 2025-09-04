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

##### Pull the mysql docker image, create container with custom configurations, login to the mysql container, create a db
```bash
docker pull mysql:latest

docker run -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=some_password --name some_container_name mysql:latest

docker exec -it some_container_name mysql -u root -p

create database db_name;
