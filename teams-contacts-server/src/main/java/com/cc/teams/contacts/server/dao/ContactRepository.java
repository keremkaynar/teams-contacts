package com.cc.teams.contacts.server.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cc.teams.contacts.server.model.Contact;

/**
 * Spring Data JPA repository class for {@link Contact}s.
 *
 */
@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
	Page<Contact> findByTeam_Id(Long teamId, Pageable pageable);
}
