package com.micro.publisher.repository;

import com.micro.publisher.document.SavedData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SavedDataRepository extends MongoRepository<SavedData, String> {
    // Define any custom query methods if needed
}
