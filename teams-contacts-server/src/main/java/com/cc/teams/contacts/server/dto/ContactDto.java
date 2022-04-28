package com.cc.teams.contacts.server.dto;

import org.springframework.hateoas.server.core.Relation;

import com.cc.teams.contacts.server.model.Contact;

import lombok.Data;

/**
 * Data Transfer Object class for {@link Contact}s.
 *
 */
@Data
@Relation(collectionRelation = "contacts")
public class ContactDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String mailAddress;
	private Long teamId;
}
