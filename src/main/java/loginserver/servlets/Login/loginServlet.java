


package loginserver.servlets.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/JavaProject";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";
    
    // SQL query to insert data into the users table
    private static final String CHECK_USER_QUERY = "SELECT * FROM users WHERE username = ? OR password = ?";
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username=request.getParameter("username");
		String password = request.getParameter("password");
		
		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_QUERY)) {
			
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			ResultSet result = preparedStatement.executeQuery();
				
			response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (result.next()) {
            	String uname=result.getString("username");
    			String pwd=result.getString("password");
            	if(uname.equals(username) && pwd.equals(password)) {
            		out.println("<h1>Login successful!</h1>");	
            	}
            	else if(!pwd.equals(password)) {
            		out.println("<h1>Password doesn't match!! Login again!</h1>");
            		out.println("<a href='Login.html'>Login Here</a>");
            	}
            	else {
            		out.println("<h1>Username doesn't match!! Login again!</h1>");
            		out.println("<a href='Login.html'>Login Here</a>");
            	}
                
            } else {
                out.println("<h1>Login failed. Details not found Pls do register.</h1>");
                out.println("<a href='register.html'>Register Here</a>");
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ServletException("Database error!", e);
		}
	}

}
