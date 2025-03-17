package com.mycompany.rental_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class SignUP extends HttpServlet {

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
            String email = request.getParameter("email");
            String phn = request.getParameter("phone");

            if (name == null || name.isEmpty()
                    || pass == null || pass.isEmpty()
                    || email == null || email.isEmpty()
                    || phn == null || phn.isEmpty()) {

                out.println("<script type='text/javascript'>");
                out.println("alert('All fields are required!');");
                out.println("window.location.href='Views/SignUP.jsp';");
                out.println("</script>");
                return;
            }
            Statement stmt = conn.createStatement();
            String sqlStr1 = "select * from RENT.\"USER\" where \"EMAIL\"='" + email + "'";
            ResultSet rset1 = stmt.executeQuery(sqlStr1);

            if (rset1.next()) {
                out.println("<script type='text/javascript'>");
                out.println("alert('Email is Already Registered!!! TRY New One');");
                out.println("window.location.href='Views/SignUP.jsp';");
                out.println("</script>");
                return;
            }

            Statement stmt2 = conn.createStatement();
            String sqlStr2 = "select * from RENT.\"USER\" where \"PHONE\"='" + phn + "'";
            ResultSet rset2 = stmt2.executeQuery(sqlStr2);

            if (rset2.next()) {
                out.println("<script type='text/javascript'>");
                out.println("alert('Phone is Already Registered!!! TRY New One');");
                out.println("window.location.href='Views/SignUP.jsp';");
                out.println("</script>");
                return;
            }

            String sqlInsert = "INSERT INTO RENT.\"USER\" (\"NAME\", \"EMAIL\", \"PHONE\", \"PASS\") VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phn);
            pstmt.setString(4, pass);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("user", name);
                out.println("<script type='text/javascript'>");
                out.println("alert('Account Registered Seccussfully!');");
                out.println("window.location.href='Views/Dashboard.jsp';");
                out.println("</script>");
            } else {
                out.println("Registration failed. Please try again.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Database error: " + ex.getMessage());
        }
    }

}
