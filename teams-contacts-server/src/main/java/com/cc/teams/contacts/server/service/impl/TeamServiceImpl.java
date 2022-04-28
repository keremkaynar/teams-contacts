package com.cc.teams.contacts.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cc.teams.contacts.server.dao.TeamRepository;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.TeamService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class TeamServiceImpl implements TeamService {
	private TeamRepository teamRepository;

	public TeamServiceImpl(TeamRepository teamRepository) {
		super();
		this.teamRepository = teamRepository;
	}

	@Override
	public Team createTeam(TeamDto teamDto) {
		Team team = new Team();
		team.setName(teamDto.getName());
		log.info("Created team in db: {}", team);
		return teamRepository.save(team);
	}

	@Override
	public Optional<Team> updateTeam(Long teamId, TeamDto teamDto) {
		if (teamId != null) {
			Team updatedTeam = teamRepository.findById(teamId).orElse(null);
			if (updatedTeam != null) {
				updatedTeam.setName(teamDto.getName());
				log.info("Updated team in db: {}", updatedTeam);
				return Optional.of(teamRepository.save(updatedTeam));
			}
		}
		return Optional.empty();
	}

	@Override
	public void deleteTeam(Long teamId) {
		if (teamId != null) {
			log.info("Deleted team with ID in db: {}", teamId);
			teamRepository.deleteById(teamId);
		}
	}

	@Override
	public List<Team> findAllTeams() {
		List<Team> teams = new ArrayList<>();
		Iterable<Team> teamsIterable = teamRepository.findAll();
		teamsIterable.forEach(teams::add);
		return teams;
	}

	@Override
	public Optional<Team> findTeamById(Long teamId) {
		if (teamId != null) {
			return teamRepository.findById(teamId);
		}
		return Optional.empty();
	}
}
