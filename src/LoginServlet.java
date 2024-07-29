
/* Name:BenjaminBelizaire
Course: CNT 4714 – Summer 2024 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Servlet: LoginServlet.java
Date: August 1, 2024
*/
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isValid = false;

        try {
            System.out.println("Loading properties file...");
            filein = new FileInputStream(
                    getServletContext().getRealPath("/WEB-INF/lib/properties/systemapp.properties"));
            properties.load(filein);

            System.out.println("Creating data source...");
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            System.out.println("Connecting to database...");
            connection = dataSource.getConnection();

            System.out.println("Executing query...");
            String sql = "SELECT * FROM usercredentials WHERE login_username = ? AND login_password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (filein != null) {
                filein.close();
            }
        }

        if (isValid) {
            System.out.println("Authentication successful!");
            if (username.equals("root")) {
                response.sendRedirect("root.jsp");
            } else if (username.equals("client")) {
                response.sendRedirect("client.jsp");
            } else if (username.equals("dataentryuser")) {
                response.sendRedirect("dataentryuser.jsp");
            } else if (username.equals("theaccountant")) {
                response.sendRedirect("accountant.jsp");
            } else {
                response.getWriter().println("Authentication Successful!");
            }
        } else {
            System.out.println("Redirecting to errorpage.html");
            response.sendRedirect("/Project3/errorpage.html");
        }
    }
}
