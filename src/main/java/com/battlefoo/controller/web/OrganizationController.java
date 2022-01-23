package com.battlefoo.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.battlefoo.model.CommonMethods;

@Controller
public class OrganizationController {

	@GetMapping("/organization")
	public String getOrganization(HttpServletRequest req) {
		if(req.getSession(true).getAttribute("loggedUser")==null)
			return "index";
		CommonMethods.updateTeamsAttribute(req, false);
		CommonMethods.updateOrganizationsAttribute(req, false);
		return "organization";
	}
	
	@GetMapping("/organizationPage")
	public String getOrganizationPage(HttpServletRequest req) {
		if(req.getSession(true).getAttribute("loggedUser")==null)
			return "index";
		CommonMethods.updateTeamsAttribute(req, false);
		CommonMethods.updateOrganizationsAttribute(req, false);
		return "organization";
	}
	
}