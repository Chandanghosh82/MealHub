package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fooddelivery.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


private static final long serialVersionUID = 1L;

@Override
protected void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    response.sendRedirect("login.html");
}

@Override
protected void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {

        con = DBConnection.getConnection();

        String sql =
                "SELECT user_id, name, email, role " +
                "FROM users " +
                "WHERE email = ? AND password = ?";

        ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);

        rs = ps.executeQuery();

        if (rs.next()) {

            HttpSession session = request.getSession();

            session.setAttribute(
                    "userId",
                    rs.getInt("user_id")
            );

            session.setAttribute(
                    "userName",
                    rs.getString("name")
            );

            session.setAttribute(
                    "userEmail",
                    rs.getString("email")
            );

            session.setAttribute(
                    "role",
                    rs.getString("role")
            );

            String role = rs.getString("role");

            if ("ADMIN".equalsIgnoreCase(role)) {

                response.sendRedirect("admin.html");

            } else {

                response.sendRedirect("index.html");
            }

        } else {

            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Login Failed</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<h2>Invalid Email or Password</h2>");

            out.println("<a href='login.html'>Try Again</a>");

            out.println("</body>");
            out.println("</html>");
        }

    } catch (Exception e) {

        e.printStackTrace();

        PrintWriter out = response.getWriter();

        out.println("<h2>Login Error</h2>");
        out.println("<p>" + e.getMessage() + "</p>");

    } finally {

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }

        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
        }

        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
        }
    }
}


}
