<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.railway.model.Train" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Train Results — RailYatra</title>
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
    List<Train> trains = (List<Train>) request.getAttribute("trains");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    String date = (String) request.getAttribute("date");
%>

<div class="page-container">
    <!-- Route Banner -->
    <div class="route-banner">
        <div class="route-info">
            <span class="route-city"><%= from %></span>
            <span class="route-arrow">→</span>
            <span class="route-city"><%= to %></span>
        </div>
        <div class="route-date">📅 <%= date %></div>
        <a href="index.html" class="modify-btn">✏ Modify Search</a>
    </div>

    <%if (trains == null || trains.isEmpty()) { %>
    <div class="no-trains">
        <div class="no-trains-icon">🚫</div>
        <h2>No Trains Found</h2>
        <p>No trains are available for this route on the selected date.</p>
        <a href="index.html" class="search-btn" style="display:inline-block;text-decoration:none;margin-top:16px;">Search Again</a>
    </div>
    <% } else { %>

    <div class="results-header">
        <h2><%= trains.size() %> Trains Found</h2>
        <p>Showing all trains from <%= from %> to <%= to %></p>
    </div>

    <div class="train-list">
        <% for (Train train : trains) { %>
        <div class="train-card">
            <!-- Train Header -->
            <div class="train-card-header">
                <div class="train-name-section">
                    <h3 class="train-name"><%= train.getTrainName() %></h3>
                    <span class="train-number">#<%= train.getTrainNumber() %></span>
                </div>
                <div class="train-timing">
                    <div class="time-block">
                        <div class="time"><%= train.getDepartureTime() %></div>
                        <div class="station"><%= train.getFrom() %></div>
                    </div>
                    <div class="duration-block">
                        <div class="duration-line"></div>
                        <div class="duration-text"><%= train.getDuration() %></div>
                        <div class="duration-line"></div>
                    </div>
                    <div class="time-block">
                        <div class="time"><%= train.getArrivalTime() %></div>
                        <div class="station"><%= train.getTo() %></div>
                    </div>
                </div>
            </div>

            <!-- Class Availability -->
            <div class="class-grid">
                <%
                    String[] classes = {"1A", "2A", "3A", "SL"};
                    String[] classLabels = {"First AC", "Second AC", "Third AC", "Sleeper"};
                    for (int i = 0; i < classes.length; i++) {
                        String cls = classes[i];
                        String label = classLabels[i];
                        String availClass = train.getAvailabilityClass(cls);
                        String availText = train.getAvailabilityStatus(cls);
                        int price = train.getPrice(cls);
                %>
                <div class="class-box <%= availClass %>">
                    <div class="class-name"><%= cls %></div>
                    <div class="class-label"><%= label %></div>
                    <div class="class-price">₹<%= price %></div>
                    <div class="class-avail avail-<%= availClass %>"><%= availText %></div>
                    <% if (!availClass.equals("notavailable")) { %>
                    <a href="book?trainNumber=<%= train.getTrainNumber() %>&class=<%= cls %>&from=<%= from %>&to=<%= to %>&date=<%= date %>"
                       class="book-btn">Book Now</a>
                    <% } else { %>
                    <span class="book-btn-disabled">Full</span>
                    <% } %>
                </div>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>
    <% } %>
</div>

<footer class="footer">
    <p>© 2025 RailYatra · Built with ❤️ for Indian Railways</p>
</footer>
</body>
</html>
