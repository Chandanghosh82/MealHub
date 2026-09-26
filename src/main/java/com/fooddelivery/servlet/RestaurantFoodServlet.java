
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

@WebServlet("/restaurantFoods")
public class RestaurantFoodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String restaurantId =
                request.getParameter("restaurantId");

        String sql =
                "SELECT food_id, food_name, category, price, "
                + "description, image_url "
                + "FROM food_items "
                + "WHERE restaurant_id = ?";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(
                    1,
                    Integer.parseInt(restaurantId));

            ResultSet rs =
                    ps.executeQuery();

            out.println("<html>");

            out.println("<head>");

            out.println("<title>MealHub - Restaurant Food</title>");

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
            out.println("margin: 35px 0 25px;");
            out.println("font-size: 30px;");
            out.println("color: #198754;");
            out.println("}");

            out.println(".food-container {");
            out.println("display: grid;");
            out.println("grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));");
            out.println("gap: 25px;");
            out.println("max-width: 1200px;");
            out.println("margin: 0 auto;");
            out.println("padding: 20px;");
            out.println("}");

            out.println(".food-card {");
            out.println("background: white;");
            out.println("border-radius: 18px;");
            out.println("overflow: hidden;");
            out.println("box-shadow: 0 5px 18px rgba(0,0,0,0.10);");
            out.println("border: 1px solid #e5e5e5;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".food-card:hover {");
            out.println("transform: translateY(-6px);");
            out.println("box-shadow: 0 10px 25px rgba(0,0,0,0.15);");
            out.println("}");

            out.println(".food-image {");
            out.println("width: 100%;");
            out.println("height: 200px;");
            out.println("object-fit: cover;");
            out.println("display: block;");
            out.println("}");

            out.println(".food-content {");
            out.println("padding: 20px;");
            out.println("text-align: center;");
            out.println("}");

            out.println(".food-content h3 {");
            out.println("margin: 5px 0 15px;");
            out.println("font-size: 21px;");
            out.println("color: #198754;");
            out.println("}");

            out.println(".food-content p {");
            out.println("margin: 8px 0;");
            out.println("font-size: 14px;");
            out.println("color: #555;");
            out.println("}");

            out.println(".price {");
            out.println("font-size: 20px !important;");
            out.println("font-weight: bold;");
            out.println("color: #198754 !important;");
            out.println("}");

            out.println(".cart-button {");
            out.println("display: inline-block;");
            out.println("margin-top: 12px;");
            out.println("padding: 11px 22px;");
            out.println("background: #198754;");
            out.println("color: white;");
            out.println("text-decoration: none;");
            out.println("border-radius: 8px;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".cart-button:hover {");
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

            out.println("<h2>🍴 Food Items</h2>");

            out.println("<div class='food-container'>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int foodId =
                        rs.getInt("food_id");

                String foodName =
                        rs.getString("food_name");

                String category =
                        rs.getString("category");

                double price =
                        rs.getDouble("price");

                String description =
                        rs.getString("description");

                String imageUrl =
                        rs.getString("image_url");

                if (imageUrl == null ||
                        imageUrl.trim().isEmpty()) {

                    imageUrl =
                            "images/margherita-pizza.jpg";
                }

                out.println("<div class='food-card'>");

                out.println("<img src='"
                        + imageUrl
                        + "' class='food-image'>");

                out.println("<div class='food-content'>");

                out.println("<h3>"
                        + foodName
                        + "</h3>");

                out.println("<p><b>Category:</b> "
                        + category
                        + "</p>");

                out.println("<p class='price'>₹"
                        + price
                        + "</p>");

                out.println("<p>"
                        + description
                        + "</p>");

                out.println("<a class='cart-button' "
                        + "href='cart?foodId="
                        + foodId
                        + "'>"
                        + "🛒 Add to Cart"
                        + "</a>");

                out.println("</div>");

                out.println("</div>");
            }

            out.println("</div>");

            if (!found) {

                out.println(
                        "<p style='text-align:center;'>"
                        + "No food items found."
                        + "</p>");
            }

            out.println("<div class='bottom-link'>");

            out.println(
                    "<a href='restaurants'>"
                    + "← Back to Restaurants"
                    + "</a>");

            out.println(" &nbsp; | &nbsp; ");

            out.println(
                    "<a href='foods'>"
                    + "🍴 All Food Items"
                    + "</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h3>Unable to load food items</h3>");
        }
    }
}
