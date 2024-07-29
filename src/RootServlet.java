
/* Name:BenjaminBelizaire
Course: CNT 4714 – Summer 2024 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Servlet: RootServlet.java
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

@WebServlet("/ExecuteSQLServlet")
public class RootServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sqlCommand = request.getParameter("sqlCommand");
        System.out.println("Received SQL Command: " + sqlCommand);

        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            System.out.println("Loading properties file...");
            filein = new FileInputStream(getServletContext().getRealPath("/WEB-INF/lib/properties/root.properties"));
            properties.load(filein);

            System.out.println("Creating data source...");
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            System.out.println("Connecting to database...");
            connection = dataSource.getConnection();

            System.out.println("Executing query...");
            statement = connection.prepareStatement(sqlCommand);

            int affectedRows = 0;
            boolean isSelect = sqlCommand.trim().toLowerCase().startsWith("select");
            if (isSelect) {
                resultSet = statement.executeQuery();
                out.println("<table border='1'>");
                int columnCount = resultSet.getMetaData().getColumnCount();
                out.println("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<th>" + resultSet.getMetaData().getColumnName(i) + "</th>");
                }
                out.println("</tr>");
                while (resultSet.next()) {
                    out.println("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        out.println("<td>" + resultSet.getString(i) + "</td>");
                    }
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                affectedRows = statement.executeUpdate();
                out.println("<p>Command executed successfully. Affected rows: " + affectedRows + "</p>");

                boolean isBusinessLogicTriggered = sqlCommand.trim().toLowerCase().contains("shipments") &&
                        (sqlCommand.trim().toLowerCase().startsWith("insert") ||
                                sqlCommand.trim().toLowerCase().startsWith("update"));

                if (isBusinessLogicTriggered) {
                    String updateStatusSql = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments WHERE quantity >= 100)";
                    statement = connection.prepareStatement(updateStatusSql);
                    int updatedRows = statement.executeUpdate();
                    if (updatedRows > 0) {
                        out.println("<p>Business Logic Detected - Updating Supplier Status</p>");
                        out.println("<p>Business Logic updated " + updatedRows + " supplier status marks.</p>");
                    } else {
                        out.println("<p>Business Logic Detected - No Supplier Status Updates</p>");
                    }
                } else {
                    out.println("<p>Business Logic Not Detected</p>");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL command: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("ERROR:" + e.getMessage() + "\nCOMMAND:" + sqlCommand);
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
            if (filein != null)
                filein.close();
        }
    }
}
