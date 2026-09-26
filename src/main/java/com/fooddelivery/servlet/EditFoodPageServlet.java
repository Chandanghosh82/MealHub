
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

@WebServlet("/editFoodPage")
public class EditFoodPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String foodIdText = request.getParameter("foodId");

        try {

            int foodId = Integer.parseInt(foodIdText);

            Connection con = DBConnection.getConnection();

            String sql = "SELECT food_id, restaurant_id, food_name, "
                    + "category, price, description "
                    + "FROM food_items "
                    + "WHERE food_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, foodId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int restaurantId = rs.getInt("restaurant_id");
                String foodName = rs.getString("food_name");
                String category = rs.getString("category");
                String price = rs.getBigDecimal("price").toString();
                String description = rs.getString("description");

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println("<title>Edit Food Item</title>");

                out.println("<style>");

                out.println("body {");
                out.println("margin: 0;");
                out.println("font-family: Arial, sans-serif;");
                out.println("background: #f5f5f5;");
                out.println("}");

                out.println(".navbar {");
                out.println("background: #ff5722;");
                out.println("color: white;");
                out.println("padding: 20px;");
                out.println("text-align: center;");
                out.println("}");

                out.println(".navbar h2 {");
                out.println("margin: 0;");
                out.println("}");

                out.println(".container {");
                out.println("width: 500px;");
                out.println("margin: 40px auto;");
                out.println("background: white;");
                out.println("padding: 35px;");
                out.println("border-radius: 12px;");
                out.println("box-shadow: 0 4px 12px rgba(0,0,0,0.12);");
                out.println("}");

                out.println("h1 {");
                out.println("text-align: center;");
                out.println("color: #333;");
                out.println("}");

                out.println("label {");
                out.println("display: block;");
                out.println("margin-top: 18px;");
                out.println("margin-bottom: 7px;");
                out.println("font-weight: bold;");
                out.println("color: #444;");
                out.println("}");

                out.println("input, select, textarea {");
                out.println("width: 100%;");
                out.println("padding: 12px;");
                out.println("border: 1px solid #ccc;");
                out.println("border-radius: 6px;");
                out.println("box-sizing: border-box;");
                out.println("font-size: 15px;");
                out.println("}");

                out.println("textarea {");
                out.println("height: 90px;");
                out.println("resize: none;");
                out.println("}");

                out.println("button {");
                out.println("width: 100%;");
                out.println("margin-top: 25px;");
                out.println("padding: 13px;");
                out.println("background: #2196F3;");
                out.println("color: white;");
                out.println("border: none;");
                out.println("border-radius: 6px;");
                out.println("font-size: 16px;");
                out.println("cursor: pointer;");
                out.println("}");

                out.println("button:hover {");
                out.println("background: #1976D2;");
                out.println("}");

                out.println(".back {");
                out.println("display: block;");
                out.println("text-align: center;");
                out.println("margin-top: 20px;");
                out.println("color: #ff5722;");
                out.println("text-decoration: none;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println("</style>");

                out.println("</head>");

                out.println("<body>");

                out.println("<div class='navbar'>");
                out.println("<h2>Food Delivery - Admin Panel</h2>");
                out.println("</div>");

                out.println("<div class='container'>");

                out.println("<h1>Edit Food Item</h1>");

                out.println("<form action='updateFood' method='post'>");

                out.println("<input type='hidden' "
                        + "name='foodId' "
                        + "value='" + foodId + "'>");

                out.println("<label>Restaurant</label>");

                out.println("<select name='restaurantId' required>");

                out.println("<option value=''>Select Restaurant</option>");

                String restaurantSql =
                        "SELECT restaurant_id, restaurant_name "
                        + "FROM restaurants "
                        + "ORDER BY restaurant_name";

                PreparedStatement restaurantPs =
                        con.prepareStatement(restaurantSql);

                ResultSet restaurantRs =
                        restaurantPs.executeQuery();

                while (restaurantRs.next()) {

                    int id = restaurantRs.getInt("restaurant_id");

                    String name =
                            restaurantRs.getString("restaurant_name");

                    if (id == restaurantId) {

                        out.println("<option value='" + id
                                + "' selected>"
                                + name
                                + "</option>");

                    } else {

                        out.println("<option value='" + id
                                + "'>"
                                + name
                                + "</option>");
                    }
                }

                restaurantRs.close();
                restaurantPs.close();

                out.println("</select>");

                out.println("<label>Food Name</label>");

                out.println("<input type='text' "
                        + "name='foodName' "
                        + "value='" + foodName + "' "
                        + "required>");

                out.println("<label>Category</label>");

                out.println("<select name='category' required>");

                out.println("<option value='Pizza' "
                        + ("Pizza".equals(category) ? "selected" : "")
                        + ">Pizza</option>");

                out.println("<option value='Biryani' "
                        + ("Biryani".equals(category) ? "selected" : "")
                        + ">Biryani</option>");

                out.println("<option value='Burger' "
                        + ("Burger".equals(category) ? "selected" : "")
                        + ">Burger</option>");

                out.println("<option value='Chinese' "
                        + ("Chinese".equals(category) ? "selected" : "")
                        + ">Chinese</option>");

                out.println("<option value='South Indian' "
                        + ("South Indian".equals(category) ? "selected" : "")
                        + ">South Indian</option>");

                out.println("<option value='Dessert' "
                        + ("Dessert".equals(category) ? "selected" : "")
                        + ">Dessert</option>");

                out.println("<option value='Drinks' "
                        + ("Drinks".equals(category) ? "selected" : "")
                        + ">Drinks</option>");

                out.println("</select>");

                out.println("<label>Price</label>");

                out.println("<input type='number' "
                        + "name='price' "
                        + "step='0.01' "
                        + "value='" + price + "' "
                        + "required>");

                out.println("<label>Description</label>");

                out.println("<textarea name='description' "
                        + "required>"
                        + description
                        + "</textarea>");

                out.println("<button type='submit'>");
                out.println("Update Food Item");
                out.println("</button>");

                out.println("</form>");

                out.println("<a href='adminFoods' class='back'>");
                out.println("&larr; Back to Food Items");
                out.println("</a>");

                out.println("</div>");

                out.println("</body>");

                out.println("</html>");

            } else {

                out.println("<h2>Food item not found</h2>");
                out.println("<a href='adminFoods'>Back to Food Items</a>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Food ID</h2>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

        } catch (Exception e) {

            out.println("<h2>Error while loading food item</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

            e.printStackTrace();
        }
    }
}
