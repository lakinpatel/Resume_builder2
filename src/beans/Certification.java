package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

import controllers.SQLConnection;

@ManagedBean(name="Certification")
public class Certification implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int user_id;
	private String errMessage;
	private boolean canEdit;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public void resetData(){
		name = "";
		user_id=0;
	}
	
	public String addCertification()
	{
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		
		try
		{
			String strInsert = "insert into certification(name,user_id) values(?,?)";
			Connection conn = controllers.SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strInsert);
			objPreStmt.setString(1, name);
			user_id= Frequent.GetLoggedinUserId();
			objPreStmt.setInt(2, user_id);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0){
				name = "";
				errMessage="Your Certification saved.";
			}
			else{
				errMessage="Your Certification not saved, Try again adding.";
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
	
	public ArrayList<Certification> getCertifications() throws ClassNotFoundException, SQLException
	{
		ArrayList<Certification> certifications = new ArrayList<Certification>();
		Connection conn = SQLConnection.GetConnection();
		int user_id = Frequent.GetLoggedinUserId(); 
		String strListCerti = "select * from certification where user_id = '"+user_id+"'";
		PreparedStatement stmt = conn.prepareStatement(strListCerti);
		ResultSet rs = stmt.executeQuery();
		if(rs.isBeforeFirst())
		{
			while (rs.next()) {
				Certification objCerti = new Certification();
				objCerti.setId(rs.getInt("id"));
				objCerti.setName(rs.getString("name"));
				objCerti.setUser_id(rs.getInt("user_id"));
				certifications.add(objCerti);
			}
		}
		return certifications;
	}
	
	public String editCerti(Certification certification)
	{
		certification.setCanEdit(true);
	    return null;
	}
	
	public boolean deleteCerti()
	{
		
		boolean done = false;
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		int user_id = Frequent.GetLoggedinUserId();
		String strDelete = "delete from certification where id = '"+id+"' and user_id = '"+user_id+"'";
		try {
			Connection conn = SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strDelete);
			iReturn=objPreStmt.executeUpdate();
			if (iReturn > 0){
				errMessage= "One Certification Deleted";
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
}
