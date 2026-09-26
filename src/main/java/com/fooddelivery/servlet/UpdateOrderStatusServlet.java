
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

@WebServlet("/updateOrderStatus")
public class UpdateOrderStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String orderIdText = request.getParameter("orderId");
        String status = request.getParameter("status");

        try {

            int orderId = Integer.parseInt(orderIdText);

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, orderId);

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                response.sendRedirect("adminOrders");

            } else {

                out.println("<h2>Order not found</h2>");
                out.println("<a href='adminOrders'>Back to Orders</a>");
            }

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Order ID</h2>");
            out.println("<a href='adminOrders'>Back to Orders</a>");

        } catch (Exception e) {

            out.println("<h2>Error while updating order status</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='adminOrders'>Back to Orders</a>");

            e.printStackTrace();
        }
    }
}
