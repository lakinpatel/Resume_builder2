package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.*;

/**
 * Servlet implementation class projects
 */
@WebServlet("/projects")
public class projects extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public projects() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.append("Served at: ").append(request.getContextPath());
		out.append("in get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		//HttpSession session = request.getSession();
		String strName=(String) request.getAttribute("title");
		String strDesc=(String) request.getAttribute("description");
		String strTechs=(String) request.getAttribute("technologies");
		int user_id = (int) request.getAttribute("user_id");
		
		Project objProj = new Project();
		objProj.setTitle(strName);
		objProj.setDescription(strDesc);
		objProj.setTechnologies(strTechs);
		objProj.setUser_id(1);
		
		request.setAttribute("project",objProj);
		
		try {
			int done = StoreProject(objProj);
			if (done==1) 
			{
				out.println("data stored");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int StoreProject(Project objProj) throws SQLException
	{
		int iReturn=0;
		String strInsert = "insert into project(title,description,technologies,user_id) values(?,?,?,?)";
		java.sql.Connection connec = null;
		PreparedStatement objPreStmt = null;
		try{
			connec=SQLConnection.GetConnection();
			objPreStmt = connec.prepareStatement(strInsert);
			objPreStmt.setString(1, objProj.getTitle());
			objPreStmt.setString(2, objProj.getDescription());
			objPreStmt.setString(3, objProj.getTechnologies());
			objPreStmt.setInt(4, objProj.getUser_id());
			iReturn = objPreStmt.executeUpdate();
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
			iReturn = 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			objPreStmt.close();
			connec.close();
		}
		
		return iReturn;
	}

}
