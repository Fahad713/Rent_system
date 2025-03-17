package com.mycompany.rental_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String dbURL = "jdbc:derby://localhost:1527/rentalSystem";
        String dbUser = "rent";
        String dbPass = "rent";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);) {
            String name = request.getParameter("name");
            String pass = request.getParameter("password");

            if (name == null || name.isEmpty()
                    || pass == null || pass.isEmpty()) {

                out.println("<script type='text/javascript'>");
                out.println("alert('All fields are required!');");
                out.println("window.location.href='Views/SignUP.jsp';");
                out.println("</script>");
                return;
            }
            Statement stmt = conn.createStatement();
            String sqlStr1 = "select * from RENT.\"USER\" where \"NAME\"='" + name + "'";
            ResultSet rset1 = stmt.executeQuery(sqlStr1);

            while (rset1.next()) {
                Object username = rset1.getString(2);
                if (name.equals(username)) {
                    Object password = rset1.getString(5);
                    if (pass.equals(password)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", name);
                        response.sendRedirect("Views/Dashboard.jsp");
                        return;
                    }
                }
            }
            out.println("<script type='text/javascript'>");
            out.println("alert('User Not Found!!! Login Again with correct Credentials!!!');");
            out.println("window.location.href='Views/SignIN.jsp';");
            out.println("</script>");

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Database error: " + ex.getMessage());
        }
    }
}
