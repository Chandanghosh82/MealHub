
package com.fooddelivery.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fooddelivery.util.DBConnection;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {

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
        String action = request.getParameter("action");

        if (foodIdText == null || action == null) {

            response.sendRedirect("cart");
            return;
        }

        try {

            int foodId = Integer.parseInt(foodIdText);

            Connection con = DBConnection.getConnection();

            String selectSql = "SELECT quantity FROM cart "
                    + "WHERE user_id = ? AND food_id = ?";

            PreparedStatement selectPs = con.prepareStatement(selectSql);

            selectPs.setInt(1, userId);
            selectPs.setInt(2, foodId);

            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {

                int quantity = rs.getInt("quantity");

                if ("increase".equals(action)) {

                    quantity++;

                } else if ("decrease".equals(action)) {

                    quantity--;

                }

                if (quantity <= 0) {

                    String deleteSql = "DELETE FROM cart "
                            + "WHERE user_id = ? AND food_id = ?";

                    PreparedStatement deletePs =
                            con.prepareStatement(deleteSql);

                    deletePs.setInt(1, userId);
                    deletePs.setInt(2, foodId);

                    deletePs.executeUpdate();

                    deletePs.close();

                } else {

                    String updateSql = "UPDATE cart "
                            + "SET quantity = ? "
                            + "WHERE user_id = ? AND food_id = ?";

                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql);

                    updatePs.setInt(1, quantity);
                    updatePs.setInt(2, userId);
                    updatePs.setInt(3, foodId);

                    updatePs.executeUpdate();

                    updatePs.close();
                }
            }

            rs.close();
            selectPs.close();
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
