package com.cc.teams.contacts.server.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cc.teams.contacts.server.dto.ContactDto;
import com.cc.teams.contacts.server.dto.assemble.ContactDtoAssembler;
import com.cc.teams.contacts.server.dto.assemble.ModelToDtoConverter;
import com.cc.teams.contacts.server.model.Contact;
import com.cc.teams.contacts.server.service.ContactService;

@RestController
@RequestMapping("/contacts")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:9090" })
public class ContactController {
	private ContactService contactService;

	private ContactDtoAssembler contactDtoAssembler;

	private ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();

	public ContactController(ContactService contactService, ContactDtoAssembler contactDtoAssembler) {
		this.contactService = contactService;
		this.contactDtoAssembler = contactDtoAssembler;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<ContactDto>> createContact(@RequestBody ContactDto contactDto) {
		return new ResponseEntity<>(
				contactDtoAssembler.toModel(modelToDtoConverter.convertFrom(contactService.createContact(contactDto))),
				HttpStatus.CREATED);
	}

	@PutMapping(path = "/{contactId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<ContactDto>> updateContact(@PathVariable Long contactId,
			@RequestBody ContactDto contactDto) {
		Contact updatedContact = contactService.updateContact(contactId, contactDto).orElse(null);
		if (updatedContact != null) {
			return new ResponseEntity<>(contactDtoAssembler.toModel(modelToDtoConverter.convertFrom(updatedContact)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PatchMapping(path = "/{contactId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<ContactDto>> updateContactPartial(@PathVariable Long contactId,
			@RequestBody ContactDto contactDto) {
		Contact updatedContact = contactService.updateContactPartial(contactId, contactDto).orElse(null);
		if (updatedContact != null) {
			return new ResponseEntity<>(contactDtoAssembler.toModel(modelToDtoConverter.convertFrom(updatedContact)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(path = "/{contactId}")
	public ResponseEntity<ContactDto> deleteContact(@PathVariable Long contactId) {
		contactService.deleteContact(contactId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(path = "/{contactId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EntityModel<ContactDto>> findContact(@PathVariable Long contactId) {
		EntityModel<ContactDto> contactDto = contactService.findContactById(contactId)
				.map(contact -> modelToDtoConverter.convertFrom(contact))
				.map(contactDtoAssembler::toModel)
				.orElse(null);
		if (contactDto != null) {
			return new ResponseEntity<>(contactDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> findAllContacts() {
		List<EntityModel<ContactDto>> contactDtos = contactService.findAllContacts().stream()
				.map(contact -> modelToDtoConverter.convertFrom(contact))
				.map(contactDtoAssembler::toModel)
				.collect(Collectors.toList());
		return new ResponseEntity<>(CollectionModel.of(contactDtos,
				linkTo(methodOn(ContactController.class).findAllContacts()).withSelfRel()), HttpStatus.OK);
	}
}
