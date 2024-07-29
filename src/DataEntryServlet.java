
/* Name:BenjaminBelizaire
Course: CNT 4714 – Summer 2024 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Servlet: DataEntryServlet.java
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/dataentryuser")
public class DataEntryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String table = request.getParameter("table");
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        Connection connection = null;
        PreparedStatement statement = null;
        PrintWriter out = response.getWriter();
        String sql = "";
        boolean businessLogicTriggered = false;

        response.setContentType("text/html");

        try {
            filein = new FileInputStream(
                    getServletContext().getRealPath("/WEB-INF/lib/properties/dataentryuser.properties"));
            properties.load(filein);

            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            connection = dataSource.getConnection();

            switch (table) {
                case "suppliers":
                    sql = "INSERT INTO suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, request.getParameter("snum"));
                    statement.setString(2, request.getParameter("sname"));
                    statement.setInt(3, Integer.parseInt(request.getParameter("status")));
                    statement.setString(4, request.getParameter("city"));
                    break;
                case "parts":
                    sql = "INSERT INTO parts (pnum, pname, color, weight, city) VALUES (?, ?, ?, ?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, request.getParameter("pnum"));
                    statement.setString(2, request.getParameter("pname"));
                    statement.setString(3, request.getParameter("color"));
                    statement.setInt(4, Integer.parseInt(request.getParameter("weight")));
                    statement.setString(5, request.getParameter("city"));
                    break;
                case "jobs":
                    sql = "INSERT INTO jobs (jnum, jname, numworkers, city) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, request.getParameter("jnum"));
                    statement.setString(2, request.getParameter("jname"));
                    statement.setInt(3, Integer.parseInt(request.getParameter("numworkers")));
                    statement.setString(4, request.getParameter("city"));
                    break;
                case "shipments":
                    sql = "INSERT INTO shipments (snum, pnum, jnum, quantity) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, request.getParameter("snum"));
                    statement.setString(2, request.getParameter("pnum"));
                    statement.setString(3, request.getParameter("jnum"));
                    statement.setInt(4, Integer.parseInt(request.getParameter("quantity")));
                    businessLogicTriggered = Integer.parseInt(request.getParameter("quantity")) >= 100;
                    break;
            }

            int rowsAffected = statement.executeUpdate();

            out.println("<div style='color: blue; border: 2px solid blue; padding: 10px;'>");
            out.println("<p>New " + table + " record: (");

            // Append the values entered for the new record
            switch (table) {
                case "suppliers":
                    out.println(request.getParameter("snum") + ", " + request.getParameter("sname") + ", "
                            + request.getParameter("status") + ", " + request.getParameter("city"));
                    break;
                case "parts":
                    out.println(request.getParameter("pnum") + ", " + request.getParameter("pname") + ", "
                            + request.getParameter("color") + ", " + request.getParameter("weight") + ", "
                            + request.getParameter("city"));
                    break;
                case "jobs":
                    out.println(request.getParameter("jnum") + ", " + request.getParameter("jname") + ", "
                            + request.getParameter("numworkers") + ", " + request.getParameter("city"));
                    break;
                case "shipments":
                    out.println(request.getParameter("snum") + ", " + request.getParameter("pnum") + ", "
                            + request.getParameter("jnum") + ", " + request.getParameter("quantity"));
                    break;
            }

            out.println(") - successfully entered into database.</p>");

            // Business logic message
            if (businessLogicTriggered) {
                out.println("<p>Business Logic Detected - Updating Supplier Status</p>");
                String updateStatusSql = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments WHERE quantity >= 100)";
                statement = connection.prepareStatement(updateStatusSql);
                int suppliersUpdated = statement.executeUpdate();
                if (suppliersUpdated > 0) {
                    out.println("<p>Business Logic updated " + suppliersUpdated + " supplier status marks.</p>");
                } else {
                    out.println("<p>Business Logic Detected - No Supplier Status Updates</p>");
                }
            } else {
                out.println("<p>Business Logic Not Detected</p>");
            }

            out.println("</div>");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<div style='color: red; border: 2px solid red; padding: 10px;'>");
            out.println("<p>Error executing SQL command:</p>");
            out.println("<p><b>" + e.getMessage() + "</b></p>");
            out.println("<p><b>Command:</b> " + sql + "</p>");
            out.println("</div>");
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
                if (filein != null)
                    filein.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
