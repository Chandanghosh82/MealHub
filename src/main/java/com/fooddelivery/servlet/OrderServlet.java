
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

@WebServlet("/placeOrder")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String UPI_ID =
            "chandanghosh8340@okaxis";

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out =
                response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");

            return;
        }

        Integer userId =
                (Integer) session.getAttribute("userId");

        Connection con = null;

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            con =
                    DBConnection.getConnection();

            /*
             * GET CART TOTAL
             */

            String sql =
                    "SELECT c.food_id, c.quantity, " +
                    "f.price, f.food_name " +
                    "FROM cart c " +
                    "JOIN food_items f " +
                    "ON c.food_id = f.food_id " +
                    "WHERE c.user_id = ?";

            ps =
                    con.prepareStatement(sql);

            ps.setInt(1, userId);

            rs =
                    ps.executeQuery();

            double total = 0;

            boolean cartFound = false;

            boolean hasBurger = false;

            boolean hasBiryani = false;

            while (rs.next()) {

                cartFound = true;

                double price =
                        rs.getDouble("price");

                int quantity =
                        rs.getInt("quantity");

                String foodName =
                        rs.getString("food_name");

                total +=
                        price * quantity;

                String lowerName =
                        foodName.toLowerCase();

                if (lowerName.contains("burger")) {
                    hasBurger = true;
                }

                if (lowerName.contains("biryani")) {
                    hasBiryani = true;
                }
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (!cartFound || total <= 0) {

                out.println("<html>");

                out.println("<body style='font-family:Arial;text-align:center;padding:80px'>");

                out.println("<h2>Your cart is empty</h2>");

                out.println("<a href='foods'>Browse Food</a>");

                out.println("</body>");

                out.println("</html>");

                return;
            }

            /*
             * COUPON
             */

            String coupon =
                    (String) session.getAttribute(
                            "appliedCoupon");

            double discount = 0;

            String couponMessage = "";

            if (coupon != null) {

                /*
                 * FIRST50
                 */

                if ("FIRST50".equals(coupon)) {

                    String orderCheckSql =
                            "SELECT COUNT(*) FROM orders WHERE user_id = ?";

                    PreparedStatement orderCheckPs =
                            con.prepareStatement(
                                    orderCheckSql);

                    orderCheckPs.setInt(1, userId);

                    ResultSet orderCheckRs =
                            orderCheckPs.executeQuery();

                    int orderCount = 0;

                    if (orderCheckRs.next()) {

                        orderCount =
                                orderCheckRs.getInt(1);
                    }

                    orderCheckRs.close();

                    orderCheckPs.close();

                    if (orderCount == 0) {

                        discount =
                                total * 0.50;

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }

                }

                /*
                 * SAVE100
                 */

                else if ("SAVE100".equals(coupon)) {

                    if (total >= 499) {

                        discount = 100;

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }
                }

                /*
                 * BURGER30
                 */

                else if ("BURGER30".equals(coupon)) {

                    if (hasBurger) {

                        double burgerTotal = 0;

                        String burgerSql =
                                "SELECT c.quantity, f.price, f.food_name " +
                                "FROM cart c " +
                                "JOIN food_items f " +
                                "ON c.food_id = f.food_id " +
                                "WHERE c.user_id = ?";

                        PreparedStatement burgerPs =
                                con.prepareStatement(
                                        burgerSql);

                        burgerPs.setInt(1, userId);

                        ResultSet burgerRs =
                                burgerPs.executeQuery();

                        while (burgerRs.next()) {

                            String name =
                                    burgerRs.getString(
                                            "food_name");

                            if (name.toLowerCase()
                                    .contains("burger")) {

                                burgerTotal +=
                                        burgerRs.getDouble("price")
                                        *
                                        burgerRs.getInt("quantity");
                            }
                        }

                        burgerRs.close();

                        burgerPs.close();

                        discount =
                                burgerTotal * 0.30;

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }
                }

                /*
                 * BIRYANI20
                 */

                else if ("BIRYANI20".equals(coupon)) {

                    if (hasBiryani) {

                        double biryaniTotal = 0;

                        String biryaniSql =
                                "SELECT c.quantity, f.price, f.food_name " +
                                "FROM cart c " +
                                "JOIN food_items f " +
                                "ON c.food_id = f.food_id " +
                                "WHERE c.user_id = ?";

                        PreparedStatement biryaniPs =
                                con.prepareStatement(
                                        biryaniSql);

                        biryaniPs.setInt(1, userId);

                        ResultSet biryaniRs =
                                biryaniPs.executeQuery();

                        while (biryaniRs.next()) {

                            String name =
                                    biryaniRs.getString(
                                            "food_name");

                            if (name.toLowerCase()
                                    .contains("biryani")) {

                                biryaniTotal +=
                                        biryaniRs.getDouble("price")
                                        *
                                        biryaniRs.getInt("quantity");
                            }
                        }

                        biryaniRs.close();

                        biryaniPs.close();

                        discount =
                                biryaniTotal * 0.20;

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }
                }

                /*
                 * NEW50
                 */

                else if ("NEW50".equals(coupon)) {

                    if (total >= 199) {

                        discount = 50;

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }
                }

                /*
                 * LIMITED
                 */

                else if ("LIMITED".equals(coupon)) {

                    if (total >= 299) {

                        discount = 0;

                        couponMessage =
                                "Free delivery applied";

                    } else {

                        session.removeAttribute(
                                "appliedCoupon");

                        coupon = null;
                    }
                }

                /*
                 * INVALID
                 */

                else {

                    session.removeAttribute(
                            "appliedCoupon");

                    coupon = null;
                }
            }

            if (discount > total) {

                discount = total;
            }

            double finalTotal =
                    total - discount;

            /*
             * PAYMENT AMOUNT
             */

            String amount =
                    String.format("%.2f",
                            finalTotal);

            String upiLink =
                    "upi://pay?pa=" +
                    URLEncoder.encode(
                            UPI_ID,
                            StandardCharsets.UTF_8) +
                    "&pn=" +
                    URLEncoder.encode(
                            "MealHub",
                            StandardCharsets.UTF_8) +
                    "&am=" +
                    URLEncoder.encode(
                            amount,
                            StandardCharsets.UTF_8) +
                    "&cu=INR";

            String qrUrl =
                    "https://api.qrserver.com/v1/create-qr-code/?size=280x280&data=" +
                    URLEncoder.encode(
                            upiLink,
                            StandardCharsets.UTF_8);

            /*
             * HTML
             */

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

            out.println("<title>MealHub Checkout</title>");

            out.println("<style>");

            out.println("*{");

            out.println("box-sizing:border-box;");

            out.println("margin:0;");

            out.println("padding:0;");

            out.println("}");

            out.println("body{");

            out.println("font-family:Arial,Helvetica,sans-serif;");

            out.println("background:#f5f7f9;");

            out.println("color:#20252b;");

            out.println("min-height:100vh;");

            out.println("}");

            out.println(".topbar{");

            out.println("height:70px;");

            out.println("background:#151a1f;");

            out.println("display:flex;");

            out.println("align-items:center;");

            out.println("padding:0 7%;");

            out.println("}");

            out.println(".logo{");

            out.println("font-size:28px;");

            out.println("font-weight:700;");

            out.println("color:white;");

            out.println("}");

            out.println(".logo span{");

            out.println("color:#ff7a00;");

            out.println("}");

            out.println(".page{");

            out.println("max-width:1050px;");

            out.println("margin:45px auto;");

            out.println("padding:0 20px;");

            out.println("}");

            out.println(".heading{");

            out.println("margin-bottom:25px;");

            out.println("}");

            out.println(".heading h1{");

            out.println("font-size:32px;");

            out.println("margin-bottom:7px;");

            out.println("}");

            out.println(".heading p{");

            out.println("color:#70777f;");

            out.println("font-size:15px;");

            out.println("}");

            out.println(".checkout{");

            out.println("display:grid;");

            out.println("grid-template-columns:1.25fr .75fr;");

            out.println("gap:25px;");

            out.println("}");

            out.println(".card{");

            out.println("background:white;");

            out.println("border:1px solid #e5e8eb;");

            out.println("border-radius:18px;");

            out.println("box-shadow:0 8px 25px rgba(0,0,0,0.06);");

            out.println("padding:28px;");

            out.println("}");

            out.println(".card-title{");

            out.println("font-size:20px;");

            out.println("font-weight:700;");

            out.println("margin-bottom:20px;");

            out.println("}");

            out.println(".payment-option{");

            out.println("display:block;");

            out.println("border:1px solid #dfe3e7;");

            out.println("border-radius:14px;");

            out.println("padding:18px;");

            out.println("margin-bottom:13px;");

            out.println("cursor:pointer;");

            out.println("transition:.2s;");

            out.println("}");

            out.println(".payment-option:hover{");

            out.println("border-color:#198754;");

            out.println("background:#f8fffb;");

            out.println("}");

            out.println(".payment-option input{");

            out.println("accent-color:#198754;");

            out.println("margin-right:10px;");

            out.println("}");

            out.println(".method-name{");

            out.println("font-size:16px;");

            out.println("font-weight:700;");

            out.println("}");

            out.println(".method-desc{");

            out.println("display:block;");

            out.println("font-size:13px;");

            out.println("color:#737a81;");

            out.println("margin-top:7px;");

            out.println("margin-left:25px;");

            out.println("}");

            out.println(".qr-section{");

            out.println("display:none;");

            out.println("margin-top:22px;");

            out.println("padding:22px;");

            out.println("border-radius:15px;");

            out.println("background:#f7f9fa;");

            out.println("border:1px dashed #cfd5da;");

            out.println("text-align:center;");

            out.println("}");

            out.println(".qr-section h3{");

            out.println("margin-bottom:8px;");

            out.println("}");

            out.println(".qr-section p{");

            out.println("font-size:13px;");

            out.println("color:#737a81;");

            out.println("margin:8px 0;");

            out.println("}");

            out.println(".qr-code{");

            out.println("width:280px;");

            out.println("height:280px;");

            out.println("max-width:100%;");

            out.println("margin:15px auto;");

            out.println("display:block;");

            out.println("background:white;");

            out.println("padding:8px;");

            out.println("border-radius:12px;");

            out.println("}");

            out.println(".upi-id{");

            out.println("display:inline-block;");

            out.println("background:#e8f7ee;");

            out.println("color:#157347;");

            out.println("font-weight:700;");

            out.println("padding:9px 14px;");

            out.println("border-radius:8px;");

            out.println("font-size:13px;");

            out.println("}");

            out.println(".security-note{");

            out.println("margin-top:14px;");

            out.println("font-size:12px;");

            out.println("color:#777;");

            out.println("line-height:1.5;");

            out.println("}");

            out.println(".summary-title{");

            out.println("font-size:18px;");

            out.println("font-weight:700;");

            out.println("margin-bottom:20px;");

            out.println("}");

            out.println(".summary-row{");

            out.println("display:flex;");

            out.println("justify-content:space-between;");

            out.println("padding:13px 0;");

            out.println("border-bottom:1px solid #eee;");

            out.println("font-size:14px;");

            out.println("}");

            out.println(".discount-row{");

            out.println("color:#198754;");

            out.println("font-weight:700;");

            out.println("}");

            out.println(".summary-total{");

            out.println("display:flex;");

            out.println("justify-content:space-between;");

            out.println("font-size:25px;");

            out.println("font-weight:700;");

            out.println("color:#198754;");

            out.println("padding-top:20px;");

            out.println("}");

            out.println(".confirm-btn{");

            out.println("width:100%;");

            out.println("border:none;");

            out.println("background:#198754;");

            out.println("color:white;");

            out.println("padding:15px;");

            out.println("border-radius:10px;");

            out.println("font-size:16px;");

            out.println("font-weight:700;");

            out.println("cursor:pointer;");

            out.println("margin-top:25px;");

            out.println("}");

            out.println(".confirm-btn:hover{");

            out.println("background:#157347;");

            out.println("}");

            out.println(".back-btn{");

            out.println("display:block;");

            out.println("text-align:center;");

            out.println("margin-top:17px;");

            out.println("color:#555;");

            out.println("text-decoration:none;");

            out.println("font-size:14px;");

            out.println("}");

            out.println(".back-btn:hover{");

            out.println("color:#198754;");

            out.println("}");

            out.println(".payment-badge{");

            out.println("display:inline-block;");

            out.println("background:#fff4e8;");

            out.println("color:#d96500;");

            out.println("padding:6px 10px;");

            out.println("border-radius:20px;");

            out.println("font-size:12px;");

            out.println("font-weight:bold;");

            out.println("margin-top:10px;");

            out.println("}");

            out.println("@media(max-width:800px){");

            out.println(".checkout{");

            out.println("grid-template-columns:1fr;");

            out.println("}");

            out.println(".page{");

            out.println("margin:30px auto;");

            out.println("}");

            out.println(".heading h1{");

            out.println("font-size:27px;");

            out.println("}");

            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            /*
             * TOP BAR
             */

            out.println("<div class='topbar'>");

            out.println("<div class='logo'>Meal<span>Hub</span></div>");

            out.println("</div>");

            out.println("<div class='page'>");

            out.println("<div class='heading'>");

            out.println("<h1>Checkout</h1>");

            out.println("<p>Choose your preferred payment method to complete your order.</p>");

            out.println("</div>");

            out.println("<form method='post' action='placeOrder' id='paymentForm'>");

            out.println("<div class='checkout'>");

            /*
             * PAYMENT CARD
             */

            out.println("<div class='card'>");

            out.println("<div class='card-title'>💳 Payment Method</div>");

            out.println("<label class='payment-option'>");

            out.println("<input type='radio' name='paymentMethod' "
                    + "value='CASH ON DELIVERY' "
                    + "checked onclick='hideQR()'>");

            out.println("<span class='method-name'>💵 Cash on Delivery</span>");

            out.println("<span class='method-desc'>"
                    + "Pay securely when your order is delivered."
                    + "</span>");

            out.println("</label>");

            out.println("<label class='payment-option'>");

            out.println("<input type='radio' name='paymentMethod' "
                    + "value='GOOGLE PAY' "
                    + "onclick='showQR()'>");

            out.println("<span class='method-name'>🟢 Google Pay</span>");

            out.println("<span class='method-desc'>"
                    + "Scan the QR code using Google Pay."
                    + "</span>");

            out.println("</label>");

            out.println("<label class='payment-option'>");

            out.println("<input type='radio' name='paymentMethod' "
                    + "value='PHONEPE' "
                    + "onclick='showQR()'>");

            out.println("<span class='method-name'>🟣 PhonePe</span>");

            out.println("<span class='method-desc'>"
                    + "Scan the QR code using PhonePe."
                    + "</span>");

            out.println("</label>");

            /*
             * QR
             */

            out.println("<div class='qr-section' id='qrSection'>");

            out.println("<h3>Scan to Pay</h3>");

            out.println("<p>"
                    + "Scan this QR code using your selected UPI app."
                    + "</p>");

            out.println("<img class='qr-code' "
                    + "src='" + qrUrl + "' "
                    + "alt='MealHub UPI QR Code'>");

            out.println("<div class='upi-id'>"
                    + "UPI ID: " + UPI_ID
                    + "</div>");

            out.println("<div class='security-note'>");

            out.println("Amount to pay: <b>₹"
                    + amount
                    + "</b><br>");

            out.println("After completing the payment, select the confirmation option below.");

            out.println("</div>");

            out.println("</div>");

            out.println("</div>");

            /*
             * SUMMARY
             */

            out.println("<div class='card'>");

            out.println("<div class='summary-title'>Order Summary</div>");

            out.println("<div class='summary-row'>");

            out.println("<span>Order Amount</span>");

            out.println("<span>₹"
                    + String.format("%.2f", total)
                    + "</span>");

            out.println("</div>");

            if (discount > 0) {

                out.println("<div class='summary-row discount-row'>");

                out.println("<span>Coupon Discount");

                if (coupon != null) {

                    out.println(" (" + coupon + ")");

                }

                out.println("</span>");

                out.println("<span>- ₹"
                        + String.format("%.2f", discount)
                        + "</span>");

                out.println("</div>");
            }

            out.println("<div class='summary-row'>");

            out.println("<span>Delivery</span>");

            out.println("<span>FREE</span>");

            out.println("</div>");

            out.println("<div class='summary-row'>");

            out.println("<span>Payment</span>");

            out.println("<span id='paymentLabel'>Cash on Delivery</span>");

            out.println("</div>");

            out.println("<div class='summary-total'>");

            out.println("<span>Total</span>");

            out.println("<span>₹"
                    + String.format("%.2f", finalTotal)
                    + "</span>");

            out.println("</div>");

            out.println("<div class='payment-badge'>🔒 Secure Checkout</div>");

            out.println("<button type='submit' "
                    + "class='confirm-btn' "
                    + "id='confirmBtn'>");

            out.println("Confirm Order");

            out.println("</button>");

            out.println("<a href='cart' class='back-btn'>"
                    + "← Back to Cart"
                    + "</a>");

            out.println("</div>");

            out.println("</div>");

            out.println("</form>");

            out.println("</div>");

            /*
             * JAVASCRIPT
             */

            out.println("<script>");

            out.println("function showQR(){");

            out.println("document.getElementById('qrSection').style.display='block';");

            out.println("document.getElementById('paymentLabel').innerText='Online Payment';");

            out.println("document.getElementById('confirmBtn').innerText='I Have Completed Payment';");

            out.println("}");

            out.println("function hideQR(){");

            out.println("document.getElementById('qrSection').style.display='none';");

            out.println("document.getElementById('paymentLabel').innerText='Cash on Delivery';");

            out.println("document.getElementById('confirmBtn').innerText='Confirm Order';");

            out.println("}");

            out.println("</script>");

            out.println("</body>");

            out.println("</html>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Checkout Error</h2>");

            out.println("<p>"
                    + e.getMessage()
                    + "</p>");

            out.println("<a href='cart'>Back to Cart</a>");

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
            }

            try {

                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
            }

            try {

                if (con != null &&
                    !con.isClosed()) {

                    con.close();
                }

            } catch (Exception e) {
            }
        }
    }

    /*
     * PLACE ORDER
     */

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "text/html;charset=UTF-8");

        PrintWriter out =
                response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");

            return;
        }

        Integer userId =
                (Integer) session.getAttribute("userId");

        String paymentMethod =
                request.getParameter(
                        "paymentMethod");

        if (paymentMethod == null ||
            paymentMethod.trim().isEmpty()) {

            paymentMethod =
                    "CASH ON DELIVERY";
        }

        boolean onlinePayment =
                "GOOGLE PAY".equals(paymentMethod)
                ||
                "PHONEPE".equals(paymentMethod);

        String paymentCompleted =
                request.getParameter(
                        "paymentCompleted");

        Connection con = null;

        try {

            con =
                    DBConnection.getConnection();

            con.setAutoCommit(false);

            /*
             * GET CART
             */

            String cartSql =
                    "SELECT c.food_id, c.quantity, " +
                    "f.price, f.food_name " +
                    "FROM cart c " +
                    "JOIN food_items f " +
                    "ON c.food_id = f.food_id " +
                    "WHERE c.user_id = ?";

            PreparedStatement cartPs =
                    con.prepareStatement(cartSql);

            cartPs.setInt(1, userId);

            ResultSet cartRs =
                    cartPs.executeQuery();

            double total = 0;

            boolean cartFound = false;

            boolean hasBurger = false;

            boolean hasBiryani = false;

            while (cartRs.next()) {

                cartFound = true;

                double price =
                        cartRs.getDouble("price");

                int quantity =
                        cartRs.getInt("quantity");

                String foodName =
                        cartRs.getString("food_name");

                total +=
                        price * quantity;

                String lowerName =
                        foodName.toLowerCase();

                if (lowerName.contains("burger")) {
                    hasBurger = true;
                }

                if (lowerName.contains("biryani")) {
                    hasBiryani = true;
                }
            }

            cartRs.close();

            cartPs.close();

            if (!cartFound || total <= 0) {

                con.rollback();

                out.println("<h2>Your cart is empty</h2>");

                out.println("<a href='cart'>Back to Cart</a>");

                return;
            }

            /*
             * APPLY COUPON AGAIN
             */

            String coupon =
                    (String) session.getAttribute(
                            "appliedCoupon");

            double discount = 0;

            if (coupon != null) {

                /*
                 * FIRST50
                 */

                if ("FIRST50".equals(coupon)) {

                    String orderCheckSql =
                            "SELECT COUNT(*) FROM orders WHERE user_id = ?";

                    PreparedStatement orderCheckPs =
                            con.prepareStatement(
                                    orderCheckSql);

                    orderCheckPs.setInt(1, userId);

                    ResultSet orderCheckRs =
                            orderCheckPs.executeQuery();

                    int orderCount = 0;

                    if (orderCheckRs.next()) {

                        orderCount =
                                orderCheckRs.getInt(1);
                    }

                    orderCheckRs.close();

                    orderCheckPs.close();

                    if (orderCount == 0) {

                        discount =
                                total * 0.50;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * SAVE100
                 */

                else if ("SAVE100".equals(coupon)) {

                    if (total >= 499) {

                        discount = 100;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * BURGER30
                 */

                else if ("BURGER30".equals(coupon)) {

                    if (hasBurger) {

                        double burgerTotal = 0;

                        String burgerSql =
                                "SELECT c.quantity, f.price, f.food_name " +
                                "FROM cart c " +
                                "JOIN food_items f " +
                                "ON c.food_id = f.food_id " +
                                "WHERE c.user_id = ?";

                        PreparedStatement burgerPs =
                                con.prepareStatement(
                                        burgerSql);

                        burgerPs.setInt(1, userId);

                        ResultSet burgerRs =
                                burgerPs.executeQuery();

                        while (burgerRs.next()) {

                            String name =
                                    burgerRs.getString(
                                            "food_name");

                            if (name.toLowerCase()
                                    .contains("burger")) {

                                burgerTotal +=
                                        burgerRs.getDouble(
                                                "price")
                                        *
                                        burgerRs.getInt(
                                                "quantity");
                            }
                        }

                        burgerRs.close();

                        burgerPs.close();

                        discount =
                                burgerTotal * 0.30;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * BIRYANI20
                 */

                else if ("BIRYANI20".equals(coupon)) {

                    if (hasBiryani) {

                        double biryaniTotal = 0;

                        String biryaniSql =
                                "SELECT c.quantity, f.price, f.food_name " +
                                "FROM cart c " +
                                "JOIN food_items f " +
                                "ON c.food_id = f.food_id " +
                                "WHERE c.user_id = ?";

                        PreparedStatement biryaniPs =
                                con.prepareStatement(
                                        biryaniSql);

                        biryaniPs.setInt(1, userId);

                        ResultSet biryaniRs =
                                biryaniPs.executeQuery();

                        while (biryaniRs.next()) {

                            String name =
                                    biryaniRs.getString(
                                            "food_name");

                            if (name.toLowerCase()
                                    .contains("biryani")) {

                                biryaniTotal +=
                                        biryaniRs.getDouble(
                                                "price")
                                        *
                                        biryaniRs.getInt(
                                                "quantity");
                            }
                        }

                        biryaniRs.close();

                        biryaniPs.close();

                        discount =
                                biryaniTotal * 0.20;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * NEW50
                 */

                else if ("NEW50".equals(coupon)) {

                    if (total >= 199) {

                        discount = 50;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * LIMITED
                 */

                else if ("LIMITED".equals(coupon)) {

                    if (total >= 299) {

                        discount = 0;

                    } else {

                        coupon = null;

                        session.removeAttribute(
                                "appliedCoupon");
                    }
                }

                /*
                 * INVALID
                 */

                else {

                    coupon = null;

                    session.removeAttribute(
                            "appliedCoupon");
                }
            }

            if (discount > total) {

                discount = total;
            }

            double finalTotal =
                    total - discount;

            /*
             * PAYMENT
             */

            String paymentStatus;

            if (onlinePayment) {

                if (!"YES".equals(
                        paymentCompleted)) {

                    con.rollback();

                    out.println("<html>");

                    out.println("<body style='font-family:Arial;text-align:center;padding:80px'>");

                    out.println("<h2>Payment Confirmation Required</h2>");

                    out.println("<p>Please complete the UPI payment and try again.</p>");

                    out.println("<a href='placeOrder'>Return to Payment</a>");

                    out.println("</body>");

                    out.println("</html>");

                    return;
                }

                paymentStatus = "PAID";

            } else {

                paymentStatus = "PENDING";
            }

            /*
             * INSERT ORDER
             */

            String orderSql =
                    "INSERT INTO orders " +
                    "(user_id, total_amount, status, payment_method, payment_status) " +
                    "VALUES (?, ?, 'PLACED', ?, ?)";

            PreparedStatement orderPs =
                    con.prepareStatement(
                            orderSql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            orderPs.setInt(1, userId);

            orderPs.setDouble(2, finalTotal);

            orderPs.setString(
                    3,
                    paymentMethod);

            orderPs.setString(
                    4,
                    paymentStatus);

            orderPs.executeUpdate();

            ResultSet keys =
                    orderPs.getGeneratedKeys();

            int orderId = 0;

            if (keys.next()) {

                orderId =
                        keys.getInt(1);
            }

            keys.close();

            orderPs.close();

            /*
             * INSERT ORDER ITEMS
             */

            String itemSql =
                    "SELECT c.food_id, c.quantity, f.price " +
                    "FROM cart c " +
                    "JOIN food_items f " +
                    "ON c.food_id = f.food_id " +
                    "WHERE c.user_id = ?";

            PreparedStatement itemPs =
                    con.prepareStatement(itemSql);

            itemPs.setInt(1, userId);

            ResultSet itemRs =
                    itemPs.executeQuery();

            String insertItemSql =
                    "INSERT INTO order_items " +
                    "(order_id, food_id, quantity, price) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement orderItemPs =
                    con.prepareStatement(
                            insertItemSql);

            while (itemRs.next()) {

                int foodId =
                        itemRs.getInt(
                                "food_id");

                int quantity =
                        itemRs.getInt(
                                "quantity");

                double price =
                        itemRs.getDouble(
                                "price");

                orderItemPs.setInt(
                        1,
                        orderId);

                orderItemPs.setInt(
                        2,
                        foodId);

                orderItemPs.setInt(
                        3,
                        quantity);

                orderItemPs.setDouble(
                        4,
                        price);

                orderItemPs.executeUpdate();
            }

            itemRs.close();

            itemPs.close();

            orderItemPs.close();

            /*
             * DELETE CART
             */

            String deleteCartSql =
                    "DELETE FROM cart WHERE user_id = ?";

            PreparedStatement deletePs =
                    con.prepareStatement(
                            deleteCartSql);

            deletePs.setInt(1, userId);

            deletePs.executeUpdate();

            deletePs.close();

            /*
             * REMOVE COUPON
             */

            session.removeAttribute(
                    "appliedCoupon");

            /*
             * COMMIT
             */

            con.commit();

            con.close();

            /*
             * SUCCESS PAGE
             */

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

            out.println("<title>Order Confirmed - MealHub</title>");

            out.println("<style>");

            out.println("body{");

            out.println("font-family:Arial,Helvetica,sans-serif;");

            out.println("background:#f5f7f9;");

            out.println("text-align:center;");

            out.println("padding:60px 20px;");

            out.println("}");

            out.println(".success-card{");

            out.println("max-width:520px;");

            out.println("margin:auto;");

            out.println("background:white;");

            out.println("padding:45px;");

            out.println("border-radius:22px;");

            out.println("box-shadow:0 10px 35px rgba(0,0,0,0.08);");

            out.println("}");

            out.println(".check{");

            out.println("width:70px;");

            out.println("height:70px;");

            out.println("border-radius:50%;");

            out.println("background:#e8f7ee;");

            out.println("color:#198754;");

            out.println("font-size:40px;");

            out.println("display:flex;");

            out.println("align-items:center;");

            out.println("justify-content:center;");

            out.println("margin:0 auto 20px;");

            out.println("}");

            out.println(".logo{");

            out.println("font-size:28px;");

            out.println("font-weight:bold;");

            out.println("margin-bottom:20px;");

            out.println("}");

            out.println(".logo span{");

            out.println("color:#ff7a00;");

            out.println("}");

            out.println("h1{");

            out.println("color:#198754;");

            out.println("margin-bottom:10px;");

            out.println("}");

            out.println(".order-id{");

            out.println("color:#666;");

            out.println("margin:10px 0;");

            out.println("}");

            out.println(".amount{");

            out.println("font-size:28px;");

            out.println("font-weight:bold;");

            out.println("margin:20px;");

            out.println("}");

            out.println(".discount{");

            out.println("color:#198754;");

            out.println("font-weight:bold;");

            out.println("margin:10px;");

            out.println("}");

            out.println(".payment-box{");

            out.println("background:#f5f7f9;");

            out.println("padding:16px;");

            out.println("border-radius:12px;");

            out.println("margin:20px 0;");

            out.println("line-height:1.7;");

            out.println("}");

            out.println(".paid{");

            out.println("color:#198754;");

            out.println("font-weight:bold;");

            out.println("}");

            out.println(".pending{");

            out.println("color:#d96500;");

            out.println("font-weight:bold;");

            out.println("}");

            out.println("a{");

            out.println("display:inline-block;");

            out.println("margin:7px;");

            out.println("padding:12px 20px;");

            out.println("background:#198754;");

            out.println("color:white;");

            out.println("text-decoration:none;");

            out.println("border-radius:9px;");

            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<div class='success-card'>");

            out.println("<div class='logo'>Meal<span>Hub</span></div>");

            out.println("<div class='check'>✓</div>");

            out.println("<h1>Order Confirmed!</h1>");

            out.println("<p>Thank you! Your order has been placed successfully.</p>");

            out.println("<div class='order-id'>Order ID: <b>"
                    + orderId
                    + "</b></div>");

            out.println("<div class='amount'>₹"
                    + String.format(
                            "%.2f",
                            finalTotal)
                    + "</div>");

            if (discount > 0) {

                out.println("<div class='discount'>"
                        + "🎁 You saved ₹"
                        + String.format(
                                "%.2f",
                                discount)
                        + "</div>");
            }

            out.println("<div class='payment-box'>");

            out.println("<b>Payment Method</b><br>");

            out.println(paymentMethod);

            out.println("<br><br>");

            out.println("<b>Payment Status</b><br>");

            if ("PAID".equals(
                    paymentStatus)) {

                out.println("<span class='paid'>✓ PAID</span>");

            } else {

                out.println("<span class='pending'>● PENDING</span>");
            }

            out.println("</div>");

            out.println("<a href='myOrders'>View My Orders</a>");

            out.println("<a href='foods'>Continue Shopping</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

        } catch (Exception e) {

            try {

                if (con != null) {

                    con.rollback();
                }

            } catch (Exception rollbackException) {

                rollbackException.printStackTrace();
            }

            e.printStackTrace();

            out.println("<html>");

            out.println("<body style='font-family:Arial;text-align:center;padding:80px'>");

            out.println("<h2>Order Failed</h2>");

            out.println("<p>"
                    + e.getMessage()
                    + "</p>");

            out.println("<a href='cart'>Back to Cart</a>");

            out.println("</body>");

            out.println("</html>");

        } finally {

            try {

                if (con != null &&
                    !con.isClosed()) {

                    con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
