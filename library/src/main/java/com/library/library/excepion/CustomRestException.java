package com.library.library.excepion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomRestException {
    private  String message;
    private  HttpStatus httpStatus ;
    private List errors;

}
