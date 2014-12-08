package com.e6893.education.erp.entity;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;


@NodeEntity
public class User {
	@GraphId
	private Long userId;
	
	private String userName;
	private String pwd;		//password
	
	private String email;

	@Fetch
	@RelatedTo(type = "InterestedTopics", direction = Direction.OUTGOING)
	private Set<Topic> interestedTopics;
	
	@RelatedToVia(type = "Searched")
	private Set<Searched> histories;
	
	
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
