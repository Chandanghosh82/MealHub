
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fooddelivery.util.DBConnection;

@WebServlet("/adminUsers")
public class AdminUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

        out.println("<title>MealHub - Admin Users</title>");

        out.println("<link rel='preconnect' href='https://fonts.googleapis.com'>");
        out.println("<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap' rel='stylesheet'>");

        out.println("<style>");

        out.println("*{");
        out.println("margin:0;");
        out.println("padding:0;");
        out.println("box-sizing:border-box;");
        out.println("}");

        out.println("body{");
        out.println("font-family:'Poppins',sans-serif;");
        out.println("background:#f7f8fc;");
        out.println("color:#222;");
        out.println("}");

        out.println(".navbar{");
        out.println("height:75px;");
        out.println("background:#ffffff;");
        out.println("display:flex;");
        out.println("align-items:center;");
        out.println("justify-content:space-between;");
        out.println("padding:0 7%;");
        out.println("box-shadow:0 3px 15px rgba(0,0,0,0.08);");
        out.println("}");

        out.println(".logo{");
        out.println("font-size:28px;");
        out.println("font-weight:800;");
        out.println("color:#222;");
        out.println("}");

        out.println(".logo span{");
        out.println("color:#ff5a1f;");
        out.println("}");

        out.println(".admin-title{");
        out.println("font-size:15px;");
        out.println("font-weight:600;");
        out.println("color:#555;");
        out.println("}");

        out.println(".container{");
        out.println("width:92%;");
        out.println("max-width:1200px;");
        out.println("margin:45px auto;");
        out.println("}");

        out.println(".heading{");
        out.println("text-align:center;");
        out.println("margin-bottom:30px;");
        out.println("}");

        out.println(".heading h1{");
        out.println("font-size:34px;");
        out.println("font-weight:800;");
        out.println("}");

        out.println(".heading h1 span{");
        out.println("color:#ff5a1f;");
        out.println("}");

        out.println(".heading p{");
        out.println("color:#777;");
        out.println("font-size:14px;");
        out.println("margin-top:8px;");
        out.println("}");

        out.println(".table-card{");
        out.println("background:white;");
        out.println("border-radius:18px;");
        out.println("padding:25px;");
        out.println("box-shadow:0 8px 30px rgba(0,0,0,0.08);");
        out.println("overflow-x:auto;");
        out.println("}");

        out.println("table{");
        out.println("width:100%;");
        out.println("border-collapse:collapse;");
        out.println("min-width:800px;");
        out.println("}");

        out.println("th{");
        out.println("background:#ff5a1f;");
        out.println("color:white;");
        out.println("padding:15px;");
        out.println("font-size:13px;");
        out.println("font-weight:600;");
        out.println("text-align:center;");
        out.println("}");

        out.println("td{");
        out.println("padding:14px;");
        out.println("border-bottom:1px solid #eee;");
        out.println("font-size:13px;");
        out.println("text-align:center;");
        out.println("}");

        out.println("tr:hover{");
        out.println("background:#fff8f4;");
        out.println("}");

        out.println(".role{");
        out.println("display:inline-block;");
        out.println("padding:5px 12px;");
        out.println("border-radius:20px;");
        out.println("font-size:11px;");
        out.println("font-weight:700;");
        out.println("background:#f1f1f1;");
        out.println("}");

        out.println(".back{");
        out.println("display:inline-block;");
        out.println("margin-top:25px;");
        out.println("padding:12px 22px;");
        out.println("background:#222;");
        out.println("color:white;");
        out.println("text-decoration:none;");
        out.println("border-radius:10px;");
        out.println("font-size:13px;");
        out.println("font-weight:600;");
        out.println("}");

        out.println(".back:hover{");
        out.println("background:#ff5a1f;");
        out.println("}");

        out.println(".empty{");
        out.println("text-align:center;");
        out.println("padding:40px;");
        out.println("color:#777;");
        out.println("}");

        out.println(".error{");
        out.println("background:#fff0f0;");
        out.println("color:#c62828;");
        out.println("padding:20px;");
        out.println("border-radius:10px;");
        out.println("text-align:center;");
        out.println("}");

        out.println("@media(max-width:600px){");

        out.println(".navbar{");
        out.println("padding:0 5%;");
        out.println("}");

        out.println(".admin-title{");
        out.println("display:none;");
        out.println("}");

        out.println(".heading h1{");
        out.println("font-size:27px;");
        out.println("}");

        out.println(".container{");
        out.println("width:95%;");
        out.println("}");

        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        // NAVBAR
        out.println("<nav class='navbar'>");

        out.println("<div class='logo'>");
        out.println("Meal<span>Hub</span>");
        out.println("</div>");

        out.println("<div class='admin-title'>");
        out.println("Admin Panel");
        out.println("</div>");

        out.println("</nav>");

        // MAIN CONTAINER
        out.println("<div class='container'>");

        out.println("<div class='heading'>");

        out.println("<h1>Registered <span>Users</span></h1>");

        out.println("<p>Manage and view all users registered on MealHub</p>");

        out.println("</div>");

        out.println("<div class='table-card'>");

        out.println("<table>");

        out.println("<thead>");

        out.println("<tr>");

        out.println("<th>User ID</th>");
        out.println("<th>Name</th>");
        out.println("<th>Email</th>");
        out.println("<th>Phone</th>");
        out.println("<th>Address</th>");
        out.println("<th>Role</th>");

        out.println("</tr>");

        out.println("</thead>");

        out.println("<tbody>");

        boolean found = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT user_id, name, email, phone, address, role " +
                    "FROM users " +
                    "ORDER BY user_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                found = true;

                int userId =
                        rs.getInt("user_id");

                String name =
                        rs.getString("name");

                String email =
                        rs.getString("email");

                String phone =
                        rs.getString("phone");

                String address =
                        rs.getString("address");

                String role =
                        rs.getString("role");

                out.println("<tr>");

                out.println("<td>"
                        + userId
                        + "</td>");

                out.println("<td><b>"
                        + name
                        + "</b></td>");

                out.println("<td>"
                        + email
                        + "</td>");

                out.println("<td>"
                        + phone
                        + "</td>");

                out.println("<td>"
                        + address
                        + "</td>");

                out.println("<td>");

                out.println("<span class='role'>"
                        + role
                        + "</span>");

                out.println("</td>");

                out.println("</tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<tr>");

            out.println("<td colspan='6'>");

            out.println("<div class='error'>");

            out.println("<b>Unable to load users</b><br><br>");

            out.println(e.getMessage());

            out.println("</div>");

            out.println("</td>");

            out.println("</tr>");

            e.printStackTrace();
        }

        if (!found) {

            out.println("<tr>");

            out.println("<td colspan='6'>");

            out.println("<div class='empty'>");

            out.println("👤 No registered users found.");

            out.println("</div>");

            out.println("</td>");

            out.println("</tr>");
        }

        out.println("</tbody>");

        out.println("</table>");

        out.println("</div>");

        out.println("<a href='admin.html' class='back'>");

        out.println("← Back to Admin Dashboard");

        out.println("</a>");

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}
