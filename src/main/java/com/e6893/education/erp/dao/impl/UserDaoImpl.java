package com.e6893.education.erp.dao.impl;

import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.stereotype.Repository;

import com.e6893.education.erp.dao.UserDao;
import com.e6893.education.erp.entity.Topic;
import com.e6893.education.erp.entity.User;
import com.e6893.education.erp.dbFactory.DatabaseNeo4j;

@Repository
public class UserDaoImpl implements UserDao {

	
	
	@Override
	public User getUserByUserName(String userName) {
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		ExecutionEngine engine = new ExecutionEngine( db );
		ExecutionResult result;
		User user = new User();
		try ( Transaction tx = db.beginTx(); )
        {
			user.setUserName(userName);
            result = engine.execute( "match (n) where n.userName = \"" + user.getUserName() + "\" return n");
            Iterator<Node> n_column = result.columnAs( "n" );
            
            if (!n_column.hasNext())
            	return null;
            Node node = n_column.next();
            user.setPwd(node.getProperty("pwd").toString());
             
            tx.success();   
            return user;
        }
		catch (Exception e) {
			return null;
		}
		finally {
			db.shutdown();
		}
	}

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
//		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
//		
//		try ( Transaction tx = db.beginTx(); )
//        {
//            Node userNode = db.createNode();
//            userNode.setProperty( "userName", user.getUserName() );
//            userNode.setProperty("pwd", user.getPwd());
//            tx.success();
//            return user;
//        }
//		catch (Exception e) {
//			return null;
//		}
//		finally {
//			db.shutdown();
//		}
		
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		
        ExecutionEngine engine = new ExecutionEngine( db );

		try ( Transaction tx = db.beginTx(); )
        {
            //engine.execute( "match (n) where n.userName = \"" + user.getUserName() + "\" set n.pwd = \"" + user.getPwd() + "\"");
            engine.execute("merge (me: User {userName:'" + user.getUserName() + "', pwd:'" + user.getPwd() + "'})"
            		+ " return me");
            tx.success();   
            return user;
        }
		catch (Exception e) {
			return null;
		}
		finally {
			db.shutdown();
		}
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		
        ExecutionEngine engine = new ExecutionEngine( db );

		try ( Transaction tx = db.beginTx(); )
        {
            engine.execute( "match (n) where n.userName = \"" + user.getUserName() + "\" set n.pwd = \"" + user.getPwd() + "\"");
            
            tx.success();   
            return user;
        }
		catch (Exception e) {
			return null;
		}
		finally {
			db.shutdown();
		}
	}

	public boolean verifyUser(User user) {
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		ExecutionEngine engine = new ExecutionEngine( db );
		ExecutionResult result;
		try ( Transaction tx = db.beginTx(); )
        {
            result = engine.execute( "match (n) where n.userName = \"" + user.getUserName() + "\" and n.pwd = \"" + user.getPwd() + "\" return n");
            Iterator<Node> n_column = result.columnAs( "n" );
            
            if (!n_column.hasNext())
            	return false;
             
            tx.success();   
            return true;
        }
		catch (Exception e) {
			return false;
		}
		finally {
			db.shutdown();
		}
	}
	
	public int addSearchedHistory(User user, Topic topic) {
		
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		ExecutionEngine engine = new ExecutionEngine( db );
		ExecutionResult result;
		try ( Transaction tx = db.beginTx(); )
        {
            result = engine.execute( "match (u:User {userName: '" + user.getUserName() + "'}) "
            		+ " merge (tp:Topic {topicName: '" + topic.getTopicName() 
            		+ "'})"
            		+ " merge (u)-[s:Searched]->(tp)"
            		+ " on match set s.searchCount = s.searchCount + 1 "
            		+ " on create set s.searchCount = 1"
            		+ " return s.searchCount as count");
            
            int searchCount = Integer.parseInt(result.columnAs("count").next().toString());
//            System.out.println(searchCount);
             
            tx.success();   
            return searchCount;
        }
		catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
		finally {
			db.shutdown();
		}
	}
	
	public void updateUserSimilarity() {
		
		// Periodically call this one
		
		GraphDatabaseService db = DatabaseNeo4j.getDatabase();
		ExecutionEngine engine = new ExecutionEngine( db );
		try ( Transaction tx = db.beginTx(); )
        {
            engine.execute("MATCH (u1:User)-[h1:Searched]->(tp:Topic)<-[h2:Rated]-(u2:User) "
            		+ " WITH SUM(h1.searchCount * h2.searchCount) AS DotProduct, "
            		+ " SQRT(REDUCE(h1Dot = 0.0, a IN COLLECT(h1.searchCount) | h1Dot + a^2)) AS h1Length, "
            		+ " SQRT(REDUCE(h2Dot = 0.0, b IN COLLECT(h2.searchCount) | h2Dot + b^2)) AS h2Length, "
            		+ " u1, u2 "
            		+ " MERGE (u1)-[s:Similarity]-(u2) "
            		+ " SET s.similarity = DotProduct / (h1Length * h2Length)");
            
            tx.success();
        }
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			db.shutdown();
		}
	}
	
	
	
	
}
