
package com.fooddelivery.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fooddelivery.util.DBConnection;

@WebServlet("/removeCart")
public class RemoveCartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        String foodIdText = request.getParameter("foodId");

        if (foodIdText == null || foodIdText.trim().isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        try {

            int foodId = Integer.parseInt(foodIdText);

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM cart "
                    + "WHERE user_id = ? AND food_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, foodId);

            ps.executeUpdate();

            ps.close();
            con.close();

            response.sendRedirect("cart");

        } catch (NumberFormatException e) {

            response.sendRedirect("cart");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("cart");
        }
    }
}
