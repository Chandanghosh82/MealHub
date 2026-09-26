
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

@WebServlet("/deleteFood")
public class DeleteFoodServlet extends HttpServlet {

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

            String sql = "DELETE FROM food_items WHERE food_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, foodId);

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

            out.println("<h2>Invalid Food ID</h2>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

        } catch (Exception e) {

            out.println("<h2>Error while deleting food item</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='adminFoods'>Back to Food Items</a>");

            e.printStackTrace();
        }
    }
}
