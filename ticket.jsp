<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.railway.model.Ticket" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket — RailYatra</title>
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
    Ticket ticket = (Ticket) request.getAttribute("ticket");
    String booked = request.getParameter("booked");
    String cancelled = request.getParameter("cancelled");
    String error = request.getParameter("error");
    String errorMsg = (String) request.getAttribute("error");
%>

<div class="page-container">

    <% if ("true".equals(booked)) { %>
    <div class="success-banner">
        🎉 Booking Confirmed! Your ticket has been booked successfully.
    </div>
    <% } %>

    <% if ("true".equals(cancelled)) { %>
    <div class="cancel-banner">
        ✅ Ticket Cancelled. Any waiting list passengers have been auto-upgraded.
    </div>
    <% } %>

    <% if (errorMsg != null) { %>
    <div class="error-banner"><%= errorMsg %></div>
    <% } %>

    <% if (ticket != null) { %>

    <!-- Real Ticket UI -->
    <div class="ticket-wrapper">
        <!-- Ticket Header -->
        <div class="ticket-header">
            <div class="ticket-brand">
                <span>🚄</span> RailYatra
            </div>
            <div class="ticket-pnr">
                <span class="pnr-label">PNR</span>
                <span class="pnr-number"><%= ticket.getPnr() %></span>
            </div>
        </div>

        <!-- Status Strip -->
        <%
            String statusClass = "status-confirmed";
            String statusLabel = "✅ CONFIRMED";
            if (ticket.isCancelled()) {
                statusClass = "status-cancelled";
                statusLabel = "❌ CANCELLED";
            } else if (ticket.isWaitlisted()) {
                statusClass = "status-wl";
                statusLabel = "⏳ WAITLISTED — " + ticket.getStatus();
            }
        %>
        <div class="ticket-status-strip <%= statusClass %>">
            <span><%= statusLabel %></span>
        </div>

        <!-- Ticket Body -->
        <div class="ticket-body">
            <div class="ticket-route">
                <div class="ticket-station">
                    <div class="station-time"><%= ticket.getDepartureTime() %></div>
                    <div class="station-name"><%= ticket.getFrom() %></div>
                </div>
                <div class="ticket-route-middle">
                    <div class="train-name-small"><%= ticket.getTrainName() %></div>
                    <div class="route-line">
                        <span class="dot"></span>
                        <div class="dashes"></div>
                        <span class="dot"></span>
                    </div>
                    <div class="route-duration"><%= ticket.getDuration() %></div>
                </div>
                <div class="ticket-station align-right">
                    <div class="station-time"><%= ticket.getArrivalTime() %></div>
                    <div class="station-name"><%= ticket.getTo() %></div>
                </div>
            </div>

            <!-- Divider with holes -->
            <div class="ticket-divider">
                <div class="hole left"></div>
                <div class="dashed-line"></div>
                <div class="hole right"></div>
            </div>

            <!-- Passenger Details -->
            <div class="ticket-details">
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Passenger</span>
                        <span class="detail-value"><%= ticket.getPassengerName() %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Train No.</span>
                        <span class="detail-value">#<%= ticket.getTrainNumber() %></span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Date</span>
                        <span class="detail-value"><%= ticket.getDate() %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Class</span>
                        <span class="detail-value class-highlight"><%= ticket.getTravelClass() %></span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Fare Paid</span>
                        <span class="detail-value price-highlight">₹<%= ticket.getPrice() %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Booked On</span>
                        <span class="detail-value"><%= ticket.getBookingTime() %></span>
                    </div>
                </div>
            </div>

            <!-- Barcode decorative -->
            <div class="ticket-barcode">
                <div class="barcode-bars"></div>
                <div class="barcode-text"><%= ticket.getPnr() %></div>
            </div>
        </div>
    </div>

    <!-- Actions -->
    <div class="ticket-actions">
        <% if (!ticket.isCancelled()) { %>
        <form action="cancel" method="post" onsubmit="return confirmCancel()">
            <input type="hidden" name="pnr" value="<%= ticket.getPnr() %>">
            <button type="submit" class="cancel-ticket-btn">
                🗑 Cancel Ticket
            </button>
        </form>
        <% } %>
        <a href="index.html" class="search-btn" style="text-decoration:none;">🏠 Book Another</a>
        <a href="mybookings.html" class="outline-btn" style="text-decoration:none;">📋 My Bookings</a>
    </div>

    <% } %>

</div>

<footer class="footer">
    <p>© 2025 RailYatra · Built with ❤️ for Indian Railways</p>
</footer>

<script>
function confirmCancel() {
    return confirm('Are you sure you want to cancel this ticket? This action cannot be undone.\n\nIf you are confirmed, the first waitlisted passenger will be auto-upgraded.');
}
</script>
</body>
</html>
