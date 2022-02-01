package com.battlefoo.controller.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.battlefoo.ServerPaths;
import com.battlefoo.model.Response;
import com.battlefoo.model.entitiesObjects.Manager;
import com.battlefoo.model.entitiesObjects.Organization;
import com.battlefoo.model.entitiesObjects.Team;
import com.battlefoo.model.entitiesObjects.Tournament;
import com.battlefoo.persistence.dbManagement.Database;

@RestController
public class TournamentRestController {

	@PostMapping("/createTournament")
	public Response createTournament(HttpServletRequest req, @RequestBody Tournament tournament) {
		Response response = createCreateTournamentResponse(req, tournament);
		return response;
	}

	private Response createCreateTournamentResponse(HttpServletRequest req, Tournament tournament) {
		Response res = new Response(Response.failure, "Storing tournament failed!");
		Manager creator = (Manager)req.getSession(true).getAttribute("loggedManager");
		
		if(creator != null) {
			if(Database.getInstance().tournamentExists(tournament.getName())) {
				tournament.setManagerId(creator.getManagerId());
				//set tournament logo and sponsor banner
				if(tournament.getLogo() != null) {
					BufferedWriter writer;
					String imgFileName = new Date().getTime() + ".txt";
					try {
						writer = new BufferedWriter(new FileWriter(new File(ServerPaths.TOURNAMENTS_LOGOS_PATH, imgFileName)));
						writer.write(tournament.getLogo());
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					req.getSession(true).setAttribute("tournament", tournament);
					
					// setting the team logo as the path to the text file instead
					tournament.setLogo(ServerPaths.TOURNAMENTS_LOGOS_PATH + imgFileName);
				}
				else {
					tournament.setLogo(ServerPaths.DEFAULT_ORGANIZATION_BANNER);
					req.getSession(true).setAttribute("tournament", tournament);
				}
				
				if(tournament.getSponsor() != null) {
					BufferedWriter writer;
					String imgFileName = new Date().getTime() + ".txt";
					try {
						writer = new BufferedWriter(new FileWriter(new File(ServerPaths.TOURNAMENTS_SPONSORS_PATH, imgFileName)));
						writer.write(tournament.getSponsor());
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					req.getSession(true).setAttribute("tournament", tournament);
					
					// setting the team logo as the path to the text file instead
					tournament.setSponsor(ServerPaths.TOURNAMENTS_SPONSORS_PATH + imgFileName);
				}
				else {
					tournament.setSponsor(ServerPaths.DEFAULT_ORGANIZATION_BANNER);
					req.getSession(true).setAttribute("tournament", tournament);
				}
				
				if(Database.getInstance().insertTournament(tournament,((Organization)req.getSession(true).getAttribute("organization")).getOrganizationId() )) {
					List<Team> tournamentAttendees = Database.getInstance().getTournamentAttendeesByTournamentName(tournament.getName());
					req.getSession(true).setAttribute("tournamentAttendees", tournamentAttendees);
					res.setResponseCode(200);
					res.setResponseMessage("Tournament stored");
				}
			}
			else {
				res.setResponseMessage("Tournament Name already exists!");
			}
		}
		else{
			System.out.println("CREATORI ID IS NULL TOURNAMENT REST CONTROLLER CREATE TOURNAMENT");
		}
		return res;
		
	}
	
	@PostMapping("/tournamentPage")
	public Response getTournamentPage(HttpServletRequest req, @RequestBody Long tournamentId) {
		return createResponseTournamentPage(req,tournamentId);
	}

	private Response createResponseTournamentPage(HttpServletRequest req, Long tournamentId) {
		Response res = new Response(Response.failure, "Getting tournament page failed!");
		Tournament t = Database.getInstance().getTournamentByTournamentId(tournamentId);
		if(t!=null) {
			// SET TOURNAMENT BANNER AND SPONSOR BANNER

			if(t.getLogo() != null) {
				try {
					BufferedReader br =  new BufferedReader(new FileReader(t.getLogo()));
					t.setLogo(br.readLine());
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				t.setLogo(ServerPaths.DEFAULT_ORGANIZATION_BANNER);
			
			//*******************************************************************************************************
			List<Team> tournamentAttendees = Database.getInstance().getTournamentAttendeesByTournamentId(tournamentId);
			
			for(Team team : tournamentAttendees) {
				if(team.getLogo() != null) {
					try {
						BufferedReader br =  new BufferedReader(new FileReader(team.getLogo()));
						team.setLogo(br.readLine());
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
					team.setLogo(ServerPaths.DEFAULT_TEAM_LOGO);
			}
			
			req.getSession(true).setAttribute("tournamentAttendees", tournamentAttendees);
			req.getSession(true).setAttribute("tournament", t);
			res.setResponseCode(200);
			res.setResponseMessage("Tournament page has been returned");
		}
		return res;
	}
	
	@PostMapping("/signupTeam")
	public Response signupTeam(HttpServletRequest req, @RequestBody String teamName) {
		Team team = Database.getInstance().getTeamByTeamName(teamName.replace("\"", ""));
		Tournament tournament = (Tournament)req.getSession(true).getAttribute("tournament");
		return createSignupTeamResponse(req,team,tournament);
	}

	private Response createSignupTeamResponse(HttpServletRequest req, Team team, Tournament tournament) {
		Response res = new Response(Response.failure, "Signing up team failed!");
		List<Team> attendees = Database.getInstance().getTournamentAttendeesByTournamentId(tournament.getTournamentId());
		
		if(tournament.getNumOfAttendees() == attendees.size()) {
			res.setResponseMessage("Tournament is full!");
			return res;
		}
		
		for(Team t : attendees) {
			if(t.getTeamName().compareTo(team.getTeamName()) == 0) {
				res.setResponseMessage("Team already signed!");
				return res;
			}
		}
		
		if(Database.getInstance().insertTeamIntoTournament(team,tournament)) {
			res.setResponseCode(200);
			res.setResponseMessage("Team signed");
		}
		return res;
	}
	
}
