package com.library.library.excepion;

import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.Logs;
import com.library.library.entity.levelEnum.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler { //Extends So I can handle any other Exceptions as WARN
    @Autowired
    ElasticSearchQuery elasticSearchQuery;
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleAlreadyExistException(CustomException conflictError){
        List<String> details = new ArrayList<String>();
        details.add(conflictError.getMessage());
        CustomRestException customException = new CustomRestException("Already Exists", HttpStatus.BAD_REQUEST,details);
        indexDocument(conflictError.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            ResourceNotFoundException ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        CustomRestException customException = new CustomRestException("Resource Not Found Exception", HttpStatus.NOT_FOUND,details);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleEntityFieldNULL(
            DataIntegrityViolationException ex) {
        List<String> details = new ArrayList<String>();
        String filed = ex.getMessage().substring(ex.getMessage().lastIndexOf('.') + 1).trim();
        details.add(filed +" property must be not null, to create or update the "+filed);

        CustomRestException customException = new CustomRestException("Check your Json body", HttpStatus.BAD_REQUEST,details);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleParamNotFound(
            MissingServletRequestParameterException ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getParameterName()+" parameter is missing");
        CustomRestException customException = new CustomRestException("Missing Parameters", HttpStatus.BAD_REQUEST,details);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        CustomRestException customException = new CustomRestException("Check the JSON Format", HttpStatus.BAD_REQUEST,details);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            MethodArgumentTypeMismatchException ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        CustomRestException customException = new CustomRestException("Data type MisMatch ", HttpStatus.BAD_REQUEST,details);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        List<String> details = new ArrayList<String>();
        details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getResourcePath()));
        CustomRestException customException = new CustomRestException("Method Not Found", HttpStatus.BAD_REQUEST,details);
        indexDocument(ex.getMessage(),Level.WARN);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }

    // Any other Warning
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherExceptions(
            Exception ex) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        CustomRestException customException = new CustomRestException("WARN, Exception Not handled", HttpStatus.NOT_FOUND,details);
        indexDocument(ex.getMessage(),Level.WARN);
        return new ResponseEntity<>(customException , HttpStatus.NOT_FOUND);

    }
    private void indexDocument(String message,Enum<Level> level) {
        Date date = new Date();
        Logs log = new Logs(message, level, date);
        try {
            System.out.println(elasticSearchQuery.createDocumentLog(log));
        } catch (Exception e) {
            System.err.println("Error creating document log: " + e.getMessage());
        }
    }


}
