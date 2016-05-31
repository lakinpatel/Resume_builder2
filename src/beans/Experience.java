package beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.faces.bean.ManagedBean;

import controllers.SQLConnection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ManagedBean(name = "Experience")
public class Experience implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String companyname;
	private String position;
	private String responsibilities;
	private Date startdate;
	private Date enddate;
	private int user_id;
	private boolean canEdit;

	ArrayList<Experience> experience = new ArrayList<Experience>();

	public void setExperience(ArrayList<Experience> experience) {
		this.experience = experience;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public void resetData(){
		companyname = "";
		position = "";
		responsibilities = "";
		startdate = null;
		enddate = null;
	}

	public void addExperience() throws ClassNotFoundException, SQLException
	{
		int iReturn = 0;
		PreparedStatement objPreStmt = null;
		java.sql.Connection conn = null;
		String strInsert = "insert into experience(companyname, position, responsibilities, startdate, enddate, user_id) values(?,?,?,?,?,?)";
		user_id = Frequent.GetLoggedinUserId();
		conn = controllers.SQLConnection.GetConnection();
		objPreStmt = conn.prepareStatement(strInsert);
		objPreStmt.setString(1, companyname);
		objPreStmt.setString(2, position);
		objPreStmt.setString(3, responsibilities);
		objPreStmt.setDate(4, new java.sql.Date(startdate.getTime()));
		objPreStmt.setDate(5, new java.sql.Date(enddate.getTime()));
		objPreStmt.setInt(6, user_id);
		iReturn = objPreStmt.executeUpdate();

		if (iReturn > 0) {
			resetData();
		} else {
			System.out.println("Not Added");
		}
	}

	public ArrayList<Experience> getExperience() throws ClassNotFoundException, SQLException
	{
		ArrayList<Experience> experiences = new ArrayList<Experience>();
		Connection conn = SQLConnection.GetConnection();
		int user_id = Frequent.GetLoggedinUserId();
		String exp = "select * from experience where user_id = '" + user_id + "'";
		PreparedStatement stmt = conn.prepareStatement(exp);
		ResultSet rs = stmt.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				Experience objExp = new Experience();
				objExp.setId(rs.getInt("id"));
				objExp.setCompanyname(rs.getString("companyname"));
				objExp.setPosition(rs.getString("position"));
				objExp.setResponsibilities(rs.getString("responsibilities"));
				objExp.setStartdate(rs.getDate("startdate"));
				objExp.setEnddate(rs.getDate("enddate"));

				objExp.setUser_id(rs.getInt("user_id"));
				experiences.add(objExp);
			}
		}
		return experiences;
	}

	public String editExp(Experience exp) {
		exp.setCanEdit(true);
		return null;
	}

	/*
	 * public String editExp(Experience id) throws ClassNotFoundException,
	 * SQLException {
	 * 
	 * Connection conn = SQLConnection.GetConnection(); int user_id =
	 * Frequent.GetLoggedinUserId(); System.out.println(user_id); int i = 0;
	 * 
	 * String sql = "update experience SET companyname = '"+ companyname +
	 * "', position= '" + position + "', details = '" + details +
	 * "', startdate ='" + startdate + "',enddate= '" + enddate +
	 * "' where id = '" + id + "'"; PreparedStatement ps =
	 * conn.prepareStatement(sql); i = ps.executeUpdate(sql); ps.close();
	 * 
	 * 
	 * if (i > 0) { return "success"; } else return "unsuccessful"; }
	 */

	public boolean deleteExp() {

		boolean done = false;
		int iReturn = 0;
		PreparedStatement objPreStmt = null;
		int user_id = Frequent.GetLoggedinUserId();
		String strDelete = "delete from experience where id = '" + id + "' and user_id = '" + user_id + "'";
		try {
			Connection conn = SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strDelete);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0) {
				System.out.println("Deleted");
				done = true;
			} else {
				done = false;
				System.out.println("try again");
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		if (iReturn > 0)
			return true;
		else
			return false;

	}

}