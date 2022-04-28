package com.cc.teams.contacts.server.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the team business concept.
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Team {
	@Id
	@SequenceGenerator(name = "team_seq", sequenceName = "team_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "team_seq")
	private Long id;

	@NotBlank(message = "Name of a team must be non-blank!")
	private String name;

	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Contact> contacts;

	@Override
	public String toString() {
		return "Team with id: " + id + " name: " + name + " # of contacts: "
				+ (contacts == null ? "0" : String.valueOf(contacts.size()));
	}
}
