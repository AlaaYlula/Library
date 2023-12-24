package com.library.library.AppResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    @JsonProperty("message")
    String message;
    @JsonProperty("status")
    HttpStatus status;


    // I use this to extract the message from the response with status CREATE(add,update) or NO_CONTENT(delete)
    // for index the INFO to the elastic search when I create or delete successfully.
  public static String convertToJsonString(String message , HttpStatus httpStatus) throws JsonProcessingException {
      Response response = new Response(message,httpStatus);
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(response);
      return jsonString;
  }

}
