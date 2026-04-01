<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.railway.model.Train" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Ticket — RailYatra</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;1,9..40,300&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="nav-brand">
        <span class="logo-icon">🚄</span>
        <span class="logo-text">RailYatra</span>
    </div>
    <div class="nav-links">
        <a href="index.html" class="nav-link">Home</a>
        <a href="mybookings.html" class="nav-link">My Bookings</a>
    </div>
</nav>

<%
    Train train = (Train) request.getAttribute("train");
    String travelClass = (String) request.getAttribute("travelClass");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    String date = (String) request.getAttribute("date");
    String error = request.getParameter("error");

    String availStatus = train.getAvailabilityStatus(travelClass);
    String availCls = train.getAvailabilityClass(travelClass);
    int price = train.getPrice(travelClass);
%>

<div class="page-container">
    <div class="page-header">
        <h1>Book Your Ticket</h1>
        <p>Complete passenger details to confirm booking</p>
    </div>

    <!-- Journey Summary -->
    <div class="journey-summary-card">
        <div class="journey-summary-header">
            <span class="train-badge">🚄 <%= train.getTrainName() %> — #<%= train.getTrainNumber() %></span>
        </div>
        <div class="journey-summary-body">
            <div class="journey-route">
                <div class="j-station">
                    <div class="j-time"><%= train.getDepartureTime() %></div>
                    <div class="j-city"><%= from %></div>
                </div>
                <div class="j-arrow">
                    <div class="j-duration"><%= train.getDuration() %></div>
                    <div class="j-line">→</div>
                </div>
                <div class="j-station">
                    <div class="j-time"><%= train.getArrivalTime() %></div>
                    <div class="j-city"><%= to %></div>
                </div>
            </div>
            <div class="journey-meta">
                <span class="j-date">📅 <%= date %></span>
                <span class="j-class class-badge">Class: <%= travelClass %></span>
                <span class="j-price">₹<%= price %></span>
                <span class="avail-badge avail-<%= availCls %>"><%= availStatus %></span>
            </div>
        </div>
    </div>

    <% if ("name".equals(error)) { %>
    <div class="error-banner">⚠ Please enter passenger name.</div>
    <% } %>

    <!-- Booking Form -->
    <div class="booking-form-card">
        <h2>Passenger Details</h2>
        <form action="book" method="post" class="booking-form">
            <input type="hidden" name="trainNumber" value="<%= train.getTrainNumber() %>">
            <input type="hidden" name="from" value="<%= from %>">
            <input type="hidden" name="to" value="<%= to %>">
            <input type="hidden" name="date" value="<%= date %>">

            <div class="form-group">
                <label for="passengerName">👤 Passenger Full Name</label>
                <input type="text" id="passengerName" name="passengerName"
                       class="form-input" placeholder="Enter full name as on ID"
                       required maxlength="100">
            </div>

            <div class="form-group">
                <label for="travelClass">🎫 Select Travel Class</label>
                <select name="travelClass" id="travelClass" class="form-select" onchange="updatePrice(this)">
                    <%
                        String[] allClasses = {"1A", "2A", "3A", "SL"};
                        String[] allLabels = {"1A — First AC", "2A — Second AC", "3A — Third AC", "SL — Sleeper"};
                        for (int i = 0; i < allClasses.length; i++) {
                            String cls = allClasses[i];
                            String lbl = allLabels[i];
                            String avl = train.getAvailabilityClass(cls);
                            String selected = cls.equals(travelClass) ? "selected" : "";
                            String disabled = avl.equals("notavailable") ? "disabled" : "";
                    %>
                    <option value="<%= cls %>" <%= selected %> <%= disabled %> data-price="<%= train.getPrice(cls) %>">
                        <%= lbl %> — ₹<%= train.getPrice(cls) %> (<%= train.getAvailabilityStatus(cls) %>)
                    </option>
                    <% } %>
                </select>
            </div>

            <div class="price-summary">
                <div class="price-row">
                    <span>Base Fare</span>
                    <span id="displayPrice">₹<%= price %></span>
                </div>
                <div class="price-row">
                    <span>Service Fee</span>
                    <span>₹0</span>
                </div>
                <div class="price-row total">
                    <span>Total Amount</span>
                    <span id="totalPrice">₹<%= price %></span>
                </div>
            </div>

            <button type="submit" class="search-btn confirm-btn">
                ✅ Confirm Booking
            </button>
        </form>
    </div>
</div>

<footer class="footer">
    <p>© 2025 RailYatra · Built with ❤️ for Indian Railways</p>
</footer>

<script>
function updatePrice(select) {
    const price = select.options[select.selectedIndex].getAttribute('data-price');
    document.getElementById('displayPrice').textContent = '₹' + price;
    document.getElementById('totalPrice').textContent = '₹' + price;
}
</script>
</body>
</html>
