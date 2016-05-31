package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

import controllers.SQLConnection;

@ManagedBean(name="Achievement")
public class Achievement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private int user_id;
	private String errMessage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
	public ArrayList<Achievement> getAchievements() throws ClassNotFoundException, SQLException
	{
		ArrayList<Achievement> achievements = new ArrayList<Achievement>();
		Connection conn = SQLConnection.GetConnection();
		int user_id = Frequent.GetLoggedinUserId(); 
		String strListAchieve = "select * from achievement where user_id = '"+user_id+"'";
		PreparedStatement stmt = conn.prepareStatement(strListAchieve);
		ResultSet rs = stmt.executeQuery();
		if(rs.isBeforeFirst())
		{
			while (rs.next()) {
				Achievement objAchieve = new Achievement();
				objAchieve.setId(rs.getInt("id"));
				objAchieve.setDescription(rs.getString("description"));
				objAchieve.setUser_id(rs.getInt("user_id"));
				achievements.add(objAchieve);
			}
		}
		return achievements;
	}
	
	public boolean deleteAchievement()
	{
		
		boolean done = false;
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		int user_id = Frequent.GetLoggedinUserId();
		String strDelete = "delete from achievement where id = '"+id+"' and user_id = '"+user_id+"'";
		try {
			Connection conn = SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strDelete);
			iReturn=objPreStmt.executeUpdate();
			if (iReturn > 0){
				errMessage= "One Achievement Deleted";
				done = true;
			}
			else{
				done = false;
				errMessage= "Try again";
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			done = false;
		}
		
		return done;
	}
	
	public void resetData(){
		description = "";
		user_id=0;
	}
	
	public String addAchievement()
	{
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		
		try
		{
			String strInsert = "insert into achievement(description,user_id) values(?,?)";
			Connection conn = controllers.SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strInsert);
			objPreStmt.setString(1, description);
			user_id= Frequent.GetLoggedinUserId();
			objPreStmt.setInt(2, user_id);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0){
				description= "";
				errMessage="Your Achievement saved.";
			}
			else{
				errMessage="Your Achievement not saved, Try again adding.";
			}
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
}
