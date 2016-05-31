package beans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.SQLConnection;

public class Frequent
{
	public static HttpServletRequest GetRequest()
	{
		HttpServletRequest request = null;		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			request = (HttpServletRequest) context.getExternalContext().getRequest();
        }
		catch(Exception ex){}
		return request;
	}
	
	public static HttpServletResponse GetResponse()
	{
		HttpServletResponse response = null;		
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			response = (HttpServletResponse) context.getExternalContext().getRequest();
        }
		catch(Exception ex){}
		return response;
	}
	
	public static int GetLoggedinUserId()
	{
		HttpServletRequest request = GetRequest();
		int user_id = (int)((HttpSession)request.getSession()).getAttribute("user_id");
		return user_id;
	}
	
	public static User GetLoggedinUser() throws ClassNotFoundException
	{
		User objUser = new User();
		int user_id = GetLoggedinUserId();
		String strUser = "select id,firstname,lastname,emailid,password,phone,dateofbirth,city,country from user where id = '" + user_id + "'";
		try {
				PreparedStatement ps1 = SQLConnection.GetConnection().prepareStatement(strUser);

				ResultSet rs = ps1.executeQuery();

				while (rs.next()) {
					objUser.setId(rs.getInt("id"));
					objUser.setFirstname(rs.getString("firstname"));
					objUser.setLastname(rs.getString("lastname"));
					objUser.setEmailid(rs.getString("emailid"));
					objUser.setPhone(rs.getInt("phone"));
					objUser.setCity(rs.getString("city"));
					objUser.setCountry(rs.getString("country"));
					objUser.setPassword(rs.getString("password"));
					objUser.setDateofbirth(rs.getDate("dateofbirth"));
				}
				rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		return objUser;
	}
	
	public static Resume GetLoggedinUserObjective() throws ClassNotFoundException
	{
		Resume objResume = new Resume();
		int user_id = GetLoggedinUserId();
		String strObjective = "select objective from resume where user_id = '" + user_id + "'";
	
		try {
				PreparedStatement ps1 = SQLConnection.GetConnection().prepareStatement(strObjective);

				ResultSet rs = ps1.executeQuery();

				while (rs.next()) {
					objResume.setObjective(rs.getString("objective"));
				}
				rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		return objResume;
	}
	
}
