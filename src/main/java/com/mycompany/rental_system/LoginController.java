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

public class LoginController extends HttpServlet {

    private static final String DB_URL = "jdbc:derby://localhost:1527/rentalSystem";
    private static final String DB_USER = "rent";
    private static final String DB_PASS = "rent";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Views/SignIN.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String pass = request.getParameter("password");

        if (name == null || name.isEmpty() || pass == null || pass.isEmpty()) {
            showAlert(out, "All fields are required!", "Views/SignIN.jsp");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); PreparedStatement stmt = conn.prepareStatement("SELECT NAME, EMAIL, PASS FROM RENT.\"USER\" WHERE NAME = ?")) {

            stmt.setString(1, name);
            try (ResultSet rset = stmt.executeQuery()) {
                boolean loginSuccess = false;
                String email = null;

                while (rset.next()) {
                    String storedPassword = rset.getString("PASS");
                    if (pass.equals(storedPassword)) {
                        loginSuccess = true;
                        email = rset.getString("EMAIL");
                        break;
                    }
                }

                if (loginSuccess) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", name);
                    session.setAttribute("email", email);
                    response.sendRedirect("Properties");
                } else {
                    showAlert(out, "Invalid username or password. Please try again.", "Views/SignIN.jsp");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Database error: " + ex.getMessage());
        }
    }

    private void showAlert(PrintWriter out, String message, String redirectURL) {
        out.println("<script type='text/javascript'>");
        out.println("alert('" + message + "');");
        out.println("window.location.href='" + redirectURL + "';");
        out.println("</script>");
    }
}
