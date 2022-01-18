function openMenu() {
  document.getElementById("sidePanel").style.left = "0";
}

function closeMenu() {
  document.getElementById("sidePanel").style.left = "-12.5em";
}

function openOrganizeTournaments(){
	document.getElementById("organizeTournamentsBackside").style.left = "0";
}

function openManageTeams(){
	document.getElementById("manageTeamsBackside").style.left = "0";
}

function closeBack() {
	document.getElementById("organizeTournamentsBackside").style.left = "-12.5em";
	document.getElementById("manageTeamsBackside").style.left = "-12.5em";
}

function openCreateTeamSidebar(){
  closeMenu();
  closeBack();
  document.getElementsByClassName("side-panel")[0].style.right="0";
}

function openPopup(id) {
  var popup = document.getElementById(id);
  popup.classList.toggle("show");
}

function closeCreateTeam(){
  $("#nickname").val("");
  $("#img-logo").attr("src","");
  document.getElementsByClassName("side-panel")[0].style.right="-30em";
  openManageTeams();
  openMenu();
}

function storeTeam() {
	$(document).ready(function(){
		let newTeam = new Team($("#nickname").val(), $("#img-logo").attr("src"));
		$.ajax({
			type: "POST",
			url: "/createTeam",
			contentType: "application/json",
			data: JSON.stringify(newTeam),
			success: function(answer){
				switch(answer.responseCode){
						case 501:
							openPopup("nameTakenPopup");
							console.log(answer.responseMessage);
							break;
						case 502:
							openPopup("teamNameNotFilledPopup");
							console.log(answer.responseMessage);
							break;
						case 503:
							openPopup("constraintsNotSatisfiedPopup");
							console.log(answer.responseMessage);
							break;
						default:
							console.log("TEAM CREATED");
							location.reload(true);
							break;
					};
			},
			error: function(err){
				console.log("CREATE TEAM ERROR");
				console.log(err);
			}
		});
	});
}

function getImg(input) {
	let file = input.files[0];
	let reader = new FileReader();
	if(file){
		reader.readAsDataURL(file);
		reader.onload = function() {
			imgDataURL = reader.result;
			$("#img-logo").attr("src",imgDataURL);
		};
	}
}

function openLoginSignup(choice){
  	closeCreateTeam();
  	closeMenu();
  	closeBack();
  	array = document.body.getElementsByTagName("*");
  	let len = array.length;
  	for (var i = 0; i < len; i++){
    	if(!array[i].classList.contains("do-not-hide")){
        	array[i].classList.add("semi-transparent");
    	}
  	}
  	document.getElementsByClassName(choice)[0].classList.add("show-login-signup");
};

function closeLoginSignup(choice){
  	let array = document.body.getElementsByTagName("*");
  	for (let i = 0, len = array.length; i < len; i++){
    	if(!array[i].classList.contains("do-not-hide")){
        	array[i].classList.remove("semi-transparent");
    	}
  	}
	array = document.body.getElementsByClassName("clear-" + choice);
	for (let i = 0, len = array.length; i < len; i++){
    	array[i].value="";
  	}
  	document.getElementsByClassName(choice)[0].classList.remove("show-login-signup");
}

function login(){
	let user = new UserLogIn($("#loginUsername").val(),$("#loginPassword").val());
	console.log(user)
	$.ajax({
		type: "POST",
		url: "/login",
		contentType: "application/json",
		data: JSON.stringify(user),
		success: function(answer){
			if(answer.responseCode==200){
				location.reload(true);
			}
			else{
				openPopup("invalidUsernamePassword");
			}
			console.log(answer.responseMessage);
		},
		error: function(err){
				console.log("CREATE TEAM ERROR");
				console.log(err);
			}
	});
}

function signup(){
	if($("#signupPassword").val() !== $("#confirmPassword").val()){
		console.log("Passwords must be the same!");
		return;
	}
	let user = new User($("#signupUsername").val(),$("#firstname").val(),$("#lastname").val(),$("#email").val(),$("#signupPassword").val());
	console.log(user)
	$.ajax({
		type: "POST",
		url: "/signup",
		contentType: "application/json",
		data: JSON.stringify(user),
		success: function(answer){
			switch(answer.responseCode){
				case 501:
					console.log("Invali email!");
					break;
				case 502:
					console.log("Email already exists!");
					break;
				case 503:
					console.log("Username already exists!");
					break;
				default :
					location.reload(true);
					break;
			}
			console.log(answer.responseMessage);
		},
		error: function(err){
				console.log("CREATE TEAM ERROR");
				console.log(err);
			}
	});
}