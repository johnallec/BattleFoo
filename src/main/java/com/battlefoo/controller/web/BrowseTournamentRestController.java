package com.battlefoo.controller.web;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.battlefoo.model.entitiesObjects.Tournament;
import com.battlefoo.persistence.dbManagement.Database;

@RestController
public class BrowseTournamentRestController {
	@PostMapping("/filterTournament")
	public String[][] getTournamentListFiltered(HttpServletRequest req, @RequestBody String filter) {
		String[][] tournamentFilter = Database.getInstance().getTournamentByName(filter.replace("\"", "%"));
		return tournamentFilter;
		
	}


}