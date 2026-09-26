
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

@WebServlet("/cancelOrder")
public class CancelOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String orderIdText = request.getParameter("orderId");

        if (orderIdText == null) {

            response.sendRedirect("myOrders");
            return;
        }

        try {

            int orderId = Integer.parseInt(orderIdText);

            Connection con = DBConnection.getConnection();

            String checkSql = "SELECT status FROM orders "
                    + "WHERE order_id = ? AND user_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setInt(1, orderId);
            checkPs.setInt(2, userId);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {

                rs.close();
                checkPs.close();
                con.close();

                response.sendRedirect("myOrders");
                return;
            }

            String status = rs.getString("status");

            rs.close();
            checkPs.close();

            if ("PLACED".equals(status) || "CONFIRMED".equals(status)) {

                String updateSql = "UPDATE orders "
                        + "SET status = 'CANCELLED' "
                        + "WHERE order_id = ? AND user_id = ?";

                PreparedStatement updatePs = con.prepareStatement(updateSql);

                updatePs.setInt(1, orderId);
                updatePs.setInt(2, userId);

                updatePs.executeUpdate();

                updatePs.close();
            }

            con.close();

            response.sendRedirect("myOrders");

        } catch (NumberFormatException e) {

            response.sendRedirect("myOrders");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("myOrders");
        }
    }
}
