package com.library.library.configuration.logging;


import com.library.library.apiResponse.Response;
import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.levelEnum.Level;
import com.library.library.entity.Logs;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class loggingSuccess {

    @Autowired
    ElasticSearchQuery elasticSearchQuery;
    @AfterReturning(pointcut = "execution(* com.library.library.controller.*.*(..))", returning = "response")
    public void logSuccessfulResponse(ResponseEntity<?> response) {
        String message = null;
        if (response != null &&  response.getBody()!=null) {

            if(response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.NO_CONTENT) {
                    ModelMapper modelMapper = new ModelMapper();
                    Response responseReceived = modelMapper.map(response.getBody(),Response.class);

                    // Access the "message" property
                    message = responseReceived.getMessage();
                    // Log the document
                    Date date = new Date();
                    Logs log = new Logs(message, Level.INFO, date);
                    elasticSearchQuery.createDocumentLog(log);
            }

        }

    }
}
