
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

@WebServlet("/addFood")
public class AddFoodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String restaurantIdText = request.getParameter("restaurantId");
        String foodName = request.getParameter("foodName");
        String category = request.getParameter("category");
        String priceText = request.getParameter("price");
        String description = request.getParameter("description");

        try {

            if (foodName == null || foodName.trim().isEmpty()) {
                out.println("<h2>Food name is required</h2>");
                out.println("<a href='addFoodPage'>Go Back</a>");
                return;
            }

            int restaurantId = Integer.parseInt(restaurantIdText);

            BigDecimal price = new BigDecimal(priceText);

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                out.println("<h2>Price must be greater than 0</h2>");
                out.println("<a href='addFoodPage'>Go Back</a>");
                return;
            }

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO food_items "
                    + "(restaurant_id, food_name, category, price, description) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, restaurantId);
            ps.setString(2, foodName.trim());
            ps.setString(3, category);
            ps.setBigDecimal(4, price);
            ps.setString(5, description);

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                out.println("<html>");
                out.println("<head>");
                out.println("<title>Food Added</title>");
                out.println("</head>");
                out.println("<body>");

                out.println("<h2>Food Item Added Successfully!</h2>");

                out.println("<a href='adminFoods'>View Food Items</a>");

                out.println("</body>");
                out.println("</html>");

            }

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Restaurant ID or Price</h2>");
            out.println("<p>Please enter valid numeric values.</p>");
            out.println("<a href='addFoodPage'>Go Back</a>");

        } catch (Exception e) {

            out.println("<h2>Error while adding food item</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='addFoodPage'>Go Back</a>");

            e.printStackTrace();
        }
    }
}

