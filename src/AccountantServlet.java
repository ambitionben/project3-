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
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/accountant")
public class AccountantServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String report = request.getParameter("report");
        System.out.println("Received report request: " + report);

        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            System.out.println("Loading properties file...");
            filein = new FileInputStream(
                    getServletContext().getRealPath("/WEB-INF/lib/properties/theaccountant.properties"));
            properties.load(filein);

            System.out.println("Creating data source...");
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            System.out.println("Connecting to database...");
            connection = dataSource.getConnection();

            System.out.println("Executing report: " + report);
            switch (report) {
                case "Get_Maximum_Status":
                    System.out.println("Selected report: Get_Maximum_Status");
                    statement = connection.prepareCall("{CALL Get_The_Maximum_Status_Of_All_Suppliers()}");
                    break;
                case "Get_Total_Weight_Parts":
                    System.out.println("Selected report: Get_Total_Weight_Parts");
                    statement = connection.prepareCall("{CALL Get_The_Sum_Of_All_Parts_Weights()}");
                    break;
                case "Get_Total_Number_Shipments":
                    System.out.println("Selected report: Get_Total_Number_Shipments");
                    statement = connection.prepareCall("{CALL Get_The_Total_Number_Of_Shipments()}");
                    break;
                case "Get_Name_NumWorkers_MostWorkers_Job":
                    System.out.println("Selected report: Get_Name_NumWorkers_MostWorkers_Job");
                    statement = connection.prepareCall("{CALL Get_The_Name_Of_The_Job_With_The_Most_Workers()}");
                    break;
                case "List_Name_Status_Suppliers":
                    System.out.println("Selected report: List_Name_Status_Suppliers");
                    statement = connection.prepareCall("{CALL List_The_Name_And_Status_Of_All_Suppliers()}");
                    break;
                default:
                    System.err.println("Invalid report selected: " + report);
                    out.println("<p>Invalid report selected.</p>");
                    return;
            }

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
        } catch (SQLException e) {
            System.err.println("Error executing stored procedure: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<div style='color: red; border: 2px solid red; padding: 10px;'>");
            out.println("<p>Error executing stored procedure:</p>");
            out.println("<p><b>" + e.getMessage() + "</b></p>");
            out.println("<p><b>Procedure:</b> " + report + "</p>");
            out.println("</div>");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
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
