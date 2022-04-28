package com.cc.teams.contacts.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a contact of a specific team.
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Contact {
	@Id
	@SequenceGenerator(name = "contact_seq", sequenceName = "contact_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "contact_seq")
	private Long id;

	private String firstName;
	private String lastName;

	@Email(message = "Provided string is not a valid email address!")
	private String mailAddress;

	@ManyToOne
	@JoinColumn(name = "team_id", referencedColumnName = "id")
	private Team team;

	@Override
	public String toString() {
		return "Contact with id: " + id + " firstName: " + firstName + " lastName: " + lastName + " mailAddress: "
				+ mailAddress + " teamId: " + (team == null ? "no team" : String.valueOf(team.getId()));
	}
}
