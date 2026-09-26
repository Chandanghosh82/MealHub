
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

@WebServlet("/adminOrders")
public class AdminOrdersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT o.order_id, u.name, o.order_date, "
                    + "o.total_amount, o.status "
                    + "FROM orders o "
                    + "LEFT JOIN users u ON o.user_id = u.user_id "
                    + "ORDER BY o.order_id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Admin Orders</title>");

            out.println("<style>");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: #f4f6f8;");
            out.println("padding: 30px;");
            out.println("}");

            out.println("h1 {");
            out.println("text-align: center;");
            out.println("}");

            out.println("table {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("background: white;");
            out.println("}");

            out.println("th, td {");
            out.println("padding: 12px;");
            out.println("border: 1px solid #ddd;");
            out.println("text-align: center;");
            out.println("}");

            out.println("th {");
            out.println("background: #222;");
            out.println("color: white;");
            out.println("}");

            out.println("select {");
            out.println("padding: 7px;");
            out.println("}");

            out.println("button {");
            out.println("padding: 7px 12px;");
            out.println("background: #007bff;");
            out.println("color: white;");
            out.println("border: none;");
            out.println("border-radius: 4px;");
            out.println("cursor: pointer;");
            out.println("}");

            out.println(".back {");
            out.println("display: inline-block;");
            out.println("margin-top: 20px;");
            out.println("text-decoration: none;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h1>Admin - Orders</h1>");

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Order ID</th>");
            out.println("<th>Customer</th>");
            out.println("<th>Order Date</th>");
            out.println("<th>Total Amount</th>");
            out.println("<th>Status</th>");
            out.println("<th>Update Status</th>");
            out.println("</tr>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int orderId = rs.getInt("order_id");

                String customerName = rs.getString("name");

                String orderDate = rs.getString("order_date");

                String totalAmount = rs.getString("total_amount");

                String currentStatus = rs.getString("status");

                out.println("<tr>");

                out.println("<td>" + orderId + "</td>");

                out.println("<td>" + customerName + "</td>");

                out.println("<td>" + orderDate + "</td>");

                out.println("<td>₹" + totalAmount + "</td>");

                out.println("<td>" + currentStatus + "</td>");

                out.println("<td>");

                out.println("<form action='updateOrderStatus' method='post'>");

                out.println("<input type='hidden' name='orderId' value='"
                        + orderId + "'>");

                out.println("<select name='status'>");

                out.println("<option value='PLACED' "
                        + (currentStatus.equals("PLACED") ? "selected" : "")
                        + ">PLACED</option>");

                out.println("<option value='CONFIRMED' "
                        + (currentStatus.equals("CONFIRMED") ? "selected" : "")
                        + ">CONFIRMED</option>");

                out.println("<option value='PREPARING' "
                        + (currentStatus.equals("PREPARING") ? "selected" : "")
                        + ">PREPARING</option>");

                out.println("<option value='OUT FOR DELIVERY' "
                        + (currentStatus.equals("OUT FOR DELIVERY") ? "selected" : "")
                        + ">OUT FOR DELIVERY</option>");

                out.println("<option value='DELIVERED' "
                        + (currentStatus.equals("DELIVERED") ? "selected" : "")
                        + ">DELIVERED</option>");

                out.println("<option value='CANCELLED' "
                        + (currentStatus.equals("CANCELLED") ? "selected" : "")
                        + ">CANCELLED</option>");

                out.println("</select>");

                out.println("<button type='submit'>Update</button>");

                out.println("</form>");

                out.println("</td>");

                out.println("</tr>");
            }

            if (!found) {

                out.println("<tr>");
                out.println("<td colspan='6'>No orders found</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            out.println("<a href='admin.html' class='back'>← Back to Dashboard</a>");

            out.println("</body>");

            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<h2>Error while loading orders</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("<a href='admin.html'>Back to Dashboard</a>");

            e.printStackTrace();
        }
    }
}
