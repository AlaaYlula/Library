package com.library.library.Configuration.logging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.AppResponse.Response;
import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.levelEnum.Level;
import com.library.library.Entity.Logs;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Aspect
@Component
public class loggingSuccess {

    @Autowired
    ElasticSearchQuery elasticSearchQuery;
    @AfterReturning(pointcut = "execution(* com.library.library.Controller.*.*(..))", returning = "response")
    public void logSuccessfulResponse(ResponseEntity<?> response) {
        String message = null;
        if (response != null &&  response.getBody()!=null) {

            if(response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.NO_CONTENT) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    Response responseReceived = objectMapper.readValue(response.getBody().toString(), Response.class);

                    // Access the "message" property
                    message = responseReceived.getMessage();
                    System.out.println("The Message "+message);
                    // Log the document
                    Date date = new Date();
                    Logs log = new Logs(message, Level.INFO, date);
                    System.out.println(elasticSearchQuery.createDocumentLog(log));
                } catch (IOException e) {
                    System.err.println("Error deserializing the response body: " + e.getMessage());
                }
            }else{
                System.out.println("It is Getting");
            }

        }

    }
}
