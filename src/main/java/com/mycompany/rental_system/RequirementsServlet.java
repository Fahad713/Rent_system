package com.mycompany.rental_system;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequirementsServlet extends HttpServlet {

     // Database credentials (Replace with actual values)
    private static final String DB_URL = "jdbc:derby://localhost:1527/rentalSystem";
    private static final String DB_USER = "rent";
    private static final String DB_PASS = "rent";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("Views/SignIN.jsp");
            return;
        }

        String Userid = "";

        String sql1 = "SELECT * FROM RENT.\"USER\" WHERE EMAIL = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, (String) session.getAttribute("email"));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {  // Move to the first row
                Userid = rs.getString("ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequirementsServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Database error: " + ex.getMessage());
        }

        List<Map<String, String>> properties = new ArrayList<>();

        // Database Query
        String sql = "SELECT AREA, ROOMS, RENT FROM RENT.RENT_REQUIREMENT where USER_ID = " + Userid + " ";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> property = new HashMap<>();
                property.put("Area", rs.getString("AREA"));
                property.put("Rooms", rs.getString("ROOMS"));
                property.put("Rent", "$" + rs.getString("RENT"));
                properties.add(property);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PropertiesServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Database error: " + ex.getMessage());
        }

        request.setAttribute("propertyList", properties);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Views/Dashboard.jsp");
        dispatcher.forward(request, response);
    }

}
