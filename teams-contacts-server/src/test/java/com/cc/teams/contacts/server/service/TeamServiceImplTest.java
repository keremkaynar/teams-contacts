package com.cc.teams.contacts.server.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cc.teams.contacts.server.dao.TeamRepository;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.impl.TeamServiceImpl;

/**
 * Test class for {@link TeamServiceImpl}.
 *
 */
@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private TeamServiceImpl teamService;

	@Test
	void findAllTeams_success() {
		// Prepare mocks
		Team team1 = new Team();
		team1.setId(1L);
		team1.setName("team1");

		Team team2 = new Team();
		team2.setId(2L);
		team2.setName("team2");

		List<Team> teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
		when(teamRepository.findAll()).thenReturn(teams);

		List<Team> foundTeams = teamService.findAllTeams();
		assertThat(foundTeams, hasSize(2));
		assertThat(foundTeams.get(0).getId(), is(1L));
		assertThat(foundTeams.get(0).getName(), is("team1"));
		assertThat(foundTeams.get(1).getId(), is(2L));
		assertThat(foundTeams.get(1).getName(), is("team2"));

		verify(teamRepository).findAll();
	}

	@Test
	void createTeam_success() {
		// Prepare mocks

		Team createdTeam = new Team();
		createdTeam.setName("team1");
		when(teamRepository.save(any(Team.class))).thenReturn(createdTeam);

		// Execute test method
		TeamDto teamDto = new TeamDto();
		teamDto.setName("team1");

		// Assertions
		Team team = teamService.createTeam(teamDto);
		assertThat(team.getName(), is("team1"));

		verify(teamRepository).save(createdTeam);
	}
}
