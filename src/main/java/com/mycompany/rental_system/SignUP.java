package com.mycompany.rental_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUP extends HttpServlet {

    private static final String DB_URL = "jdbc:derby://localhost:1527/rentalSystem";
    private static final String DB_USER = "rent";
    private static final String DB_PASS = "rent";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Views/SignUP.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String pass = request.getParameter("password");
        String email = request.getParameter("email");
        String phn = request.getParameter("phone");

        if (name == null || name.isEmpty() || pass == null || pass.isEmpty()
                || email == null || email.isEmpty() || phn == null || phn.isEmpty()) {
            showAlert(out, "All fields are required!", "Views/SignUP.jsp");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            // Check if email exists
            if (recordExists(conn, "SELECT EMAIL FROM RENT.\"USER\" WHERE EMAIL = ?", email)) {
                showAlert(out, "Email is already registered! Try a new one.", "Views/SignUP.jsp");
                return;
            }

            // Check if phone exists
            if (recordExists(conn, "SELECT PHONE FROM RENT.\"USER\" WHERE PHONE = ?", phn)) {
                showAlert(out, "Phone number is already registered! Try a new one.", "Views/SignUP.jsp");
                return;
            }

            // Insert new user (Fixed: Changed "PASSWORD" to "PASS")
            String sqlInsert = "INSERT INTO RENT.\"USER\" (\"NAME\", \"EMAIL\", \"PHONE\", \"PASS\") VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phn);
                pstmt.setString(4, pass); // Plaintext password (not secure)

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", name);
                    session.setAttribute("email", email);
                    showAlert(out, "Account Registered Successfully!", "Properties");
                } else {
                    out.println("Registration failed. Please try again.");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(SignUP.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Database error: " + ex.getMessage());
        }
    }

    private boolean recordExists(Connection conn, String query, String value) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a record exists
            }
        }
    }

    private void showAlert(PrintWriter out, String message, String redirectURL) {
        out.println("<script type='text/javascript'>");
        out.println("alert('" + message + "');");
        out.println("window.location.href='" + redirectURL + "';");
        out.println("</script>");
    }
}
