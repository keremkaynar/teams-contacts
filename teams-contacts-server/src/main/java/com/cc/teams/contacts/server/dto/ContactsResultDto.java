package com.cc.teams.contacts.server.dto;

import java.util.List;

import com.cc.teams.contacts.server.model.Contact;

import lombok.Data;

/**
 * Data Transfer Object class holding paged {@link Contact}s.
 *
 */
@Data
public class ContactsResultDto {
	private List<ContactDto> contacts;
	private Long totalCount;
}
