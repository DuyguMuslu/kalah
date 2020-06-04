package com.kalah.domain;

import com.kalah.domain.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * To run CRUD operation on data
 */
public interface GameRepository extends CrudRepository<Game, Integer> { }
