package beans;

import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.resteasy.spi.HttpResponse;

import java.text.SimpleDateFormat;

@ManagedBean(name = "User")
public class User {

	private int id;
	private String firstname;
	private String lastname;
	private String emailid;
	private String password;
	private int phone;

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	private Date dateofbirth;
	private String city;
	private String country;

	public static String getDb() {
		return db;
	}

	public static void setDb(String db) {
		User.db = db;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	private String dbName;
	private String dbpassword;
	public static String db = "resume_builder";

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}

	private Connection connection;
	String connectionURL = "jdbc:mysql://localhost:3306/" + db;
	ResultSet rs;

	public User() {
		try{
			// Load the database driver
			Class.forName("com.mysql.jdbc.Driver");
			// Get a Connection to the database
			connection = DriverManager.getConnection(connectionURL, "root", "");
		}
		catch (Exception e) {
			System.out.println("Exception is ;" + e);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String date = fmt.format(dateofbirth);

		java.sql.Date dateofbrth = java.sql.Date.valueOf(new String(date));
		this.dateofbirth = dateofbrth;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String insert() 
	{
		HttpServletRequest request = null;
		int i = 0;
		try {
			String sql = "insert into user(firstname,lastname,emailid,password,phone, dateofbirth,city,country) values('"
					+ firstname + "','" + lastname + "','" + emailid + "','" + password + "','" + phone + "','"
					+ dateofbirth + "','" + city + "','" + country + "')";
			PreparedStatement ps = connection.prepareStatement(sql);
			i = ps.executeUpdate(sql);
			ps.close();
		} catch (Exception e) {
			System.out.println("Exception is ;" + e);
		}

		if (i > 0) {
			login();
			return "success";
		} else
			return "unsuccessful";
	}

	public void dbData(String uName, String password) {
		if (uName != null) 
		{
			try {
				PreparedStatement ps1 = connection
						.prepareStatement("select id,firstname,lastname,emailid,password,phone,dateofbirth,city,country from user where emailid = '" + uName + "' and password = '"+password+"'");

				rs = ps1.executeQuery();
				while (rs.next()) {
					dbName = rs.getString("emailid");
					dbpassword = rs.getString("password");
					this.firstname=rs.getString("firstname");
					this.lastname=rs.getString("lastname");
					this.phone=rs.getInt("phone");
					this.city=rs.getString("city");
					this.country=rs.getString("country");
					this.dateofbirth=rs.getDate("dateofbirth");
					this.id = rs.getInt("id");
				}
				rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
	public String login() 
	{
		dbData(emailid,password);
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		 
		if (emailid.equals(dbName) && password.equals(dbpassword)) 
		{
			//request = (HttpServletRequest) context.getExternalContext().getRequest();
            request = Frequent.GetRequest();
			HttpSession session = request.getSession();
            if(session.getAttribute("user_id")==null){
            	session.setAttribute("user_id", id);
            }
		}
		if(1>0)
			return "output";
		else
			return "invalid";
}

	public void logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
				.handleNavigation(FacesContext.getCurrentInstance(), null, "/login.xhtml");
	}
	
	public void setUserData() throws ClassNotFoundException{
		User currUser = Frequent.GetLoggedinUser();
		this.id=currUser.id;
		this.firstname = currUser.firstname;
		this.lastname=currUser.lastname;
		this.emailid=currUser.emailid;
		this.country=currUser.country;
		this.city=currUser.city;
		this.phone=currUser.phone;
		this.password=currUser.password;
		this.dateofbirth=currUser.dateofbirth;
	}

	public String update() {
		int user_id = Frequent.GetLoggedinUserId();
		System.out.println(user_id);
		int i = 0;
		try {
			String sql = "update user SET firstname = '"+ firstname + "', lastname= '" + lastname + "', emailid = '" + emailid + "', password ='" + password + "',phone= '" + phone + "', dateofbirth = '" + dateofbirth + "',city = '" + city + "',country= '" + country + "' where id = '" + user_id + "'";	
			PreparedStatement ps = connection.prepareStatement(sql);
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
