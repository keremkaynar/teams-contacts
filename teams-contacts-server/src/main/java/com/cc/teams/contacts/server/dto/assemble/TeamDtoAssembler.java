package com.cc.teams.contacts.server.dto.assemble;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.cc.teams.contacts.server.controller.TeamController;
import com.cc.teams.contacts.server.dto.TeamDto;

/**
 * Responsible for adding hypermedia links to {@link TeamDto} objects.
 *
 */
@Component
public class TeamDtoAssembler implements RepresentationModelAssembler<TeamDto, EntityModel<TeamDto>> {

	@Override
	public EntityModel<TeamDto> toModel(TeamDto team) {
		return EntityModel.of(team,
				linkTo(methodOn(TeamController.class).findTeam(team.getId())).withSelfRel(),
				linkTo(methodOn(TeamController.class).findAllTeams()).withRel("teams"));
	}
}
