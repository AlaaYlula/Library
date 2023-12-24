package com.library.library.Excepion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomRestException {
    private  String message;
    private  HttpStatus httpStatus ;

}
