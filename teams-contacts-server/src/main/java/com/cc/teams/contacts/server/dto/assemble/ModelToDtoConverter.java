package com.cc.teams.contacts.server.dto.assemble;

import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;

/**
 * Converter from model objects to DTOs.
 *
 */
public class ModelToDtoConverter {
	public TeamDto convertFrom(Team team) {
		TeamDto teamDto = new TeamDto();
		teamDto.setId(team.getId());
		teamDto.setName(team.getName());
		return teamDto;
	}

	public ContactDto convertFrom(Contact contact) {
		ContactDto contactDto = new ContactDto();
		contactDto.setId(contact.getId());
		contactDto.setFirstName(contact.getFirstName());
		contactDto.setLastName(contact.getLastName());
		contactDto.setMailAddress(contact.getMailAddress());
		if (contact.getTeam() != null) {
			contactDto.setTeamId(contact.getTeam().getId());
		}
		return contactDto;
	}
}
