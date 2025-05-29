package com.micro.publisher.document;

import com.micro.publisher.enumarate.MessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;


@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "saved_data")
public class SavedData {
    String data;
    LocalDateTime date;
    MessageStatus status;
}
