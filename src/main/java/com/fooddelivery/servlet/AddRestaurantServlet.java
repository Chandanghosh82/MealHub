
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fooddelivery.util.DBConnection;

@WebServlet("/addRestaurant")
public class AddRestaurantServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String restaurantName = request.getParameter("restaurantName");
        String location = request.getParameter("location");
        String phone = request.getParameter("phone");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO restaurants "
                    + "(restaurant_name, location, phone) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, restaurantName);
            ps.setString(2, location);
            ps.setString(3, phone);

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                out.println("<meta charset='UTF-8'>");
                out.println("<title>Restaurant Added</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background: #f5f5f5;");
                out.println("margin: 0;");
                out.println("text-align: center;");
                out.println("}");

                out.println(".navbar {");
                out.println("background: #ff5722;");
                out.println("color: white;");
                out.println("padding: 20px;");
                out.println("}");

                out.println(".box {");
                out.println("width: 450px;");
                out.println("margin: 80px auto;");
                out.println("background: white;");
                out.println("padding: 40px;");
                out.println("border-radius: 12px;");
                out.println("box-shadow: 0 4px 12px rgba(0,0,0,0.15);");
                out.println("}");

                out.println("h1 {");
                out.println("color: green;");
                out.println("}");

                out.println(".button {");
                out.println("display: inline-block;");
                out.println("margin-top: 20px;");
                out.println("padding: 12px 25px;");
                out.println("background: #ff5722;");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 6px;");
                out.println("}");

                out.println("</style>");

                out.println("</head>");

                out.println("<body>");

                out.println("<div class='navbar'>");
                out.println("<h2>Food Delivery - Admin Panel</h2>");
                out.println("</div>");

                out.println("<div class='box'>");

                out.println("<h1>Restaurant Added Successfully!</h1>");

                out.println("<p>Restaurant <b>"
                        + restaurantName
                        + "</b> has been added successfully.</p>");

                out.println("<a href='adminRestaurants' class='button'>");
                out.println("View Restaurants");
                out.println("</a>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

        } catch (Exception e) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Error</title>");
            out.println("</head>");

            out.println("<body>");

            out.println("<h2>Error while adding restaurant</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("<a href='addRestaurant.html'>Go Back</a>");

            out.println("</body>");

            out.println("</html>");

            e.printStackTrace();
        }
    }
}
