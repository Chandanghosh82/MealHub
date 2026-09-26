
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

@WebServlet("/searchFood")
public class SearchFoodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");
        String category = request.getParameter("category");

        if (search == null) {
            search = "";
        }

        if (category == null) {
            category = "";
        }

        String sql =
                "SELECT f.food_id, f.food_name, f.category, "
                + "f.price, f.description, f.image_url, "
                + "r.restaurant_name "
                + "FROM food_items f "
                + "JOIN restaurants r "
                + "ON f.restaurant_id = r.restaurant_id "
                + "WHERE 1=1";

        boolean normalSearch = false;

        if (!search.trim().isEmpty()) {

            if (search.equalsIgnoreCase("Pizza")) {

                sql = sql +
                        " AND f.category = 'Pizza'";

            } else if (search.equalsIgnoreCase("Biryani")) {

                sql = sql +
                        " AND f.category = 'Biryani'";

            } else if (search.equalsIgnoreCase("Burger")) {

                sql = sql +
                        " AND f.category = 'Burger'";

            } else if (search.equalsIgnoreCase("Indian")) {

                sql = sql +
                        " AND (f.category LIKE '%Indian%' "
                        + "OR f.food_name LIKE '%Indian%' "
                        + "OR f.description LIKE '%Indian%')";

            } else if (search.equals("99")) {

                sql = sql +
                        " AND f.price <= 99";

            } else if (search.equals("199")) {

                sql = sql +
                        " AND f.price <= 199";

            } else {

                normalSearch = true;

                sql = sql +
                        " AND (f.food_name LIKE ? "
                        + "OR f.category LIKE ? "
                        + "OR r.restaurant_name LIKE ?)";
            }
        }

        if (!category.trim().isEmpty()) {

            sql = sql +
                    " AND f.category = ?";
        }

        sql = sql +
                " ORDER BY f.food_name";

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>");

        out.println("<title>MealHub - Search Results</title>");

        out.println("<link rel='preconnect' "
                + "href='https://fonts.googleapis.com'>");

        out.println("<link rel='preconnect' "
                + "href='https://fonts.gstatic.com' crossorigin>");

        out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap' "
                + "rel='stylesheet'>");

        out.println("<style>");

        out.println("* {");
        out.println("margin: 0;");
        out.println("padding: 0;");
        out.println("box-sizing: border-box;");
        out.println("}");

        out.println("body {");
        out.println("font-family: 'Poppins', sans-serif;");
        out.println("background: #f7f8fc;");
        out.println("color: #222;");
        out.println("min-height: 100vh;");
        out.println("}");

        out.println(".navbar {");
        out.println("height: 75px;");
        out.println("background: white;");
        out.println("display: flex;");
        out.println("justify-content: space-between;");
        out.println("align-items: center;");
        out.println("padding: 0 7%;");
        out.println("box-shadow: 0 3px 15px rgba(0,0,0,0.08);");
        out.println("}");

        out.println(".logo {");
        out.println("font-size: 28px;");
        out.println("font-weight: 800;");
        out.println("color: #222;");
        out.println("text-decoration: none;");
        out.println("}");

        out.println(".logo span {");
        out.println("color: #ff5a1f;");
        out.println("}");

        out.println(".nav-links {");
        out.println("display: flex;");
        out.println("align-items: center;");
        out.println("gap: 25px;");
        out.println("}");

        out.println(".nav-links a {");
        out.println("text-decoration: none;");
        out.println("color: #333;");
        out.println("font-size: 14px;");
        out.println("font-weight: 600;");
        out.println("}");

        out.println(".login-btn {");
        out.println("background: #ff5a1f;");
        out.println("color: white !important;");
        out.println("padding: 11px 22px;");
        out.println("border-radius: 25px;");
        out.println("}");

        out.println(".hero {");
        out.println("text-align: center;");
        out.println("padding: 55px 20px 35px;");
        out.println("}");

        out.println(".hero h1 {");
        out.println("font-size: 38px;");
        out.println("font-weight: 800;");
        out.println("margin-bottom: 10px;");
        out.println("}");

        out.println(".hero h1 span {");
        out.println("color: #ff5a1f;");
        out.println("}");

        out.println(".hero p {");
        out.println("color: #777;");
        out.println("font-size: 15px;");
        out.println("}");

        out.println(".search-box {");
        out.println("width: 90%;");
        out.println("max-width: 850px;");
        out.println("margin: 0 auto 40px;");
        out.println("background: white;");
        out.println("padding: 18px;");
        out.println("border-radius: 18px;");
        out.println("box-shadow: 0 8px 30px rgba(0,0,0,0.08);");
        out.println("}");

        out.println(".search-form {");
        out.println("display: flex;");
        out.println("gap: 12px;");
        out.println("flex-wrap: wrap;");
        out.println("}");

        out.println(".search-input {");
        out.println("flex: 1;");
        out.println("min-width: 220px;");
        out.println("padding: 14px 18px;");
        out.println("border: 1px solid #ddd;");
        out.println("border-radius: 12px;");
        out.println("font-family: 'Poppins', sans-serif;");
        out.println("font-size: 14px;");
        out.println("outline: none;");
        out.println("}");

        out.println(".category {");
        out.println("padding: 14px;");
        out.println("border: 1px solid #ddd;");
        out.println("border-radius: 12px;");
        out.println("font-family: 'Poppins', sans-serif;");
        out.println("font-size: 14px;");
        out.println("background: white;");
        out.println("outline: none;");
        out.println("}");

        out.println(".search-btn {");
        out.println("border: none;");
        out.println("background: #ff5a1f;");
        out.println("color: white;");
        out.println("padding: 14px 25px;");
        out.println("border-radius: 12px;");
        out.println("font-family: 'Poppins', sans-serif;");
        out.println("font-weight: 600;");
        out.println("cursor: pointer;");
        out.println("}");

        out.println(".results-title {");
        out.println("width: 90%;");
        out.println("max-width: 1200px;");
        out.println("margin: 0 auto 25px;");
        out.println("font-size: 22px;");
        out.println("font-weight: 700;");
        out.println("}");

        out.println(".food-container {");
        out.println("width: 90%;");
        out.println("max-width: 1200px;");
        out.println("margin: auto;");
        out.println("display: grid;");
        out.println("grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));");
        out.println("gap: 25px;");
        out.println("padding-bottom: 50px;");
        out.println("}");

        out.println(".food-card {");
        out.println("background: white;");
        out.println("border-radius: 20px;");
        out.println("overflow: hidden;");
        out.println("box-shadow: 0 8px 25px rgba(0,0,0,0.07);");
        out.println("transition: 0.3s;");
        out.println("}");

        out.println(".food-card:hover {");
        out.println("transform: translateY(-6px);");
        out.println("box-shadow: 0 15px 35px rgba(0,0,0,0.12);");
        out.println("}");

        out.println(".food-image {");
        out.println("width: 100%;");
        out.println("height: 190px;");
        out.println("object-fit: cover;");
        out.println("display: block;");
        out.println("}");

        out.println(".food-info {");
        out.println("padding: 22px;");
        out.println("}");

        out.println(".food-card h3 {");
        out.println("font-size: 20px;");
        out.println("margin-bottom: 8px;");
        out.println("}");

        out.println(".restaurant {");
        out.println("font-size: 13px;");
        out.println("color: #777;");
        out.println("margin-bottom: 8px;");
        out.println("}");

        out.println(".category-text {");
        out.println("display: inline-block;");
        out.println("background: #f1f1f1;");
        out.println("padding: 5px 10px;");
        out.println("border-radius: 20px;");
        out.println("font-size: 11px;");
        out.println("font-weight: 600;");
        out.println("margin-bottom: 12px;");
        out.println("}");

        out.println(".description {");
        out.println("font-size: 13px;");
        out.println("color: #777;");
        out.println("min-height: 42px;");
        out.println("margin-bottom: 15px;");
        out.println("}");

        out.println(".price {");
        out.println("font-size: 21px;");
        out.println("font-weight: 800;");
        out.println("color: #ff5a1f;");
        out.println("margin-bottom: 18px;");
        out.println("}");

        out.println(".cart-btn {");
        out.println("display: block;");
        out.println("text-align: center;");
        out.println("background: #222;");
        out.println("color: white;");
        out.println("text-decoration: none;");
        out.println("padding: 12px;");
        out.println("border-radius: 12px;");
        out.println("font-size: 13px;");
        out.println("font-weight: 600;");
        out.println("}");

        out.println(".cart-btn:hover {");
        out.println("background: #ff5a1f;");
        out.println("}");

        out.println(".no-results {");
        out.println("width: 90%;");
        out.println("max-width: 600px;");
        out.println("margin: 20px auto 60px;");
        out.println("background: white;");
        out.println("padding: 50px;");
        out.println("border-radius: 20px;");
        out.println("text-align: center;");
        out.println("box-shadow: 0 8px 25px rgba(0,0,0,0.07);");
        out.println("}");

        out.println(".no-results h2 {");
        out.println("margin-bottom: 10px;");
        out.println("}");

        out.println(".back-btn {");
        out.println("display: inline-block;");
        out.println("margin-top: 20px;");
        out.println("background: #ff5a1f;");
        out.println("color: white;");
        out.println("text-decoration: none;");
        out.println("padding: 12px 22px;");
        out.println("border-radius: 12px;");
        out.println("font-weight: 600;");
        out.println("}");

        out.println("footer {");
        out.println("background: #222;");
        out.println("color: #aaa;");
        out.println("text-align: center;");
        out.println("padding: 25px;");
        out.println("font-size: 13px;");
        out.println("}");

        out.println("@media(max-width: 600px) {");

        out.println(".navbar {");
        out.println("padding: 0 5%;");
        out.println("}");

        out.println(".nav-links {");
        out.println("gap: 10px;");
        out.println("}");

        out.println(".nav-links a {");
        out.println("font-size: 11px;");
        out.println("}");

        out.println(".hero h1 {");
        out.println("font-size: 30px;");
        out.println("}");

        out.println(".search-form {");
        out.println("flex-direction: column;");
        out.println("}");

        out.println(".search-input, .category, .search-btn {");
        out.println("width: 100%;");
        out.println("}");

        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<nav class='navbar'>");

        out.println("<a href='index.html' class='logo'>");

        out.println("Meal<span>Hub</span>");

        out.println("</a>");

        out.println("<div class='nav-links'>");

        out.println("<a href='myOrders'>My Orders</a>");

        out.println("<a href='cart'>My Cart</a>");

        out.println("<a href='login.html' class='login-btn'>Login</a>");

        out.println("</div>");

        out.println("</nav>");

        out.println("<section class='hero'>");

        out.println("<h1>Search <span>Results</span></h1>");

        if (!search.trim().isEmpty()) {

            out.println("<p>Showing results for: <b>"
                    + search
                    + "</b></p>");

        } else {

            out.println("<p>"
                    + "Explore all available food items at MealHub"
                    + "</p>");
        }

        out.println("</section>");

        out.println("<div class='search-box'>");

        out.println("<form class='search-form' "
                + "action='searchFood' method='get'>");

        out.println("<input class='search-input' "
                + "type='text' "
                + "name='search' "
                + "value='" + search + "' "
                + "placeholder='Search food, category or restaurant'>");

        out.println("<select class='category' name='category'>");

        out.println("<option value=''>All Categories</option>");

        out.println("<option value='Pizza' "
                + (category.equals("Pizza") ? "selected" : "")
                + ">Pizza</option>");

        out.println("<option value='Biryani' "
                + (category.equals("Biryani") ? "selected" : "")
                + ">Biryani</option>");

        out.println("<option value='Burger' "
                + (category.equals("Burger") ? "selected" : "")
                + ">Burger</option>");

        out.println("<option value='South Indian' "
                + (category.equals("South Indian") ? "selected" : "")
                + ">South Indian</option>");

        out.println("</select>");

        out.println("<button class='search-btn' "
                + "type='submit'>Search</button>");

        out.println("</form>");

        out.println("</div>");

        boolean found = false;

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            int parameterIndex = 1;

            if (normalSearch) {

                String searchValue =
                        "%" + search.trim() + "%";

                ps.setString(
                        parameterIndex,
                        searchValue);

                parameterIndex++;

                ps.setString(
                        parameterIndex,
                        searchValue);

                parameterIndex++;

                ps.setString(
                        parameterIndex,
                        searchValue);

                parameterIndex++;
            }

            if (!category.trim().isEmpty()) {

                ps.setString(
                        parameterIndex,
                        category);
            }

            ResultSet rs =
                    ps.executeQuery();

            out.println("<div class='results-title'>");

            out.println("Available Food Items");

            out.println("</div>");

            out.println("<div class='food-container'>");

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
                            "images/margherita-pizza.jpg";
                }

                out.println("<div class='food-card'>");

                out.println("<img class='food-image' "
                        + "src='"
                        + imageUrl
                        + "' "
                        + "alt='"
                        + foodName
                        + "'>");

                out.println("<div class='food-info'>");

                out.println("<h3>"
                        + foodName
                        + "</h3>");

                out.println("<div class='restaurant'>");

                out.println("🏪 "
                        + restaurantName);

                out.println("</div>");

                out.println("<span class='category-text'>"
                        + foodCategory
                        + "</span>");

                out.println("<p class='description'>"
                        + description
                        + "</p>");

                out.println("<div class='price'>₹"
                        + String.format(
                                "%.2f",
                                price)
                        + "</div>");

                out.println("<a class='cart-btn' "
                        + "href='cart?foodId="
                        + foodId
                        + "'>");

                out.println("🛒 Add to Cart");

                out.println("</a>");

                out.println("</div>");

                out.println("</div>");
            }

            out.println("</div>");

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<div class='no-results'>");

            out.println("<h2>Something went wrong</h2>");

            out.println("<p>Unable to load food items.</p>");

            out.println("</div>");
        }

        if (!found) {

            out.println("<div class='no-results'>");

            out.println("<h2>😔 No Food Found</h2>");

            out.println("<p>");

            out.println(
                    "We couldn't find any food matching your search.");

            out.println("</p>");

            out.println("<a class='back-btn' href='foods'>");

            out.println("View All Food Items");

            out.println("</a>");

            out.println("</div>");
        }

        out.println("<footer>");

        out.println(
                "© 2026 MealHub Management System. "
                + "All Rights Reserved.");

        out.println("</footer>");

        out.println("</body>");

        out.println("</html>");
    }
}
