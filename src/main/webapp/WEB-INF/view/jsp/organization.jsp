<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/organization.css">
<script src="js/organization.js"></script>
</head>
<body>
    <jsp:include page="background.jsp"></jsp:include>

    <ul class="tabs">
        <li id="homeTab" class="active tab" onclick="setActive('homeTab')">HOME</li>
        <li id="tournamentsTab" class="tab" onclick="setActive('tournamentsTab')">TOURNAMENTS</li>
        <li id="staffTab" class="tab" onclick="setActive('staffTab')">STAFF</li>
    </ul>

    <div class="tabs-contents do-not-hide-create-tournament">

	    <div id="homeContent" class="tab-content active">
	      <img id="organizationBanner" src="${organization.banner}" alt="Organization Logo">
	      <h1>${organization.organizationName}</h1>
	      <div id="org-info">
	          <div id="org-description">
	              <textarea id="organizationDescription" readonly>${organization.description}</textarea>
	          </div>
	          <button id="submitOrganizationDescription" onclick="closeOrganizationDescriptionEditing()">Done</button>
	          <button id="editOrganizationDescription" onclick="editOrganizationDescription()">Edit</button>
	      </div>
	      <div class="delete-organization-button">
	      	<button onclick="deleteOrganization('${organization.organizationId}')">Delete Organization</button>
	      </div>
	    </div>

	    <div id="tournamentsContent" class="tab-content do-not-hide-create-tournament">
	      <h1>TOURNAMENTS</h1>
	      <table class="tournamentsTable">
	          <tr>
	          	<th>Category</th>
	            <th>Name</th>
	          </tr>

	          <jstl:forEach items="${tournamentsList}" var="tournament">
		          <tr id="${tournament.tournamentId}" onclick="selectTournament(this)">
		            <td>
			            <jstl:forEach items="${gamesList}" var="game">
			            	<jstl:if test="${game.name eq tournament.gameName}">
			            		<img src="${game.logo}">
			            	</jstl:if>
			            </jstl:forEach>
		            </td>
		            <td>${tournament.name}</td>
		          </tr>
	           </jstl:forEach>

	      </table>
		  <!-- Create Tournament Panel -->
	      <jsp:include page="createTournament.jsp"></jsp:include>

	      <div class="createTournamentBtn">
	      	<button id= "createTournament" onclick="openCreateTournament()">Create Tournament</button>
	      	<button id= "deleteTournament" onclick="deleteTournament()">Delete Tournament</button>
	      </div>
	      

	    </div>

	    <div id="staffContent" class="tab-content">
	      <h1>STAFF</h1>
	      
	      <jstl:if test="${organization.creatorId eq loggedManager.managerId}">
		      <input type="text" id="newMember">
		      <button id="addBtn" onclick="addMember()">Add Member</button>
		      <button id="removeBtn" onclick="removeMember()">Remove Member</button>
	      </jstl:if>
	      
	      <table class="staffTable">
	          <tr>
	            <th></th>
	            <th>Name</th>
	          </tr>

	          <jstl:forEach items="${staff}" var="member">
		          <tr>
		            <td><img src="${member.profilePicture}"></td>
		            <td>${member.username}</td>
		          </tr>
	           </jstl:forEach>

	      </table>

	    </div>
  	</div>
</body>
</html>
