
/* Name:BenjaminBelizaire
Course: CNT 4714 – Summer 2024 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Servlet: AccountantServlet.java
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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/accountant")
public class AccountantServlet extends HttpServlet {

    private Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        FileInputStream filein = new FileInputStream(
                getServletContext().getRealPath("/WEB-INF/lib/properties/theaccountant.properties"));
        properties.load(filein);

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
        dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
        dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

        return dataSource.getConnection();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmdValue = request.getParameter("cmd");
        String command = "";
        String message = "";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection connection = getConnection()) {
            switch (cmdValue) {
                case "1":
                    command = "{call Get_The_Sum_Of_All_Parts_Weights()}";
                    break;
                case "2":
                    command = "{call Get_The_Maximum_Status_Of_All_Suppliers()}";
                    break;
                case "3":
                    command = "{call Get_The_Total_Number_Of_Shipments()}";
                    break;
                case "4":
                    command = "{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}";
                    break;
                case "5":
                    command = "{call List_The_Name_And_Status_Of_All_Suppliers()}";
                    break;
                default:
                    command = "{call ERROR()}";
                    break;
            }

            CallableStatement statement = connection.prepareCall(command);
            boolean returnValue = statement.execute();

            if (returnValue) {
                ResultSet resultSet = statement.getResultSet();
                message = ResultSetToHTMLFormatter.getHtmlRows(resultSet);
            } else {
                message = "No data found or error executing RPC!";
            }

            statement.close();
        } catch (SQLException e) {
            message = "<tr bgcolor=#ff0000><td><font color=#ffffff><b>Error executing the SQL statement:</b><br>"
                    + e.getMessage() + "</td></tr></font>";
        }

        out.print(message);
        out.close();
    }

    private static class ResultSetToHTMLFormatter {
        public static String getHtmlRows(ResultSet rs) throws SQLException {
            StringBuilder htmlRows = new StringBuilder();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add table headers
            htmlRows.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                htmlRows.append("<th>").append(metaData.getColumnName(i)).append("</th>");
            }
            htmlRows.append("</tr>");

            // Add table rows
            while (rs.next()) {
                htmlRows.append("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    htmlRows.append("<td>").append(rs.getString(i)).append("</td>");
                }
                htmlRows.append("</tr>");
            }

            return htmlRows.toString();
        }
    }
}
