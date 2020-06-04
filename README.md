KALAH GAME
----------------------------------------
This is a java restful services project for Kalah game with 2 players and 6 stones.

Build
-----
- Clone the project to your local environment 
- To run the project in local 
```
mvnw clean install

```
 
Usage
--------------- 
- REST APIs can be found on following URL 
  POST ** http://localhost:8080/games 
  PUT  ** http://localhost:8080/games/{gameid}/pits/{pitid}

- DB Console can be accessed via http://localhost:8080/h2-console/login.jsp
     .No password required, JDBC Url is `jdbc:h2:file:~/kalah-schema;AUTO_SERVER=TRUE`  
       
- Sample requests are listed on a postman collection
     https://www.getpostman.com/collections/f6956778d828e5892605
```
Download__ : https://www.postman.com 
```
 