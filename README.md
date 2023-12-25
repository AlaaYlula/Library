# Library
The application.properties file :  
```application.properties 
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/<dataBaseName>
spring.datasource.username=<userName>
spring.datasource.password=<password>

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Elasticsearch
elasticSearchUrl = http://localhost:9200

spring.data.elasticsearch.cluster-name=<elasticsearch_clusterName>
spring.data.elasticsearch.cluster-nodes=localhost:9200
spring.data.elasticsearch.repositories.enabled=true


#logging.level.org.springframework=DEBUG
#logging.level.org.elasticsearch=DEBUG
```
1. create your postgres database, run the server.  
2. start the elasticsearch.  
3. run the App.  


### Category Controller

| Method                       |                        return                         |                body                 |
|:-----------------------------|:-----------------------------------------------------:|:-----------------------------------:|
| GET /category/all            |                   List of category                    |                                     |
| GET /category/get/{id}       |                       category                        |                                     |
| POST /category/add           | json String with message and status <br/>and will Log |    {"categoryName":"category1"}     |
| PUT /category/update/{id}    | json String with message and status <br/>and will Log | {"categoryName":"category1 update"} |
| DELETE /category/delete/{id} |             No_CONTENT <br/>and will Log              |                                                  |

### Book Controller

| Method                       |                        return                         |                       body                                |
|:-----------------------------|:-----------------------------------------------------:|:---------------------------------------------------------:|
| GET /book/all                |                     List of book                      |                                                           |
| GET /book/get/{id}           |                         book                          |                                                           |
| POST /book/add               | json String with message and status <br/>and will Log | param : categoryId  , Body: {"bookName":"book1"}          |
| PUT /book/update/{id}        | json String with message and status <br/>and will Log | param : categoryId  , Body: {"bookName":"book1 update"}   |
| DELETE /book/delete/{id}     |             No_CONTENT <br/>and will Log              |                                                           |

### ElasticSearch Controller
Level : warn,info and error

| Method                       |    return    |                       body                       |
|:-----------------------------|:------------:|:------------------------------------------------:|
| GET /elastic/search/{level}  | List of Logs |                                                  |
