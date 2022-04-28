package com.cc.teams.contacts.server.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.cc.teams.contacts.server.application.TeamsContactsServerApplication;
import com.cc.teams.contacts.server.dao.ContactRepository;
import com.cc.teams.contacts.server.dao.TeamRepository;
import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Contains integration tests for {@link Team}s and {@link Contact}s.
 *
 */
@SpringBootTest(classes = TeamsContactsServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamsContactsIT {
	@LocalServerPort
	private int localPort;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TestRestTemplate restTemplate;

	@AfterEach
	void cleanup() {
		contactRepository.deleteAll();
		teamRepository.deleteAll();
	}

	@Test
	void createTeam_success() throws JsonProcessingException {
		// Create and send post team request
		TeamDto teamDto = new TeamDto();
		teamDto.setName("team1");
		ResponseEntity<String> response = sendPostTeamRequest(teamDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

		Iterable<Team> teams = teamRepository.findAll();
		Iterator<Team> teamsIterator = teams.iterator();
		assertThat(teamsIterator.hasNext(), is(true));
		Team team = teamsIterator.next();
		assertThat(team.getName(), is("team1"));
		assertThat(team.getId(), notNullValue());
	}

	@Test
	void updateTeam_success() throws JsonProcessingException {
		// Create and send put team request
		TeamDto teamDto = new TeamDto();
		teamDto.setName("team1");
		ResponseEntity<String> response = sendPostTeamRequest(teamDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		Iterable<Team> teams = teamRepository.findAll();
		Iterator<Team> teamsIterator = teams.iterator();
		assertThat(teamsIterator.hasNext(), is(true));
		Team team = teamsIterator.next();

		teamDto = new TeamDto();
		teamDto.setName("team2");
		sendPutTeamRequest(team.getId(), teamDto);
		teams = teamRepository.findAll();
		teamsIterator = teams.iterator();
		assertThat(teamsIterator.next().getName(), is("team2"));
	}

	@Test
	void deleteTeam_success() throws JsonProcessingException {
		// Create and send post team request
		TeamDto teamDto = new TeamDto();
		teamDto.setName("team1");
		ResponseEntity<String> response = sendPostTeamRequest(teamDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		Iterable<Team> teams = teamRepository.findAll();
		Iterator<Team> teamsIterator = teams.iterator();
		assertThat(teamsIterator.hasNext(), is(true));
		Team team = teamsIterator.next();

		// Send delete team request
		sendDeleteTeamRequest(team.getId());
		teams = teamRepository.findAll();
		teamsIterator = teams.iterator();
		assertThat(teamsIterator.hasNext(), is(false));
	}

	@Test
	void updateContact_success() throws JsonProcessingException {
		// Create and send put contact request
		ContactDto contactDto = new ContactDto();
		contactDto.setFirstName("contactFirstName");
		contactDto.setLastName("contactLastName");
		contactDto.setMailAddress("contactMailAddress@m.com");
		ResponseEntity<String> response = sendPostContactRequest(contactDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		Iterable<Contact> contacts = contactRepository.findAll();
		Iterator<Contact> contactsIterator = contacts.iterator();
		assertThat(contactsIterator.hasNext(), is(true));
		Contact contact = contactsIterator.next();

		contactDto = new ContactDto();
		contactDto.setFirstName("contactUpdatedFirstName");
		contactDto.setLastName("contactUpdatedLastName");
		contactDto.setMailAddress("contactUpdatedMailAddress@m.com");

		sendPutContactRequest(contact.getId(), contactDto);
		contacts = contactRepository.findAll();
		contactsIterator = contacts.iterator();
		contact = contactsIterator.next();
		assertThat(contact.getFirstName(), is("contactUpdatedFirstName"));
		assertThat(contact.getLastName(), is("contactUpdatedLastName"));
		assertThat(contact.getMailAddress(), is("contactUpdatedMailAddress@m.com"));
	}

	@Test
	void createContact_success() throws JsonProcessingException {
		// Create and send post contact request
		TeamDto teamDto = new TeamDto();
		teamDto.setName("team1");
		ResponseEntity<String> response = sendPostTeamRequest(teamDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		Iterable<Team> teams = teamRepository.findAll();
		Iterator<Team> teamsIterator = teams.iterator();
		assertThat(teamsIterator.hasNext(), is(true));
		Team team = teamsIterator.next();

		// Create and send post contact request
		ContactDto contactDto = new ContactDto();
		contactDto.setFirstName("contactFirstName");
		contactDto.setLastName("contactLastName");
		contactDto.setMailAddress("contactMailAddress@m.com");
		contactDto.setTeamId(team.getId());
		response = sendPostContactRequest(contactDto);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

		Iterable<Contact> contacts = contactRepository.findAll();
		Iterator<Contact> contactsIterator = contacts.iterator();
		assertThat(contactsIterator.hasNext(), is(true));
		Contact contact = contactsIterator.next();
		assertThat(contact.getFirstName(), is("contactFirstName"));
		assertThat(contact.getLastName(), is("contactLastName"));
		assertThat(contact.getMailAddress(), is("contactMailAddress@m.com"));
		assertThat(contact.getId(), notNullValue());
		assertThat(contact.getTeam().getName(), is("team1"));
	}

	private ResponseEntity<String> sendPostContactRequest(ContactDto contactDto) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(contactDto), headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + localPort + "/contacts",
				request, String.class);
		return response;
	}

	private ResponseEntity<String> sendPostTeamRequest(TeamDto teamDto) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(teamDto), headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + localPort + "/teams",
				request, String.class);
		return response;
	}

	private void sendPutTeamRequest(Long teamId, TeamDto teamDto) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(teamDto), headers);
		restTemplate.put("http://localhost:" + localPort + "/teams/" + teamId, request);
	}

	private void sendDeleteTeamRequest(Long teamId) throws JsonProcessingException {
		restTemplate.delete("http://localhost:" + localPort + "/teams/" + teamId);
	}

	private void sendPutContactRequest(Long contactId, ContactDto contactDto) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(contactDto), headers);
		restTemplate.put("http://localhost:" + localPort + "/contacts/" + contactId, request);
	}
}
