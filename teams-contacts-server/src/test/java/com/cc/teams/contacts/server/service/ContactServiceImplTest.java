package com.cc.teams.contacts.server.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cc.teams.contacts.server.dao.ContactRepository;
import com.cc.teams.contacts.server.dao.TeamRepository;
import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.impl.ContactServiceImpl;

/**
 * Test class for {@link ContactServiceImpl}.
 *
 */
@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {
	@Mock
	private ContactRepository contactRepository;

	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private ContactServiceImpl contactService;

	@Test
	void findAllContacts_success() {
		// Prepare mocks
		Team team = new Team();
		team.setId(1L);
		team.setName("team1");

		Contact contact1 = new Contact();
		contact1.setFirstName("contact1FirstName");
		contact1.setLastName("contact1LastName");
		contact1.setMailAddress("contact1MailAddress@m.com");
		contact1.setTeam(team);

		Contact contact2 = new Contact();
		contact2.setFirstName("contact2FirstName");
		contact2.setLastName("contact2LastName");
		contact2.setMailAddress("contact2MailAddress@m.com");
		contact2.setTeam(team);

		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact1);
		contacts.add(contact2);
		when(contactRepository.findAll()).thenReturn(contacts);

		List<Contact> foundContacts = contactService.findAllContacts();
		assertThat(foundContacts, hasSize(2));
		assertThat(foundContacts.get(0).getFirstName(), is("contact1FirstName"));
		assertThat(foundContacts.get(0).getLastName(), is("contact1LastName"));
		assertThat(foundContacts.get(0).getMailAddress(), is("contact1MailAddress@m.com"));
		assertThat(foundContacts.get(0).getTeam().getId(), is(1L));
		assertThat(foundContacts.get(1).getFirstName(), is("contact2FirstName"));
		assertThat(foundContacts.get(1).getLastName(), is("contact2LastName"));
		assertThat(foundContacts.get(1).getMailAddress(), is("contact2MailAddress@m.com"));
		assertThat(foundContacts.get(1).getTeam().getId(), is(1L));

		verify(contactRepository).findAll();
	}

	@Test
	void createContact_success() {
		// Prepare mocks
		Team team = new Team();
		team.setId(1L);
		team.setName("team1");
		when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

		Contact createdContact = new Contact();
		createdContact.setFirstName("contactFirstName");
		createdContact.setLastName("contactLastName");
		createdContact.setMailAddress("contactMailAddress@m.com");
		createdContact.setTeam(team);
		when(contactRepository.save(any(Contact.class))).thenReturn(createdContact);

		// Execute test method
		ContactDto contactDto = new ContactDto();
		contactDto.setFirstName("contactFirstName");
		contactDto.setLastName("contactLastName");
		contactDto.setMailAddress("contactMailAddress@m.com");
		contactDto.setTeamId(1L);

		// Assertions
		Contact contact = contactService.createContact(contactDto);
		assertThat(contact.getFirstName(), is("contactFirstName"));
		assertThat(contact.getLastName(), is("contactLastName"));
		assertThat(contact.getMailAddress(), is("contactMailAddress@m.com"));
		assertThat(contact.getTeam().getName(), is("team1"));

		verify(contactRepository).save(contact);
		verify(teamRepository).findById(1L);
	}

	@Test
	void updateContactPartial_success() {
		// Prepare mocks
		Team team = new Team();
		team.setId(1L);
		team.setName("team1");

		Contact updatedContact = new Contact();
		updatedContact.setId(1L);
		updatedContact.setFirstName("contactFirstName");
		updatedContact.setLastName("contactLastName");
		updatedContact.setMailAddress("contactMailAddress@m.com");
		updatedContact.setTeam(team);
		when(contactRepository.findById(1L)).thenReturn(Optional.of(updatedContact));
		when(contactRepository.save(any(Contact.class))).thenReturn(updatedContact);

		// Execute test method
		ContactDto contactDto = new ContactDto();
		contactDto.setFirstName("contactUpdatedFirstName");

		// Assertions
		Optional<Contact> contactOpt = contactService.updateContactPartial(1L, contactDto);
		Contact contact = contactOpt.get();
		assertThat(contact.getFirstName(), is("contactUpdatedFirstName"));
		assertThat(contact.getLastName(), is("contactLastName"));
		assertThat(contact.getMailAddress(), is("contactMailAddress@m.com"));
		assertThat(contact.getTeam().getName(), is("team1"));

		verify(contactRepository).save(contact);
		verify(contactRepository).findById(1L);
	}
}
