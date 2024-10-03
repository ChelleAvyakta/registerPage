package loginserver.servlets.registration;

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

/**
 * Servlet implementation class registrationServlet
 */
@WebServlet("/registrationServlet")
public class registrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/JavaProject";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    // SQL query to insert data into the users table
    private static final String INSERT_QUERY = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    private static final String CHECK_USER_QUERY = "SELECT * FROM users WHERE username=? OR email=?";

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("uname");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Prepare response for the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Database connection and insertion
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement1 = connection.prepareStatement(CHECK_USER_QUERY);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            // Set parameters for the CHECK_USER_QUERY
            preparedStatement1.setString(1, username);
            preparedStatement1.setString(2, email);

            // Check if the user already exists
            ResultSet resultSet = preparedStatement1.executeQuery();

            if (resultSet.next()) {
            	String uname=resultSet.getString("username");
            	String eml=resultSet.getString("email");
            	out.print(uname+"UNAME");
            	out.print(eml+"EMAIL");
            	if(uname.equals(username) && eml.equals(email)) {
            		out.println("<h1>User already registered</h1>");
                    out.println("<a href='Login.html'> Login Here</a>");
            	}
            	else if(uname.equals(username)){
            		out.println("<h1>User name already registered</h1>");
                    out.println("<a href='Login.html'> Login Here</a>");
            	}
            	else {
                out.println("<h1>Email already registered</h1>");
                out.println("<a href='Login.html'> Login Here</a>");
            	}
            } else {
                // Set parameters for the INSERT_QUERY
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                // Execute the insert query
                int result = preparedStatement.executeUpdate();

                if (result > 0) {
                    out.println("<h1>Registration successful!</h1>");
                    out.println("<a href='Login.html'>Login Here</a>");
                } else {
                    out.println("<h1>Registration failed. Please try again.</h1>");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error!", e);
        } finally {
            out.close();
        }
    }
}
