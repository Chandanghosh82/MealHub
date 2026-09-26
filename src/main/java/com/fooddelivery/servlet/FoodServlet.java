
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

@WebServlet("/foods")
public class FoodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");
        String category = request.getParameter("category");

        String sql =
                "SELECT f.food_id, f.food_name, f.category, "
                + "f.price, f.description, f.image_url, "
                + "r.restaurant_name "
                + "FROM food_items f "
                + "JOIN restaurants r "
                + "ON f.restaurant_id = r.restaurant_id "
                + "WHERE 1=1";

        /*
         * VEG FILTER
         */

        if ("Veg".equalsIgnoreCase(category)) {

            sql = sql
                    + " AND LOWER(f.food_name) NOT LIKE '%chicken%'"
                    + " AND LOWER(f.food_name) NOT LIKE '%mutton%'"
                    + " AND LOWER(f.food_name) NOT LIKE '%fish%'"
                    + " AND LOWER(f.food_name) NOT LIKE '%egg%'"
                    + " AND LOWER(f.food_name) NOT LIKE '%pepperoni%'";

        }

        /*
         * NON-VEG FILTER
         */

        else if ("Non-Veg".equalsIgnoreCase(category)) {

            sql = sql
                    + " AND ("
                    + "LOWER(f.food_name) LIKE '%chicken%'"
                    + " OR LOWER(f.food_name) LIKE '%mutton%'"
                    + " OR LOWER(f.food_name) LIKE '%fish%'"
                    + " OR LOWER(f.food_name) LIKE '%egg%'"
                    + " OR LOWER(f.food_name) LIKE '%pepperoni%'"
                    + ")";

        }

        /*
         * NORMAL SEARCH
         */

        if (search != null && !search.trim().isEmpty()) {

            sql = sql + " AND f.food_name LIKE ?";

        }

        /*
         * NORMAL CATEGORY FILTER
         */

        if (category != null
                && !category.trim().isEmpty()
                && !category.equalsIgnoreCase("Veg")
                && !category.equalsIgnoreCase("Non-Veg")) {

            sql = sql + " AND f.category = ?";

        }

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            int parameterIndex = 1;

            if (search != null && !search.trim().isEmpty()) {

                ps.setString(
                        parameterIndex,
                        "%" + search + "%");

                parameterIndex++;

            }

            if (category != null
                    && !category.trim().isEmpty()
                    && !category.equalsIgnoreCase("Veg")
                    && !category.equalsIgnoreCase("Non-Veg")) {

                ps.setString(
                        parameterIndex,
                        category);

            }

            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head>");

            out.println("<title>MealHub - Food Items</title>");

            out.println("<style>");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: #f7f7f7;");
            out.println("margin: 0;");
            out.println("padding: 30px;");
            out.println("}");

            out.println("h2 {");
            out.println("text-align: center;");
            out.println("color: #222;");
            out.println("}");

            /* SEARCH */

            out.println(".search-box {");
            out.println("text-align: center;");
            out.println("margin-bottom: 20px;");
            out.println("}");

            out.println(".search-box input {");
            out.println("padding: 12px;");
            out.println("width: 250px;");
            out.println("border: 1px solid #ccc;");
            out.println("border-radius: 8px;");
            out.println("}");

           
            out.println(".search-box button {");
            out.println("padding: 12px 18px;");
            out.println("background: #ff6b35;");
            out.println("color: white;");
            out.println("border: none;");
            out.println("border-radius: 8px;");
            out.println("cursor: pointer;");
            out.println("margin-left: 5px;");
            out.println("}");

            /* CATEGORY OVALS */

            out.println(".category-container {");
            out.println("display: flex;");
            out.println("flex-wrap: wrap;");
            out.println("justify-content: center;");
            out.println("gap: 10px;");
            out.println("margin: 20px auto 30px auto;");
            out.println("max-width: 1100px;");
            out.println("}");

            out.println(".category-oval {");
            out.println("display: inline-flex;");
            out.println("align-items: center;");
            out.println("justify-content: center;");
            out.println("padding: 8px 17px;");
            out.println("border-radius: 50px;");
            out.println("background: white;");
            out.println("color: #333;");
            out.println("border: 1px solid #ddd;");
            out.println("text-decoration: none;");
            out.println("font-size: 13px;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("box-shadow: 0 2px 7px rgba(0,0,0,0.08);");
            out.println("}");

            out.println(".category-oval:hover {");
            out.println("background: #ff6b35;");
            out.println("color: white;");
            out.println("border-color: #ff6b35;");
            out.println("transform: translateY(-2px);");
            out.println("}");

            /* VEG */

            out.println(".veg-oval {");
            out.println("background: #e8f7ee;");
            out.println("color: #198754;");
            out.println("border-color: #198754;");
            out.println("}");

            out.println(".veg-oval:hover {");
            out.println("background: #198754;");
            out.println("color: white;");
            out.println("}");

            /* NON VEG */

            out.println(".nonveg-oval {");
            out.println("background: #fdeaea;");
            out.println("color: #dc3545;");
            out.println("border-color: #dc3545;");
            out.println("}");

            out.println(".nonveg-oval:hover {");
            out.println("background: #dc3545;");
            out.println("color: white;");
            out.println("}");

            /* FOOD CONTAINER */

            out.println(".food-container {");
            out.println("display: flex;");
            out.println("flex-wrap: wrap;");
            out.println("justify-content: center;");
            out.println("gap: 20px;");
            out.println("}");

            /* CARD */

            out.println(".card {");
            out.println("background: white;");
            out.println("border-radius: 15px;");
            out.println("padding: 15px;");
            out.println("width: 270px;");
            out.println("box-shadow: 0 4px 15px rgba(0,0,0,0.10);");
            out.println("vertical-align: top;");
            out.println("}");

            /* IMAGE */

            out.println(".food-image {");
            out.println("width: 100%;");
            out.println("height: 180px;");
            out.println("object-fit: cover;");
            out.println("border-radius: 10px;");
            out.println("}");

            out.println(".card h3 {");
            out.println("color: #222;");
            out.println("margin-bottom: 10px;");
            out.println("}");

            /* PRICE */

            out.println(".price {");
            out.println("font-size: 20px;");
            out.println("font-weight: bold;");
            out.println("color: #198754;");
            out.println("}");

            /* BUTTON */

            out.println(".button {");
            out.println("display: inline-block;");
            out.println("background: #198754;");
            out.println("color: white;");
            out.println("padding: 10px 15px;");
            out.println("text-decoration: none;");
            out.println("border-radius: 8px;");
            out.println("margin-top: 10px;");
            out.println("}");

            out.println(".button:hover {");
            out.println("background: #157347;");
            out.println("}");

            /* BOTTOM LINK */

            out.println(".bottom-link {");
            out.println("display: block;");
            out.println("text-align: center;");
            out.println("margin-top: 30px;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h2>🍴 MealHub - Available Food Items</h2>");

            /* SEARCH */

            out.println("<div class='search-box'>");

            out.println("<form action='foods' method='get'>");

            out.println("<input type='text' name='search' "
                    + "placeholder='Search food'>");

            

            out.println("<button type='submit'>Search</button>");

            out.println("</form>");

            out.println("</div>");

            /* CATEGORY OVALS */

            out.println("<div class='category-container'>");

            out.println("<a class='category-oval' href='foods'>");
            out.println("🍽️ All");
            out.println("</a>");

            out.println("<a class='category-oval veg-oval' "
                    + "href='foods?category=Veg'>");
            out.println("🟢 Veg");
            out.println("</a>");

            out.println("<a class='category-oval nonveg-oval' "
                    + "href='foods?category=Non-Veg'>");
            out.println("🔴 Non-Veg");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Pizza'>");
            out.println("🍕 Pizza");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Biryani'>");
            out.println("🍛 Biryani");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Burger'>");
            out.println("🍔 Burger");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=South Indian'>");
            out.println("🥞 South Indian");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=North Indian'>");
            out.println("🥘 North Indian");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Chinese'>");
            out.println("🍜 Chinese");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Tandoori'>");
            out.println("🍗 Tandoori");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Desserts'>");
            out.println("🍰 Desserts");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Beverages'>");
            out.println("🥤 Beverages");
            out.println("</a>");

            out.println("<a class='category-oval' "
                    + "href='foods?category=Thali'>");
            out.println("🍱 Thali");
            out.println("</a>");

            out.println("</div>");

            /* FOOD CARDS */

            out.println("<div class='food-container'>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int foodId =
                        rs.getInt("food_id");

                String foodName =
                        rs.getString("food_name");

                String foodCategory =
                        rs.getString("category");

                double price =
                        rs.getDouble("price");

                String description =
                        rs.getString("description");

                String restaurantName =
                        rs.getString("restaurant_name");

                String imageUrl =
                        rs.getString("image_url");

                if (imageUrl == null ||
                        imageUrl.trim().isEmpty()) {

                    imageUrl =
                            "https://via.placeholder.com/600x400?text="
                            + java.net.URLEncoder.encode(
                                    foodName,
                                    "UTF-8");
                }

                out.println("<div class='card'>");

                out.println("<img src='"
                        + imageUrl
                        + "' class='food-image'>");

                out.println("<h3>"
                        + foodName
                        + "</h3>");

                out.println("<p><b>Restaurant:</b> "
                        + restaurantName
                        + "</p>");

                out.println("<p><b>Category:</b> "
                        + foodCategory
                        + "</p>");

                out.println("<p class='price'>₹"
                        + price
                        + "</p>");

                out.println("<p>"
                        + description
                        + "</p>");

                out.println("<a class='button' "
                        + "href='cart?foodId="
                        + foodId
                        + "'>Add to Cart</a>");

                out.println("</div>");
            }

            out.println("</div>");

            if (!found) {

                out.println(
                        "<h3 style='text-align:center;'>"
                        + "No food items found."
                        + "</h3>");
            }

            out.println("<div class='bottom-link'>");

            out.println("<a href='cart'>🛒 View Cart</a>");

            out.println(" &nbsp; | &nbsp; ");

            out.println("<a href='index.html'>🏠 Home</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h3>Unable to load food items</h3>");
        }
    }
}
