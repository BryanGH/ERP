package com.e6893.education.erp.dao;

import com.e6893.education.erp.entity.User;

public interface UserDao {
	public User getUserById(long userId);
	
	public User getUserByEmail(String email);
	
	public int createUser(User user);
	
	public int updateUser(User user);
	
	// public int deleteUser(long userId);
}
