package com.railway.servlet;

import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.util.DataStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Map;

@WebServlet("/cancel")
public class CancelTicketServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pnr = request.getParameter("pnr");
        DataStore ds = DataStore.getInstance();

        Ticket ticket = ds.getTicket(pnr);
        if (ticket == null || ticket.isCancelled()) {
            response.sendRedirect(request.getContextPath() + "/ticket?pnr=" + pnr + "&error=cancel");
            return;
        }

        Train train = ds.getTrain(ticket.getTrainNumber());
        if (train == null) {
            response.sendRedirect(request.getContextPath() + "/ticket?pnr=" + pnr + "&error=cancel");
            return;
        }

        String cls = ticket.getTravelClass();
        String oldStatus = ticket.getStatus();

        // Cancel the seat and get promoted PNR if any
        String promotedPnr = train.cancelSeat(cls, oldStatus, pnr);

        // Mark ticket as cancelled
        ticket.setCancelled(true);
        ticket.setStatus("CANCELLED");

        // If a WL person got promoted, update their ticket
        if (promotedPnr != null) {
            Ticket promotedTicket = ds.getTicket(promotedPnr);
            if (promotedTicket != null) {
                promotedTicket.setStatus("CONFIRMED");
            }

            // Re-number remaining WL tickets for this train+class
            updateWaitingListNumbers(train, cls, ds);
        }

        response.sendRedirect(request.getContextPath() + "/ticket?pnr=" + pnr + "&cancelled=true");
    }

    // After a WL person is promoted, update WL numbers for remaining WL tickets
    private void updateWaitingListNumbers(Train train, String cls, DataStore ds) {
        int pos = 1;
        for (String wlPnr : train.getWaitingList(cls)) {
            Ticket t = ds.getTicket(wlPnr);
            if (t != null && !t.isCancelled()) {
                t.setStatus("WL" + pos);
                pos++;
            }
        }
    }
}
