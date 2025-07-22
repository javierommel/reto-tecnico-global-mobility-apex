package com.rommelchocho.orderworker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.rommelchocho.orderworker.model.EnrichedOrder;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<EnrichedOrder, String> {
}
