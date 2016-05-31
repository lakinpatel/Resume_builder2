package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

import controllers.SQLConnection;

@ManagedBean(name = "Education")
public class Education implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String program;
	private String graduated_year;
	private String institution;
	private String country;
	private int user_id;
	private String errMessage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getGraduated_year() {
		return graduated_year;
	}

	public void setGraduated_year(String graduated_year) {
		this.graduated_year = graduated_year;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String addEducation() {
		int iReturn = 0;
		PreparedStatement objPreStmt = null;

		try {
			String strInsert = "insert into education(program,graduated_year,institution,country,user_id) values(?,?,?,?,?)";
			Connection conn = controllers.SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strInsert);
			objPreStmt.setString(1, program);
			objPreStmt.setString(2, graduated_year);
			objPreStmt.setString(3, institution);
			objPreStmt.setString(4, country);
			int user_id = Frequent.GetLoggedinUserId();
			objPreStmt.setInt(5, user_id);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0) {
				program = "";
				graduated_year = "";
				institution = "";
				country = "";
				errMessage = "Your Education saved.";
			} else {
				errMessage = "Your Education not saved, Try again adding.";
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (iReturn > 0)
			return "added";
		else
			return "not";
	}
	
		
	
	
	public ArrayList<Education> getEducations() throws ClassNotFoundException, SQLException
	{
		ArrayList<Education> educations = new ArrayList<Education>();
		Connection conn = SQLConnection.GetConnection();
		int user_id = Frequent.GetLoggedinUserId(); 
		String strListCerti = "select * from education where user_id = '"+user_id+"'";
		PreparedStatement stmt = conn.prepareStatement(strListCerti);
		ResultSet rs = stmt.executeQuery();
		if(rs.isBeforeFirst())
		{
			while (rs.next()) {
				Education objEdu = new Education();
				objEdu.setId(rs.getInt("id"));
				objEdu.setProgram(rs.getString("program"));
				objEdu.setGraduated_year(rs.getString("graduated_year"));
				objEdu.setInstitution(rs.getString("institution"));
				objEdu.setCountry(rs.getString("country"));
				objEdu.setUser_id(rs.getInt("user_id"));
				educations.add(objEdu);
				
			
			}
		
		}
		return educations;
	}
	
	public boolean deleteEducation()
	{
		
		boolean done = false;
		int iReturn=0;
		PreparedStatement objPreStmt = null;
		int user_id = Frequent.GetLoggedinUserId();
		String strDelete = "delete from education where id = '"+id+"' and user_id = '"+user_id+"'";
		try {
			Connection conn = SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strDelete);
			iReturn=objPreStmt.executeUpdate();
			if (iReturn > 0){
				errMessage= "One Education Deleted";
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