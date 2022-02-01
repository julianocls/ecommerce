package br.com.alura.ecommerce;


import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet implements Servlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));

            var orderId = UUID.randomUUID().toString();
            var order = new Order(orderId, amount, email);

            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, new CorrelationId(NewOrderServlet.class.getSimpleName()), order);

            System.out.println("New Order sent successfully");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New Order sent successfully");
        } catch (ExecutionException e) {
            throw new ServletException();
        } catch (InterruptedException e) {
            throw new ServletException();
        }
    }
}
