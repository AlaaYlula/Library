package com.library.library.Excepion;

import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.levelEnum.Level;
import com.library.library.Entity.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
// extends SimpleMappingExceptionResolver
@ControllerAdvice
public class customExceptionHandler{ //Extends So I can handle any other Exceptions as WARN
    @Autowired
    ElasticSearchQuery elasticSearchQuery;

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleAlreadyExistException(CustomException conflictError){
        CustomRestException customException = new CustomRestException(conflictError.getMessage(), HttpStatus.BAD_REQUEST);
        indexDocument(conflictError.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);
    }

    // check if the Book or Category not in the database
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            ResourceNotFoundException ex) {
        CustomRestException customException = new CustomRestException(ex.getMessage(), HttpStatus.NOT_FOUND);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleEntityFieldNULL(
            DataIntegrityViolationException ex) {
        CustomRestException customException = new CustomRestException(ex.getMessage(), HttpStatus.CONFLICT);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.CONFLICT);

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleParamNotFound(
            MissingServletRequestParameterException ex) {
        CustomRestException customException = new CustomRestException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        indexDocument(ex.getMessage(),Level.ERROR);
        return new ResponseEntity<>(customException , HttpStatus.BAD_REQUEST);

    }

    // Any other Warning
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherExceptions(
            Exception ex) {
        CustomRestException customException = new CustomRestException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        indexDocument(ex.getMessage(),Level.WARN);
        return new ResponseEntity<>(customException , HttpStatus.INTERNAL_SERVER_ERROR);

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
