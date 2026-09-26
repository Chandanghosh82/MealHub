
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

@WebServlet("/addFoodPage")
public class AddFoodPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");
        out.println("<title>Add Food Item - Admin</title>");

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
        out.println("background: #ff5722;");
        out.println("color: white;");
        out.println("border: none;");
        out.println("border-radius: 6px;");
        out.println("font-size: 16px;");
        out.println("cursor: pointer;");
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

        out.println("<h1>Add Food Item</h1>");

        out.println("<form action='addFood' method='post'>");

        out.println("<label>Restaurant</label>");

        out.println("<select name='restaurantId' required>");

        out.println("<option value=''>Select Restaurant</option>");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT restaurant_id, restaurant_name "
                       + "FROM restaurants "
                       + "ORDER BY restaurant_name";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int restaurantId = rs.getInt("restaurant_id");

                String restaurantName =
                        rs.getString("restaurant_name");

                out.println("<option value='" + restaurantId + "'>"
                        + restaurantName
                        + "</option>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<option value=''>Error loading restaurants</option>");

            e.printStackTrace();
        }

        out.println("</select>");

        out.println("<label>Food Name</label>");

        out.println("<input type='text' "
                + "name='foodName' "
                + "placeholder='Enter Food Name' "
                + "required>");

        out.println("<label>Category</label>");

        out.println("<select name='category' required>");

        out.println("<option value=''>Select Category</option>");
        out.println("<option value='Pizza'>Pizza</option>");
        out.println("<option value='Biryani'>Biryani</option>");
        out.println("<option value='Burger'>Burger</option>");
        out.println("<option value='Chinese'>Chinese</option>");
        out.println("<option value='South Indian'>South Indian</option>");
        out.println("<option value='Dessert'>Dessert</option>");
        out.println("<option value='Drinks'>Drinks</option>");

        out.println("</select>");

        out.println("<label>Price</label>");

        out.println("<input type='number' "
                + "name='price' "
                + "step='0.01' "
                + "placeholder='Enter Price' "
                + "required>");

        out.println("<label>Description</label>");

        out.println("<textarea name='description' "
                + "placeholder='Enter food description' "
                + "required></textarea>");

        out.println("<button type='submit'>");
        out.println("Add Food Item");
        out.println("</button>");

        out.println("</form>");

        out.println("<a href='adminFoods' class='back'>");
        out.println("&larr; Back to Food Items");
        out.println("</a>");

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}
