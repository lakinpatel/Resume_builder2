package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.*;

/**
 * Servlet implementation class resumes
 */
@WebServlet("/resumes")
public class resumes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public resumes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String strObject=(String) request.getAttribute("objective");
		int strUser=(int) request.getAttribute("user_id");
		Resume objResume = new Resume();
		objResume.setObjective(strObject);
		objResume.setUser_id(strUser);
		
		try {
			int done = StoreResume(objResume);
			if (done==1) 
			{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/build_resume.xhtml");
				out.println("Your Objective has been stored");
				rd.include(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int StoreResume(Resume objResume) throws SQLException{
		int iReturn=0;
		String strInsert = "insert into resume(objective,user_id) values(?,?)";
		java.sql.Connection connec = null;
		PreparedStatement objPreStmt = null;
		try{
			connec=SQLConnection.GetConnection();
			objPreStmt = connec.prepareStatement(strInsert);
			objPreStmt.setString(1, objResume.getObjective());
			objPreStmt.setInt(2, objResume.getUser_id());
			iReturn = objPreStmt.executeUpdate();
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
			iReturn = 0;
		} catch (SQLException e) {
			e.printStackTrace();
			iReturn = 0;
		}
		finally {
			objPreStmt.close();
			connec.close();
		}
		
		return iReturn;
	}

}
