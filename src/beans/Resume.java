package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.SQLConnection;

@ManagedBean(name="Resume")
public class Resume implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String objective;
	private int user_id;
	private String objectData;
	
	private int objectivelength =0;
	
	public int getObjectivelength() throws ClassNotFoundException {
		Resume resume = Frequent.GetLoggedinUserObjective();
		
		if(resume.objective != null){
			objectivelength = resume.objective.length();
		}
		
		return objectivelength;
		
	}
	public void setObjectivelength(int objectivelength) throws ClassNotFoundException {
		Resume resume = Frequent.GetLoggedinUserObjective();
	
		this.objectivelength = resume.objective.length();
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public String getObjectData(){
		if(this.objective != null)
			objectData = this.objective;
		else
			objectData = "";
		return this.objectData;
	}
	
	
	
	public void setUserObjective() throws ClassNotFoundException{
		Resume resume = Frequent.GetLoggedinUserObjective();
		this.objective = resume.objective;
	}
	
	public String addObjective()
	{
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		try
		{
			String strInsert = "insert into resume(objective,user_id) values(?,?)";
			Connection conn = controllers.SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strInsert);
			objPreStmt.setString(1, objective);
			user_id= Frequent.GetLoggedinUserId();
			objPreStmt.setInt(2, user_id);
			iReturn = objPreStmt.executeUpdate();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (iReturn>0)
			return "added";
		else
			return "not";
	}
	
	public String update() {
		int user_id = Frequent.GetLoggedinUserId();
		int i = 0;
		try {
			String sql = "update resume SET objective = '"+ objective + "' where user_id = '" + user_id + "'";	
			
			Connection conn = controllers.SQLConnection.GetConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			i = ps.executeUpdate(sql);
			ps.close();
		} catch (Exception e) {
			System.out.println("Exception is ;" + e);
		}
		if (i > 0) {
			return "success";
		} else
			return "unsuccessful";
	}

}
