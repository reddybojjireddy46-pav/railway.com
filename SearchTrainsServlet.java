package com.railway.servlet;

import com.railway.model.Train;
import com.railway.util.DataStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchTrainsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String date = request.getParameter("date");

        if (from == null || to == null || date == null || from.isEmpty() || to.isEmpty() || date.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=missing");
            return;
        }

        if (from.equals(to)) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=same");
            return;
        }

        List<Train> trains = DataStore.getInstance().searchTrains(from, to);

        request.setAttribute("trains", trains);
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        request.setAttribute("date", date);

        RequestDispatcher rd = request.getRequestDispatcher("/trainlist.jsp");
        rd.forward(request, response);
    }
}
