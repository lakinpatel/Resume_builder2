package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.mail.iap.ConnectionException;

import controllers.SQLConnection;

@ManagedBean(name = "Project")
public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String description;
	private String technologies;
	private int user_id;
	private String errMessage;
	private boolean showEditForm  = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTechnologies() {
		return technologies;
	}

	public void setTechnologies(String technologies) {
		this.technologies = technologies;
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

	public boolean isShowEditForm() {
		return showEditForm;
	}

	public void setShowEditForm(boolean showEditForm) {
		this.showEditForm = showEditForm;
	}

	public void resetData() {
		title = "";
		description = "";
		technologies = "";
		user_id = 0;
	}

	public int storeProject() throws SQLException {
		int iReturn = 0;
		String strInsert = "insert into project(title,description,technologies,user_id) values(?,?,?,?)";
		user_id = Frequent.GetLoggedinUserId();
		java.sql.Connection connec = null;
		PreparedStatement objPreStmt = null;
		try {
			connec = SQLConnection.GetConnection();
			objPreStmt = connec.prepareStatement(strInsert);
			objPreStmt.setString(1, title);
			objPreStmt.setString(2, description);
			objPreStmt.setString(3, technologies);
			objPreStmt.setInt(4, user_id);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0) {
				resetData();
				errMessage = "Your Project saved.";
			} else {
				errMessage = "Your Project not saved, Try again adding.";
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			iReturn = 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			objPreStmt.close();
			connec.close();
		}

		return iReturn;
	}

	public ArrayList<Project> getProjects() throws ClassNotFoundException, SQLException {
		ArrayList<Project> projects = new ArrayList<Project>();
		Connection conn = SQLConnection.GetConnection();
		int user_id = Frequent.GetLoggedinUserId();
		String strListProj = "select * from project where user_id = '" + user_id + "'";
		PreparedStatement stmt = conn.prepareStatement(strListProj);
		ResultSet rs = stmt.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				Project objProj = new Project();
				objProj.setId(rs.getInt("id"));
				objProj.setTitle(rs.getString("title"));
				objProj.setDescription(rs.getString("description"));
				objProj.setTechnologies(rs.getString("technologies"));
				objProj.setUser_id(rs.getInt("user_id"));
				projects.add(objProj);
			}
		}
		return projects;
	}

	public boolean deleteProject() {

		boolean done = false;
		int iReturn = 0;
		PreparedStatement objPreStmt = null;
		int user_id = Frequent.GetLoggedinUserId();
		String strDelete = "delete from project where id = '" + id + "' and user_id = '" + user_id + "'";
		try {
			Connection conn = SQLConnection.GetConnection();
			objPreStmt = conn.prepareStatement(strDelete);
			iReturn = objPreStmt.executeUpdate();
			if (iReturn > 0) {
				errMessage = "One Project Deleted";
				done = true;
			} else {
				done = false;
				errMessage = "Try again";
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			done = false;
		}

		return done;
	}

	public void editProject()
	{
		System.out.println(id);
		Project objProj = getProjectData();
		this.id=objProj.id;
		this.title=objProj.title;
		this.description=objProj.description;
		this.technologies=objProj.technologies;
		user_id=Frequent.GetLoggedinUserId();
		System.out.println(user_id+"id = "+objProj.id+objProj.title+objProj.description+objProj.technologies);
	}
	
	public Project getProjectData()
	{
		Project objProj = new Project();
		int user_id = Frequent.GetLoggedinUserId();
		String sqlProj = "select * from project where id = '" + id + "' and user_id = '" + user_id + "'";
		Connection conn = null;
		try {
			conn = SQLConnection.GetConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlProj);
			ResultSet rs = stmt.executeQuery();

			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					id=rs.getInt("id");
					title=rs.getString("title");
					description=rs.getString("description");
					technologies=rs.getString("technologies");
					objProj.setId(id);
					objProj.setTitle(title);
					objProj.setDescription(description);
					objProj.setTechnologies(technologies);
					objProj.setUser_id(user_id);
				}
			}
			else{
				System.out.println(user_id + "not found");
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return objProj;
	}

	public String updateProject() {
		int user_id = Frequent.GetLoggedinUserId();
		Connection connection = null;
		System.out.println(user_id);
		int i = 0;
		try {
			connection = SQLConnection.GetConnection();
			String sql = "update project SET title = '" + title + "', description= '" + description
					+ "', technologies = '" + technologies + "'  where id = '" + id + "' and user_id= '"
					+ Frequent.GetLoggedinUserId() + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			i = ps.executeUpdate(sql);
			if (i > 0) {
				errMessage = "Project details updated";
			} else {
				errMessage = "Error updating Project details";
			}
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
