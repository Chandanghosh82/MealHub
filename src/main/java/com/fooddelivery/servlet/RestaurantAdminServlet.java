
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

@WebServlet("/adminRestaurants")
public class RestaurantAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        out.println("<title>Manage Restaurants</title>");

        out.println("<style>");

        out.println("body {");
        out.println("    margin: 0;");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    background: #f5f5f5;");
        out.println("}");

        out.println(".navbar {");
        out.println("    background: #ff5722;");
        out.println("    color: white;");
        out.println("    padding: 20px;");
        out.println("    text-align: center;");
        out.println("}");

        out.println(".navbar h2 {");
        out.println("    margin: 0;");
        out.println("}");

        out.println(".container {");
        out.println("    width: 90%;");
        out.println("    margin: 40px auto;");
        out.println("    background: white;");
        out.println("    padding: 30px;");
        out.println("    border-radius: 12px;");
        out.println("    box-shadow: 0 4px 12px rgba(0,0,0,0.12);");
        out.println("}");

        out.println("h1 {");
        out.println("    text-align: center;");
        out.println("    color: #333;");
        out.println("}");

        out.println(".add {");
        out.println("    display: inline-block;");
        out.println("    margin-top: 15px;");
        out.println("    padding: 12px 20px;");
        out.println("    background: #4CAF50;");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 6px;");
        out.println("    font-weight: bold;");
        out.println("}");

        out.println(".add:hover {");
        out.println("    background: #388E3C;");
        out.println("}");

        out.println("table {");
        out.println("    width: 100%;");
        out.println("    border-collapse: collapse;");
        out.println("    margin-top: 30px;");
        out.println("}");

        out.println("th, td {");
        out.println("    border: 1px solid #ddd;");
        out.println("    padding: 12px;");
        out.println("    text-align: center;");
        out.println("}");

        out.println("th {");
        out.println("    background: #ff5722;");
        out.println("    color: white;");
        out.println("}");

        out.println("tr:nth-child(even) {");
        out.println("    background: #f9f9f9;");
        out.println("}");

        out.println(".back {");
        out.println("    display: inline-block;");
        out.println("    margin-top: 25px;");
        out.println("    padding: 10px 20px;");
        out.println("    background: #333;");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 6px;");
        out.println("}");

        out.println(".back:hover {");
        out.println("    background: #555;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<div class='navbar'>");
        out.println("<h2>Food Delivery - Manage Restaurants</h2>");
        out.println("</div>");

        out.println("<div class='container'>");

        out.println("<h1>Restaurant Management</h1>");

        out.println("<a href='addRestaurant.html' class='add'>+ Add Restaurant</a>");

        out.println("<table>");

        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Restaurant Name</th>");
        out.println("<th>Location</th>");
        out.println("<th>Phone</th>");
        out.println("</tr>");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT restaurant_id, restaurant_name, location, phone "
                       + "FROM restaurants ORDER BY restaurant_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                int id = rs.getInt("restaurant_id");

                String name = rs.getString("restaurant_name");

                String location = rs.getString("location");

                String phone = rs.getString("phone");

                out.println("<tr>");

                out.println("<td>" + id + "</td>");

                out.println("<td>" + name + "</td>");

                out.println("<td>" + location + "</td>");

                out.println("<td>" + phone + "</td>");

                out.println("</tr>");
            }

            if (!found) {

                out.println("<tr>");

                out.println("<td colspan='4'>No restaurants found</td>");

                out.println("</tr>");
            }

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            out.println("<tr>");

            out.println("<td colspan='4'>");

            out.println("Database Error: " + e.getMessage());

            out.println("</td>");

            out.println("</tr>");

            e.printStackTrace();
        }

        out.println("</table>");

        out.println("<a href='admin.html' class='back'>&larr; Back to Admin Dashboard</a>");

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}

