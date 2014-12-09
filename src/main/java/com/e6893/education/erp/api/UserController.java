package com.e6893.education.erp.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.e6893.education.erp.entity.Topic;
import com.e6893.education.erp.entity.User;
import com.e6893.education.erp.services.NlpService;
import com.e6893.education.erp.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	UserService userService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody User userCreate(HttpServletRequest request,
			HttpServletResponse response, User user) {
		System.out.println(request.getParameterMap().toString());
		System.out.println(user.getUserName());
		return userService.userSignUp(user);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> userLogin(HttpServletRequest request,
			HttpServletResponse response, User user, BindingResult result) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		
		User loginUser = userService.userLogin(user);
		if (loginUser != null) {
			responseBody.put("user", loginUser);
			responseBody.put("status", "ok");
		}
		else {
			responseBody.put("status", "none");
		}

		return responseBody;
	}
	
	@RequestMapping(value = "/addHistory", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addHistory(HttpServletRequest request,
			HttpServletResponse response, User user, Topic topic, BindingResult result) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		
		int searchCount = userService.addHistory(user, topic);
		if (searchCount != -1) {
			responseBody.put("searchCount", searchCount);
			responseBody.put("status", "ok");
		}
		else {
			responseBody.put("status", "error");
		}

		return responseBody;
	}
}
