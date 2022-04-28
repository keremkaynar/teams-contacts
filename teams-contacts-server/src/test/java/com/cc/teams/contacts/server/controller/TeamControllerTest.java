package com.cc.teams.contacts.server.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cc.teams.contacts.server.application.TeamsContactsServerApplication;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link TeamController}.
 *
 */
@SpringBootTest(classes = { TeamsContactsServerApplication.class })
@AutoConfigureMockMvc
public class TeamControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private TeamService teamService;

	@Test
	void givenTeams_whenGetTeams_thenReturnJsonArray() throws Exception {
		Team team1 = new Team();
		team1.setId(1L);
		team1.setName("team1");

		Team team2 = new Team();
		team2.setId(2L);
		team2.setName("team2");

		List<Team> teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);

		when(teamService.findAllTeams()).thenReturn(teams);
		
		mvc.perform(get("/teams")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.teams", hasSize(2)))
				.andExpect(jsonPath("$._embedded.teams[0].name", is("team1")))
				.andExpect(jsonPath("$._embedded.teams[1].name", is("team2")));
		verify(teamService, times(1)).findAllTeams();
	}

	@Test
	void givenTeam_whenUpdate_thenReturnUpdatedTeam() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		TeamDto teamDto = new TeamDto();
		teamDto.setName("teamUpdated");

		Team teamUpdated = new Team();
		teamUpdated.setId(1L);
		teamUpdated.setName("teamUpdated");

		when(teamService.updateTeam(1L, teamDto)).thenReturn(Optional.of(teamUpdated));

		mvc.perform(put("/teams/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(teamDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("teamUpdated")));
		verify(teamService, times(1)).updateTeam(1L, teamDto);
	}
}
