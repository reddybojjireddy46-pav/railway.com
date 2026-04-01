package com.railway.servlet;

import com.railway.model.Train;
import com.railway.model.Ticket;
import com.railway.util.DataStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/book")
public class BookTicketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show booking form
        String trainNumber = request.getParameter("trainNumber");
        String travelClass = request.getParameter("class");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String date = request.getParameter("date");

        Train train = DataStore.getInstance().getTrain(trainNumber);
        if (train == null) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=notfound");
            return;
        }

        request.setAttribute("train", train);
        request.setAttribute("travelClass", travelClass);
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        request.setAttribute("date", date);

        RequestDispatcher rd = request.getRequestDispatcher("/book.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String trainNumber = request.getParameter("trainNumber");
        String travelClass = request.getParameter("travelClass");
        String passengerName = request.getParameter("passengerName");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String date = request.getParameter("date");

        if (passengerName == null || passengerName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/book?trainNumber=" + trainNumber
                    + "&class=" + travelClass + "&from=" + from + "&to=" + to + "&date=" + date + "&error=name");
            return;
        }

        DataStore ds = DataStore.getInstance();
        Train train = ds.getTrain(trainNumber);
        if (train == null) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=notfound");
            return;
        }

        String pnr = ds.generatePNR();
        String status = train.bookSeat(travelClass, pnr);

        Ticket ticket = new Ticket(
                pnr,
                passengerName.trim(),
                train.getTrainName(),
                train.getTrainNumber(),
                from, to, date,
                travelClass,
                train.getPrice(travelClass),
                status,
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getDuration()
        );

        ds.saveTicket(ticket);

        response.sendRedirect(request.getContextPath() + "/ticket?pnr=" + pnr + "&booked=true");
    }
}
