package com.cc.teams.contacts.server.dto;

import org.springframework.hateoas.server.core.Relation;

import com.cc.teams.contacts.server.model.Team;

import lombok.Data;

/**
 * Data Transfer Object class for {@link Team}s.
 *
 */
@Data
@Relation(collectionRelation = "teams")
public class TeamDto {
	private Long id;
	private String name;
}
