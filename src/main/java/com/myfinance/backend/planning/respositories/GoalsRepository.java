package com.myfinance.backend.planning.respositories;

import org.springframework.data.repository.CrudRepository;

import com.myfinance.backend.planning.entities.AppGoals;

public interface GoalsRepository extends CrudRepository<AppGoals, Long> {

}
