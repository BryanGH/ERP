package com.e6893.education.erp.dao.impl;

import org.neo4j.graphdb.GraphDatabaseService;

import com.e6893.education.erp.dao.UserDao;
import com.e6893.education.erp.entity.User;
import com.e6893.education.erp.dbFactory.DatabaseNeo4j;

public class UserDaoImpl implements UserDao {

	private GraphDatabaseService db = DatabaseNeo4j.getDatabase();
	

	
	@Override
	public User getUserById(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

}
