package com.cc.teams.contacts.server.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cc.teams.contacts.server.model.Team;

/**
 * Spring Data JPA repository class for {@link Team}s.
 *
 */
@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

}
