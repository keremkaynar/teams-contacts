package com.cc.teams.contacts.server.service;

import java.util.List;
import java.util.Optional;

import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Team;

/**
 * Service class for managing {@link Team}s.
 *
 */
public interface TeamService {
	/**
	 * Creates a {@link Team} in db.
	 * 
	 * @param teamDto {@link TeamDto} to be used in creating a team
	 * @return created {@link Team}
	 */
	Team createTeam(TeamDto teamDto);

	/**
	 * Updates a {@link Team} in db, if exists.
	 * 
	 * @param teamDto {@link TeamDto} to be used in updating a team
	 * @param teamId  ID of the {@link Team} to be updated
	 * @return updated {@link Team} if already exists in db, otherwise
	 *         {@link Optional#empty()}
	 */
	Optional<Team> updateTeam(Long teamId, TeamDto teamDto);

	/**
	 * Deletes a {@link Team} in db.
	 * 
	 * @param teamId ID of the {@link Team} to be deleted
	 */
	void deleteTeam(Long teamId);

	/**
	 * Finds all {@link Team}s existing in db.
	 * 
	 * @return found {@link Team}s
	 */
	List<Team> findAllTeams();

	/**
	 * Finds {@link Team} by ID in db.
	 * 
	 * @param teamId ID of the {@link Team} to be found
	 * @return found {@link Team}s
	 */
	Optional<Team> findTeamById(Long teamId);
}
