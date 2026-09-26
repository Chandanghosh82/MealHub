
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        String userName = null;
        String userEmail = null;
        String role = null;

        if (session != null) {

            userName = (String) session.getAttribute("userName");
            userEmail = (String) session.getAttribute("userEmail");
            role = (String) session.getAttribute("role");
        }

        if (userName == null) {

            response.sendRedirect("login.html");
            return;
        }

        String initial =
                userName.substring(0, 1).toUpperCase();

        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>");

        out.println("<title>MealHub - My Profile</title>");

        out.println("<style>");

        out.println("* {");
        out.println("box-sizing: border-box;");
        out.println("}");

        out.println("body {");
        out.println("margin: 0;");
        out.println("font-family: Arial, sans-serif;");
        out.println("background: #f7f8fc;");
        out.println("}");

        out.println(".navbar {");
        out.println("height: 75px;");
        out.println("background: white;");
        out.println("display: flex;");
        out.println("align-items: center;");
        out.println("justify-content: space-between;");
        out.println("padding: 0 7%;");
        out.println("box-shadow: 0 3px 15px rgba(0,0,0,0.08);");
        out.println("}");

        out.println(".logo {");
        out.println("font-size: 28px;");
        out.println("font-weight: bold;");
        out.println("color: #222;");
        out.println("text-decoration: none;");
        out.println("}");

        out.println(".logo span {");
        out.println("color: #ff5a1f;");
        out.println("}");

        out.println(".profile-circle {");
        out.println("width: 48px;");
        out.println("height: 48px;");
        out.println("border-radius: 50%;");
        out.println("background: #ff5a1f;");
        out.println("color: white;");
        out.println("display: flex;");
        out.println("align-items: center;");
        out.println("justify-content: center;");
        out.println("font-size: 20px;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".profile-card {");
        out.println("width: 90%;");
        out.println("max-width: 500px;");
        out.println("margin: 70px auto;");
        out.println("background: white;");
        out.println("padding: 40px;");
        out.println("border-radius: 22px;");
        out.println("text-align: center;");
        out.println("box-shadow: 0 10px 35px rgba(0,0,0,0.10);");
        out.println("}");

        out.println(".big-profile {");
        out.println("width: 100px;");
        out.println("height: 100px;");
        out.println("border-radius: 50%;");
        out.println("background: #ff5a1f;");
        out.println("color: white;");
        out.println("display: flex;");
        out.println("align-items: center;");
        out.println("justify-content: center;");
        out.println("font-size: 42px;");
        out.println("font-weight: bold;");
        out.println("margin: 0 auto 20px;");
        out.println("}");

        out.println(".profile-card h2 {");
        out.println("margin-bottom: 25px;");
        out.println("color: #222;");
        out.println("}");

        out.println(".info {");
        out.println("background: #f7f8fc;");
        out.println("padding: 15px;");
        out.println("border-radius: 12px;");
        out.println("margin: 12px 0;");
        out.println("text-align: left;");
        out.println("}");

        out.println(".info b {");
        out.println("color: #ff5a1f;");
        out.println("}");

        out.println(".logout {");
        out.println("display: inline-block;");
        out.println("margin-top: 20px;");
        out.println("background: #222;");
        out.println("color: white;");
        out.println("padding: 12px 25px;");
        out.println("border-radius: 10px;");
        out.println("text-decoration: none;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".home {");
        out.println("display: inline-block;");
        out.println("margin-top: 20px;");
        out.println("margin-right: 10px;");
        out.println("background: #ff5a1f;");
        out.println("color: white;");
        out.println("padding: 12px 25px;");
        out.println("border-radius: 10px;");
        out.println("text-decoration: none;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<nav class='navbar'>");

        out.println("<a href='index.html' class='logo'>");
        out.println("Meal<span>Hub</span>");
        out.println("</a>");

        out.println("<div class='profile-circle'>");
        out.println(initial);
        out.println("</div>");

        out.println("</nav>");

        out.println("<div class='profile-card'>");

        out.println("<div class='big-profile'>");
        out.println(initial);
        out.println("</div>");

        out.println("<h2>My Profile</h2>");

        out.println("<div class='info'>");
        out.println("<b>👤 Name:</b> " + userName);
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<b>📧 Email:</b> " + userEmail);
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<b>👑 Role:</b> " + role);
        out.println("</div>");

        out.println("<a class='home' href='index.html'>");
        out.println("🏠 Home");
        out.println("</a>");

        out.println("<a class='logout' href='logout'>");
        out.println("🚪 Logout");
        out.println("</a>");

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}
