package com.cc.teams.contacts.server.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cc.teams.contacts.server.application.TeamsContactsServerApplication;
import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link ContactController}.
 *
 */
@SpringBootTest(classes = { TeamsContactsServerApplication.class })
@AutoConfigureMockMvc
public class ContactControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ContactService contactService;

	@Test
	void givenContacts_whenGetContacts_thenReturnJsonArray() throws Exception {
		Team team1 = new Team();
		team1.setId(1L);
		team1.setName("team1");

		Contact contact1 = new Contact();
		contact1.setId(1L);
		contact1.setFirstName("contact1FirstName");
		contact1.setLastName("contact1LastName");
		contact1.setMailAddress("contact1@m.com");
		contact1.setTeam(team1);

		Contact contact2 = new Contact();
		contact2.setId(2L);
		contact2.setFirstName("contact2FirstName");
		contact2.setLastName("contact2LastName");
		contact2.setMailAddress("contact2@m.com");
		contact2.setTeam(team1);

		when(contactService.findAllContacts()).thenReturn(Arrays.asList(contact1, contact2));
		
		mvc.perform(get("/contacts")
			      .contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.contacts", hasSize(2)))
				.andExpect(jsonPath("$._embedded.contacts[0].firstName", is("contact1FirstName")))
				.andExpect(jsonPath("$._embedded.contacts[0].lastName", is("contact1LastName")))
				.andExpect(jsonPath("$._embedded.contacts[0].mailAddress", is("contact1@m.com")))
				.andExpect(jsonPath("$._embedded.contacts[0].teamId", is(1)))
				.andExpect(jsonPath("$._embedded.contacts[1].firstName", is("contact2FirstName")))
				.andExpect(jsonPath("$._embedded.contacts[1].lastName", is("contact2LastName")))
				.andExpect(jsonPath("$._embedded.contacts[1].mailAddress", is("contact2@m.com")))
				.andExpect(jsonPath("$._embedded.contacts[1].teamId", is(1)));
		verify(contactService, times(1)).findAllContacts();
	}

	@Test
	void givenContact_whenPartialUpdate_thenReturnUpdatedContact() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		ContactDto contactDto = new ContactDto();
		contactDto.setFirstName("contact1FirstNameUpdated");
		contactDto.setMailAddress("contact1MailAddressUpdated@m.com");

		Team team = new Team();
		team.setId(1L);
		team.setName("team1");

		Contact contactUpdated = new Contact();
		contactUpdated.setId(1L);
		contactUpdated.setFirstName("contact1FirstNameUpdated");
		contactUpdated.setLastName("contact1LastName");
		contactUpdated.setMailAddress("contact1MailAddressUpdated@m.com");
		contactUpdated.setTeam(team);

		when(contactService.updateContactPartial(1L, contactDto)).thenReturn(Optional.of(contactUpdated));

		mvc.perform(patch("/contacts/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contactDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is("contact1FirstNameUpdated")))
				.andExpect(jsonPath("$.lastName", is("contact1LastName")))
				.andExpect(jsonPath("$.mailAddress", is("contact1MailAddressUpdated@m.com")))
				.andExpect(jsonPath("$.teamId", is(1)));
		verify(contactService, times(1)).updateContactPartial(1L, contactDto);
	}

	@Test
	void givenIdForUnexistingContact_whenGetContacts_thenReturnNotFound() throws Exception {
		when(contactService.findContactById(1L)).thenReturn(Optional.empty());

		mvc.perform(get("/contacts/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		verify(contactService, times(1)).findContactById(1L);
	}
}
