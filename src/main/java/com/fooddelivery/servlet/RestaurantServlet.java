
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

@WebServlet("/restaurants")
public class RestaurantServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String sql = "SELECT * FROM restaurants";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            out.println("<html>");

            out.println("<head>");

            out.println("<title>MealHub - Restaurants</title>");

            out.println("<style>");

            out.println("* {");
            out.println("box-sizing: border-box;");
            out.println("}");

            out.println("body {");
            out.println("margin: 0;");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: #f5f7f6;");
            out.println("color: #222;");
            out.println("}");

            out.println("h2 {");
            out.println("text-align: center;");
            out.println("margin: 35px 0 30px;");
            out.println("font-size: 30px;");
            out.println("color: #198754;");
            out.println("}");

            out.println(".restaurant-container {");
            out.println("display: grid;");
            out.println("grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));");
            out.println("gap: 25px;");
            out.println("max-width: 1200px;");
            out.println("margin: 0 auto;");
            out.println("padding: 20px;");
            out.println("}");

            out.println(".restaurant-card {");
            out.println("background: white;");
            out.println("border-radius: 18px;");
            out.println("padding: 28px 22px;");
            out.println("min-height: 250px;");
            out.println("display: flex;");
            out.println("flex-direction: column;");
            out.println("justify-content: space-between;");
            out.println("text-align: center;");
            out.println("box-shadow: 0 5px 18px rgba(0,0,0,0.10);");
            out.println("border: 1px solid #e5e5e5;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".restaurant-card:hover {");
            out.println("transform: translateY(-6px);");
            out.println("box-shadow: 0 10px 25px rgba(0,0,0,0.15);");
            out.println("}");

            out.println(".restaurant-icon {");
            out.println("font-size: 45px;");
            out.println("margin-bottom: 10px;");
            out.println("}");

            out.println(".restaurant-card h3 {");
            out.println("margin: 5px 0 18px;");
            out.println("font-size: 22px;");
            out.println("color: #198754;");
            out.println("}");

            out.println(".restaurant-card p {");
            out.println("margin: 8px 0;");
            out.println("font-size: 15px;");
            out.println("color: #555;");
            out.println("}");

            out.println(".view-food {");
            out.println("display: inline-block;");
            out.println("margin-top: 18px;");
            out.println("padding: 11px 22px;");
            out.println("background: #198754;");
            out.println("color: white;");
            out.println("text-decoration: none;");
            out.println("border-radius: 8px;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".view-food:hover {");
            out.println("background: #157347;");
            out.println("}");

            out.println(".bottom-link {");
            out.println("text-align: center;");
            out.println("margin: 30px 0;");
            out.println("}");

            out.println(".bottom-link a {");
            out.println("color: #198754;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h2>🍴 Available Restaurants</h2>");

            out.println("<div class='restaurant-container'>");

            while (rs.next()) {

                int id =
                        rs.getInt("restaurant_id");

                String name =
                        rs.getString("restaurant_name");

                String location =
                        rs.getString("location");

                String phone =
                        rs.getString("phone");

                out.println("<div class='restaurant-card'>");

                out.println("<div>");

                out.println("<div class='restaurant-icon'>🍽️</div>");

                out.println("<h3>"
                        + name
                        + "</h3>");

                out.println("<p><b>Location:</b> "
                        + location
                        + "</p>");

                out.println("<p><b>Phone:</b> "
                        + phone
                        + "</p>");

                out.println("</div>");

                out.println("<div>");

                out.println("<a class='view-food' "
                        + "href='restaurantFoods?restaurantId="
                        + id
                        + "'>"
                        + "View Food"
                        + "</a>");

                out.println("</div>");

                out.println("</div>");
            }

            out.println("</div>");

            out.println("<div class='bottom-link'>");

            out.println("<a href='index.html'>🏠 Home</a>");

            out.println(" &nbsp; | &nbsp; ");

            out.println("<a href='foods'>🍴 All Food Items</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h3>Unable to load restaurants</h3>");
        }
    }
}
