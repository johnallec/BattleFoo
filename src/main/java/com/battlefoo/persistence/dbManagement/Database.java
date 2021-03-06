package com.battlefoo.persistence.dbManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.battlefoo.DatabaseNames;
import com.battlefoo.model.entitiesObjects.*;
import com.battlefoo.persistence.jdbc.GamesDAO;
import com.battlefoo.persistence.jdbc.ManagersDAO;
import com.battlefoo.persistence.jdbc.MatchesDAO;
import com.battlefoo.persistence.jdbc.OrganizationsDAO;
import com.battlefoo.persistence.jdbc.PlayersDAO;
import com.battlefoo.persistence.jdbc.TeamsDAO;
import com.battlefoo.persistence.jdbc.TournamentsDAO;
import com.battlefoo.persistence.jdbc.UsersDAO;

public class Database {
	
	private Connection connection;
	private static Database instance = null;
	
	private Database() {
		try {
			connection = DriverManager.getConnection(DatabaseNames.URL_POSTGRES, "postgres", "postgres");
			System.out.println("Connected to postgres");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		if(instance == null)
			instance = new Database();
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	// ************************************* GAMES
	public List<Game> getAllGames(){
		return GamesDAO.getInstance(connection).getAll();
	}
	
	public Game getGameByName(String name) {
		return GamesDAO.getInstance(connection).getByName(name);
	}
	
	public boolean gameExists(String name) {
		return GamesDAO.getInstance(connection).exists(name);
	}
	
	public List<Game> getGamesByGenre(String genre){
		return GamesDAO.getInstance(connection).getGamesByGenre(genre);
	}
	
	// ************************************* PLAYERS
	public List<Player> getAllPlayers(){
		return PlayersDAO.getInstance(connection).getAll();
	}
	
	public Player getPlayerByUsername(String username) {
		return PlayersDAO.getInstance(connection).getByUsername(username);
	}
	
	public boolean playerExists(String name) {
		return PlayersDAO.getInstance(connection).exists(name);
	}
	
	// ************************************* MANAGERS
	public List<Manager> getAllManagers(){
		return ManagersDAO.getInstance(connection).getAll();
	}
	
	public Manager getManagerByUsername(String username) {
		return ManagersDAO.getInstance(connection).getByUsername(username);
	}
	
	public boolean managerExists(String username) {
		return ManagersDAO.getInstance(connection).exists(username);
	}
	
	// ************************************* Teams
	public List<Team> getAllTeams(){
		return TeamsDAO.getInstance(connection).getAll();
	}
	
	public Team getTeamByTeamName(String teamName) {
		return TeamsDAO.getInstance(connection).getByTeamName(teamName);
	}
	
	public boolean teamExists(String teamName) {
		return TeamsDAO.getInstance(connection).exists(teamName);
	}
	
	// ************************************* Tournaments
	public List<Tournament> getAllTournaments(){
		return TournamentsDAO.getInstance(connection).getAll();
	}
	
	public Tournament getCreatorByTournamentName(String username) {
		return TournamentsDAO.getInstance(connection).getByCreatorUsername(username);
	}
	
	public boolean tournamentExists(String tournamentName) {
		return TournamentsDAO.getInstance(connection).exists(tournamentName);
	}

	public boolean insertTeam(Team team) {
		return TeamsDAO.getInstance(connection).insert(team);
	}
	
	// ************************************* Users
	
	public boolean insertUser(User user) {
		return UsersDAO.getInstance(connection).insertUser(user);
	}
	
	// ************************************* Auth
	public boolean allowLogIn(String username, String password) {
		return PlayersDAO.getInstance(connection).logUser(username, password);
	}

	public List<Player> getAllTeamMembers(String teamName) {
		return TeamsDAO.getInstance(connection).getTeamMembers(teamName);
	}

	public boolean insertOrganizationMember(String creatorUsername, String organizationName, String membersUsername) {
		Manager creator = ManagersDAO.getInstance(connection).getByUsername(creatorUsername);
		Organization currentOrg = null;
		for(Organization o : OrganizationsDAO.getInstance(connection).getAllByCreatorId(creator.getManagerId()))
			if(o.getOrganizationName().compareTo(organizationName)==0)
				currentOrg = o;
		if(currentOrg == null)
			return false;
		Manager newMember = ManagersDAO.getInstance(connection).getByUsername(membersUsername);
		return OrganizationsDAO.getInstance(connection).insertMember(creator, currentOrg, newMember);
	}

	public List<Team> getTeamsByPlayerUsername(String username) {
		Player player = PlayersDAO.getInstance(connection).getByUsername(username);
		return TeamsDAO.getInstance(connection).getAllByPlayerId(player.getPlayerId());
	}

	public Manager getManagerById(Long creatorId) {
		return ManagersDAO.getInstance(connection).getById(creatorId);
	}
	
	public List<Manager> getOrganizationMembersById(Long id) {
		return OrganizationsDAO.getInstance(connection).getMembersByOrganizationId(id);
	}
	
	public List<Manager> getOrganizationMembersByOrgName(String orgName) {
		return OrganizationsDAO.getInstance(connection).getMembersByOrgName(orgName);
	}

	public List<Organization> getMyOrganizations(String username) {
		Manager manager = Database.getInstance().getManagerByUsername(username);
		return OrganizationsDAO.getInstance(connection).getAllByManagerId(manager.getManagerId());
	}

	public boolean insertManager(String username) {
		return ManagersDAO.getInstance(connection).insertManager(username);
	}

	public boolean managerAlreadyMemberOfTheStaff(Organization organization, String membersUsername) {
		return ManagersDAO.getInstance(connection).isMemberOf(organization, membersUsername);
	}

	public boolean insertTeamMember(Team team, String newTeamMember) {
		Player p = PlayersDAO.getInstance(connection).getByUsername(newTeamMember);
		return PlayersDAO.getInstance(connection).insertIntoTeam(team, p);
	}

	public boolean removeTeamMember(Team team, String newTeamMember) {
		Player p = PlayersDAO.getInstance(connection).getByUsername(newTeamMember);
		return PlayersDAO.getInstance(connection).removeFromTeam(team, p);
	}

	public boolean playerIsMember(Team team, String newTeamMember) {
		List<Player> members = TeamsDAO.getInstance(connection).getTeamMembers(team.getTeamName());
		for(Player p : members)
			if(p.getUsername().compareTo(newTeamMember) == 0)
				return false;
		return true;
	}

	public boolean insertTournament(Tournament tournament, Long organizationId) {
		return TournamentsDAO.getInstance(connection).insertTournament(tournament, organizationId);
	}

	public boolean editTeamDescription(Team team, String description) {
		return TeamsDAO.getInstance(connection).editDescription(team,description);
	}

	public List<Match> getAllMatchesByTournamentId(Long tournamentId) {
		return MatchesDAO.getInstance(connection).getAllByTournamentId(tournamentId);
	}

	public String[][] getTeamsByPhase(int numeroPartite, Long tournamentId) {
		return MatchesDAO.getInstance(connection).getTeamsByPhase(numeroPartite, tournamentId);
	}

	public Organization getOrganizationById(Long orgId, Long creatorId) {
		return OrganizationsDAO.getInstance(connection).getById(orgId, creatorId);
	}

	public boolean insertOrganization(Organization org) {
		return OrganizationsDAO.getInstance(connection).insertOrganization(org);
	}

	public List<Tournament> getTournamentsByOrganizationId(Long organizationId) {
		return TournamentsDAO.getInstance(connection).getByOrganizationId(organizationId);
	}

	public Tournament getTournamentByTournamentId(Long tournamentId) {
		return TournamentsDAO.getInstance(connection).getById(tournamentId);
	}

	public List<Team> getTournamentAttendeesByTournamentId(Long tournamentId) {
		List<String> attendeesNames = TournamentsDAO.getInstance(connection).getAttendeesById(tournamentId);
		List<Team> attendees = null;
		if(attendeesNames != null) {
			attendees = new ArrayList<Team>();
			for(String teamName : attendeesNames) {
				attendees.add(TeamsDAO.getInstance(connection).getByTeamName(teamName));
			}
		}
		return attendees;
	}

	public List<Team> getTournamentAttendeesByTournamentName(String name) {
		List<String> attendeesNames = TournamentsDAO.getInstance(connection).getAttendeesByName(name);
		List<Team> attendees = null;
		if(attendeesNames != null) {
			attendees = new ArrayList<Team>();
			for(String teamName : attendeesNames) {
				attendees.add(TeamsDAO.getInstance(connection).getByTeamName(teamName));
			}
		}
		return attendees;
	}

	public Organization getOrganizationByOrganizationName(String organizationName, Long creatorId) {
		return OrganizationsDAO.getInstance(connection).getByName(organizationName, creatorId);
	}

	public boolean insertTeamIntoTournament(Team team, Tournament tournament) {
		return TournamentsDAO.getInstance(connection).insertTeam(team,tournament);
	}

	public boolean removeStaffMember(Organization org, String membersUsername) {
		Manager m = ManagersDAO.getInstance(connection).getByUsername(membersUsername);
		return OrganizationsDAO.getInstance(connection).removeMember(org, m);
	}

	public String[][] getGamesListByFilter(String filter) {
		List<Game> gamesFiltered = GamesDAO.getInstance(connection).getListByFilter(filter);
		String[][] gamesArray = new String[gamesFiltered.size()][];
		
		for(int i = 0; i < gamesArray.length; ++i) { 
			gamesArray[i] = new String[3];
			gamesArray[i][0] = gamesFiltered.get(i).getLogo();
			gamesArray[i][1] = gamesFiltered.get(i).getName();
			gamesArray[i][2] = gamesFiltered.get(i).getGenre();
		}
		return gamesArray;	
	}

	public String[][] getTournamentByName(String filter) {
		List<Tournament> tournamentFiltered = TournamentsDAO.getInstance(connection).getListByFilter(filter);
		List<Game> gameList = GamesDAO.getInstance(connection).getAll();
		String[][] tournamentArray = new String[tournamentFiltered.size()][];
		
	
		for (int i = 0; i < tournamentArray.length;i++) {
			tournamentArray[i] = new String[4];
			for (int j = 0; j<gameList.size();j++)	{
				if( tournamentFiltered.get(i).getGameName().equals(gameList.get(j).getName())) {
					tournamentArray[i][0] = gameList.get(j).getLogo();
					break;
				}
			}
			tournamentArray[i][1] = tournamentFiltered.get(i).getName();
			tournamentArray[i][2] = tournamentFiltered.get(i).getDate().getDate();
			tournamentArray[i][3] = String.valueOf(tournamentFiltered.get(i).getTournamentId());
		}
		return tournamentArray;
	}
	
	public String getChatHistory(Long matchId) {
		return MatchesDAO.getInstance(connection).getChatByMatchId(matchId);
	}

	public boolean setChatHistory(String chatHistory, Long matchId) {
		return MatchesDAO.getInstance(connection).setChatHistoryByMatchId(chatHistory,matchId);
	}

	public boolean deleteTournamentById(Long tournamentId) {
		return TournamentsDAO.getInstance(connection).deleteTournamentById(tournamentId);
	}

	public boolean leaveTournamentByTeamName(Team team, Tournament tournament) {
		return TournamentsDAO.getInstance(connection).deleteTeamFromTournament(team, tournament);
	}

	public boolean deleteOrganizationById(Long orgId) {
		return OrganizationsDAO.getInstance(connection).deleteById(orgId);
	}

	public boolean editOrganizationDescription(Organization currentOrganization, String description) {
		return OrganizationsDAO.getInstance(connection).editDescription(currentOrganization, description);
	}

	public boolean setTwitchChannel(Tournament t, String twitchChannel) {
		return TournamentsDAO.getInstance(connection).setTwitchChannel(t,twitchChannel);
	}

	public List<Manager> getMatchStaff(Long tournamentId) {
		List<Long> managersId = TournamentsDAO.getInstance(connection).getTournamentStaff(tournamentId);
		if(managersId != null) {
			List<Manager> tournamentStaff = new ArrayList<Manager>();
			for(Long id : managersId) {
				tournamentStaff.add(ManagersDAO.getInstance(connection).getById(id));
			}
			return tournamentStaff;
		}
		return null;
	}

	public Match getMatchById(Long matchId) {
		return MatchesDAO.getInstance(connection).getMatchById(matchId);
	}

	public List<Player> getMatchLongAttendeesByMatchId(Long matchId) {
		List<Long> matchLongsAttendees = MatchesDAO.getInstance(connection).getMatchLongAttendeesByMatchId(matchId);
		List<Player> matchAttendees = new ArrayList<Player>();
		for(Long id : matchLongsAttendees)
			matchAttendees.add(Database.getInstance().getPlayerById(id));
		return matchAttendees;
	}

	private Player getPlayerById(Long id) {
		List<Player> allPlayers = PlayersDAO.getInstance(connection).getAll();
		for(Player p : allPlayers)
			if(p.getPlayerId() == id)
				return p;
		return null;
	}

	public boolean insertMatch(Match m) {
		return MatchesDAO.getInstance(connection).insertMatch(m);
	}
}
