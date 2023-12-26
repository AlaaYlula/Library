package com.library.library.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.library.entity.levelEnum.Level;
import com.library.library.entity.levelEnum.LevelDeserializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(indexName = "logsearch")
@JsonIgnoreProperties(ignoreUnknown = true) // It will back from Elastic _class object and it is not in Logs class
public class Logs {

    @Id
    @JsonIgnore
    public String id;
    public String message;
    @JsonDeserialize(using = LevelDeserializer.class)
    public Enum<Level> level;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    Date timeStamp;

    public Logs(String message, Enum<Level> level, Date timeStamp) {
        this.message = message;
        this.level = level;
        this.timeStamp = timeStamp;
    }

}
