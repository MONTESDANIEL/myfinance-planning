package com.myfinance.backend.planning.respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.planning.entities.AppEvents;

public interface EventsRepository extends CrudRepository<AppEvents, Long> {

    List<AppEvents> findByUserId(Long userId);

}
