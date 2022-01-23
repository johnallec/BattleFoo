<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/teamPage.css">
<script type="text/javascript" src="js/teamPage.js"></script>
</head>
<body>
<jsp:include page="background.jsp"></jsp:include>
<jstl:set value="${team}" var="team"/>
	<div class="out-tab">
      <div class="out-tab-logo-name">
        <img id="team-logo" src="${team.logo}" alt="">
        <label for="team-logo">${team.teamName}</label>
      </div>
      <div class="out-tab-btn">
        <label for="edit"><img src="images/sidebar-icons/editIcon.png" title="Edit Team"></label>
        <button class="keep-away" id="edit"></button>
      </div>
    </div>

    <div class="tabs">
      <!-- Tab links -->
      <div class="tab">
        <label class="tablinks first-tab" for="description" onclick="openTab(event, 'description-content')">Description</label>
        <label class="tablinks second-tab" for="attendees"  onclick="openTab(event, 'list-attendees')">Attendees</label>
        <button class="keep-away"></button>
        <button class="keep-away"></button>
      </div>

      <!-- Tab content -->
      <div id="description-content" class="tabcontent">
      	<h3 contenteditable="true">${team.description}</h3>
      </div>

      <div id="list-attendees" class="tabcontent">
      	<jstl:forEach items="${teamMembersList}" var="player">
		<div class="attendee tooltip">
		  <img id="${player.username}" src="${player.profilePicture}">
		  <span class="tooltiptext">${player.username}</span>
		</div>
		</jstl:forEach>
      </div>
    </div>
</body>
</html>