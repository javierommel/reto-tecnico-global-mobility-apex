package com.rommelchocho.orderworker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.rommelchocho.orderworker.model.EnrichedOrder;

public interface OrderRepository extends ReactiveMongoRepository<EnrichedOrder,String>{

}
