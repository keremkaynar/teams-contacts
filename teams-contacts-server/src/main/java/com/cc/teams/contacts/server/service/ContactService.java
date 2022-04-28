package com.cc.teams.contacts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;

/**
 * Service class for managing {@link Contact}s.
 *
 */
public interface ContactService {
	/**
	 * Creates a {@link Contact} in db.
	 * 
	 * @param contactDto {@link ContactDto} to be used in creating a contact
	 * @return created {@link Contact}
	 */
	Contact createContact(ContactDto contactDto);

	/**
	 * Updates a {@link Contact} in db, if exists.
	 * 
	 * @param contactDto {@link ContactDto} to be used in updating a contact
	 * @param contactId  ID of the {@link Contact} to be updated
	 * @return updated {@link Contact} if already exists in db, otherwise
	 *         {@link Optional#empty()}
	 */
	Optional<Contact> updateContact(Long contactId, ContactDto contactDto);

	/**
	 * Updates a {@link Contact} in db partially, if exists.
	 * 
	 * @param contactDto {@link ContactDto} to be used in updating a contact
	 * @param contactId  ID of the {@link Contact} to be updated
	 * @return updated {@link Contact} if already exists in db, otherwise
	 *         {@link Optional#empty()}
	 */
	Optional<Contact> updateContactPartial(Long contactId, ContactDto contactDto);

	/**
	 * Deletes a {@link Contact} in db.
	 * 
	 * @param contactId ID of the {@link Contact} to be deleted
	 */
	void deleteContact(Long contactId);

	/**
	 * Finds the {@link Contact}s existing in db according to the given team ID,
	 * page index and size.
	 * 
	 * @param teamId    {@link Team} ID
	 * @param pageIndex page index
	 * @param pageSize  page size
	 * @return {@link Page} containing found {@link Contact}s
	 */
	Page<Contact> findPagedContactsByTeamId(Long teamId, int pageIndex, int pageSize);

	/**
	 * Gets a {@link Contact} in db by ID, if exists.
	 * 
	 * @param contactId ID of the {@link Contact} to be found
	 * @return found {@link Contact} if already exists in db, otherwise
	 *         {@link Optional#empty()}
	 */
	Optional<Contact> findContactById(Long contactId);

	/**
	 * Gets all {@link Contact}s in db.
	 * 
	 * @return all {@link Contact}s in db
	 */
	List<Contact> findAllContacts();
}
