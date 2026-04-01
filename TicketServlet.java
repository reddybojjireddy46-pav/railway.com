package com.railway.servlet;

import com.railway.model.Ticket;
import com.railway.util.DataStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pnr = request.getParameter("pnr");
        String booked = request.getParameter("booked");

        if (pnr == null || pnr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mybookings.html");
            return;
        }

        Ticket ticket = DataStore.getInstance().getTicket(pnr);

        if (ticket == null) {
            request.setAttribute("error", "Ticket not found for PNR: " + pnr);
            request.getRequestDispatcher("/mybookings.jsp").forward(request, response);
            return;
        }

        request.setAttribute("ticket", ticket);
        request.setAttribute("booked", booked);
        request.getRequestDispatcher("/ticket.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // PNR search from my bookings page
        String pnr = request.getParameter("pnr");
        response.sendRedirect(request.getContextPath() + "/ticket?pnr=" + pnr);
    }
}
