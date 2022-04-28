package com.cc.teams.contacts.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cc.teams.contacts.server.dao.ContactRepository;
import com.cc.teams.contacts.server.dao.TeamRepository;
import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.ContactService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ContactServiceImpl implements ContactService {
	private ContactRepository contactRepository;

	private TeamRepository teamRepository;

	public ContactServiceImpl(ContactRepository contactRepository, TeamRepository teamRepository) {
		super();
		this.contactRepository = contactRepository;
		this.teamRepository = teamRepository;
	}

	@Override
	public Contact createContact(ContactDto contactDto) {
		Contact contact = new Contact();
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setMailAddress(contactDto.getMailAddress());
		setContactTeam(contactDto, contact);
		log.info("Created contact in db: {}", contact);
		return contactRepository.save(contact);
	}

	@Override
	public Optional<Contact> updateContact(Long contactId, ContactDto contactDto) {
		if (contactId != null) {
			Contact updatedContact = contactRepository.findById(contactId).orElse(null);
			if (updatedContact != null) {
				updatedContact.setFirstName(contactDto.getFirstName());
				updatedContact.setLastName(contactDto.getLastName());
				updatedContact.setMailAddress(contactDto.getMailAddress());
				setContactTeam(contactDto, updatedContact);
				log.info("Updated contact in db: {}", updatedContact);
				return Optional.of(contactRepository.save(updatedContact));
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<Contact> updateContactPartial(Long contactId, ContactDto contactDto) {
		if (contactId != null) {
			Contact updatedContact = contactRepository.findById(contactId).orElse(null);
			if (updatedContact != null) {
				if (contactDto.getFirstName() != null) {
					updatedContact.setFirstName(contactDto.getFirstName());
				}
				if (contactDto.getLastName() != null) {
					updatedContact.setLastName(contactDto.getLastName());
				}
				if (contactDto.getMailAddress() != null) {
					updatedContact.setMailAddress(contactDto.getMailAddress());
				}
				if (contactDto.getTeamId() != null) {
					setContactTeam(contactDto, updatedContact);
				}
				log.info("Updated contact partially in db: {}", updatedContact);
				return Optional.of(contactRepository.save(updatedContact));
			}
		}
		return Optional.empty();
	}

	@Override
	public void deleteContact(Long contactId) {
		if (contactId != null) {
			log.info("Deleted contact with ID in db: {}", contactId);
			contactRepository.deleteById(contactId);
		}
	}

	@Override
	public Page<Contact> findPagedContactsByTeamId(Long teamId, int pageIndex, int pageSize) {
		if (teamId != null) {
			Pageable pageRequest = PageRequest.of(pageIndex, pageSize);
			return contactRepository.findByTeam_Id(teamId, pageRequest);
		}
		return new PageImpl<>(Collections.emptyList());
	}

	@Override
	public Optional<Contact> findContactById(Long contactId) {
		if (contactId != null) {
			return contactRepository.findById(contactId);
		}
		return Optional.empty();
	}

	@Override
	public List<Contact> findAllContacts() {
		List<Contact> contacts = new ArrayList<>();
		Iterable<Contact> contactsIterable = contactRepository.findAll();
		contactsIterable.forEach(contacts::add);
		return contacts;
	}

	private void setContactTeam(ContactDto contactDto, Contact contact) {
		if (contactDto.getTeamId() != null) {
			Team team = teamRepository.findById(contactDto.getTeamId()).orElse(null);
			contact.setTeam(team);
		}
	}
}
