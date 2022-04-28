package com.cc.teams.contacts.server.dto.assemble;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.cc.teams.contacts.server.controller.ContactController;
import com.cc.teams.contacts.server.dto.ContactDto;

/**
 * Responsible for adding hypermedia links to {@link ContactDto} objects.
 *
 */
@Component
public class ContactDtoAssembler implements RepresentationModelAssembler<ContactDto, EntityModel<ContactDto>> {

	@Override
	public EntityModel<ContactDto> toModel(ContactDto contact) {
		return EntityModel.of(contact,
				linkTo(methodOn(ContactController.class).findContact(contact.getId())).withSelfRel(),
				linkTo(methodOn(ContactController.class).findAllContacts()).withRel("contacts"));
	}
}
