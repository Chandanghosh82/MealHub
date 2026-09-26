
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fooddelivery.util.DBConnection;

@WebServlet("/updateFood")
public class UpdateFoodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String foodIdText = request.getParameter("foodId");
        String restaurantIdText = request.getParameter("restaurantId");
        String foodName = request.getParameter("foodName");
        String category = request.getParameter("category");
        String priceText = request.getParameter("price");
        String description = request.getParameter("description");

        try {

            if (foodName == null || foodName.trim().isEmpty()) {
                out.println("<h2>Food name is required</h2>");
                out.println("<a href='adminFoods'>Go Back</a>");
                return;
            }

            int foodId = Integer.parseInt(foodIdText);

            int restaurantId = Integer.parseInt(restaurantIdText);

            BigDecimal price = new BigDecimal(priceText);

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                out.println("<h2>Price must be greater than 0</h2>");
                out.println("<a href='adminFoods'>Go Back</a>");
                return;
            }

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE food_items "
                    + "SET restaurant_id = ?, "
                    + "food_name = ?, "
                    + "category = ?, "
                    + "price = ?, "
                    + "description = ? "
                    + "WHERE food_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, restaurantId);
            ps.setString(2, foodName.trim());
            ps.setString(3, category);
            ps.setBigDecimal(4, price);
            ps.setString(5, description);
            ps.setInt(6, foodId);

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                response.sendRedirect("adminFoods");

            } else {

                out.println("<h2>Food item not found</h2>");
                out.println("<a href='adminFoods'>Back to Food Items</a>");
            }

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Input</h2>");
            out.println("<p>Please enter valid numeric values.</p>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

        } catch (Exception e) {

            out.println("<h2>Error while updating food item</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

            e.printStackTrace();
        }
    }
}

