package com.battlefoo.model.entitiesObjects;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Tournament {
	
	@NonNull
	private Long tournamentId;
	
	@NonNull
	private String name;
	
	@NonNull
	private Date date;
	
	private String description;
	private String rules;
	private String schedule;
	
	@NonNull
	private String gameName;
	
	private String sponsor;
	
	@NonNull
	private String logo;
	
	@NonNull
	private Long managerId;
	
	@NonNull
	private Integer numOfAttendees;
	
	private String twitchChannel;
	
	public Tournament(String name, Date date, String description, String rules, String schedule,
					String gameName, String sponsor, String logo, Integer numOfAttendees) {
		this.name = name;
		this.date = date;
		this.description = description;
		this.rules = rules;
		this.schedule = schedule;
		this.gameName = gameName;
		this.sponsor = sponsor;
		this.logo = logo;
		this.numOfAttendees = numOfAttendees;
	}
	
	public Tournament(String name, String date, String description, String rules, String schedule,
			String gameName, String sponsor, String logo, Integer numOfAttendees) {
		this.name = name;
		this.date = new Date(date);
		this.description = description;
		this.rules = rules;
		this.schedule = schedule;
		this.gameName = gameName;
		this.sponsor = sponsor;
		this.logo = logo;
		this.numOfAttendees = numOfAttendees;
	}
	
	public Tournament(String name, String date, String description, String rules, String schedule,
			String gameName, String sponsor, String logo, Long managerId, Long tournamentId, Integer numOfAttendees) {
		this.name = name;
		this.date = new Date(date);
		this.description = description;
		this.rules = rules;
		this.schedule = schedule;
		this.gameName = gameName;
		this.sponsor = sponsor;
		this.logo = logo;
		this.managerId = managerId;
		this.tournamentId = tournamentId;
		this.numOfAttendees = numOfAttendees;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tournamentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tournament other = (Tournament) obj;
		return Objects.equals(tournamentId, other.tournamentId);
	}
}
