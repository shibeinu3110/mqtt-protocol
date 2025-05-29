package com.micro.publisher.repository;

import com.micro.publisher.document.SavedData;
import com.micro.publisher.enumarate.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SavedDataRepository extends MongoRepository<SavedData, String> {
    List<SavedData> findByStatus(MessageStatus messageStatus);
    // Define any custom query methods if needed
}
