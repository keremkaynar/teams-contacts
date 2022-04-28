package com.cc.teams.contacts.server.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cc.teams.contacts.server.dto.ContactsResultDto;
import com.cc.teams.contacts.server.dto.TeamDto;
import com.cc.teams.contacts.server.dto.assemble.ModelToDtoConverter;
import com.cc.teams.contacts.server.dto.assemble.TeamDtoAssembler;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.model.Team;
import com.cc.teams.contacts.server.service.ContactService;
import com.cc.teams.contacts.server.service.TeamService;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:9090" })
public class TeamController {
	private TeamService teamService;

	private ContactService contactService;

	private TeamDtoAssembler teamDtoAssembler;

	private ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();

	public TeamController(TeamService teamService, ContactService contactService, TeamDtoAssembler teamDtoAssembler) {
		this.teamService = teamService;
		this.contactService = contactService;
		this.teamDtoAssembler = teamDtoAssembler;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<TeamDto>> createTeam(@RequestBody TeamDto teamDto) {
		return new ResponseEntity<>(
				teamDtoAssembler.toModel(modelToDtoConverter.convertFrom(teamService.createTeam(teamDto))),
				HttpStatus.CREATED);
	}

	@PutMapping(path = "/{teamId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<TeamDto>> updateTeam(@PathVariable Long teamId, @RequestBody TeamDto teamDto) {
		Team updatedTeam = teamService.updateTeam(teamId, teamDto).orElse(null);
		if (updatedTeam != null) {
			return new ResponseEntity<>(teamDtoAssembler.toModel(modelToDtoConverter.convertFrom(updatedTeam)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/{teamId}/contacts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ContactsResultDto> findPagedContacts(@PathVariable Long teamId,
			@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
		Page<Contact> pagedContacts = contactService.findPagedContactsByTeamId(teamId, pageIndex, pageSize);
		ContactsResultDto contactsResultDto = new ContactsResultDto();
		contactsResultDto.setContacts(pagedContacts.getContent().stream()
				.map(contact -> modelToDtoConverter.convertFrom(contact)).collect(Collectors.toList()));
		contactsResultDto.setTotalCount(pagedContacts.getTotalElements());
		return new ResponseEntity<>(contactsResultDto, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{teamId}")
	public ResponseEntity<TeamDto> deleteTeam(@PathVariable Long teamId) {
		teamService.deleteTeam(teamId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(path = "/{teamId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<TeamDto>> findTeam(@PathVariable Long teamId) {
		EntityModel<TeamDto> teamDto = teamService.findTeamById(teamId)
				.map(team -> modelToDtoConverter.convertFrom(team))
				.map(teamDtoAssembler::toModel)
				.orElse(null);
		if (teamDto != null) {
			return new ResponseEntity<>(teamDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> findAllTeams() {
		List<EntityModel<TeamDto>> teamDtos = teamService.findAllTeams().stream()
				.map(team -> modelToDtoConverter.convertFrom(team))
				.map(teamDtoAssembler::toModel)
				.collect(Collectors.toList());
		return new ResponseEntity<>(CollectionModel.of(teamDtos,
				linkTo(methodOn(TeamController.class).findAllTeams()).withSelfRel()), HttpStatus.OK);
	}
}
