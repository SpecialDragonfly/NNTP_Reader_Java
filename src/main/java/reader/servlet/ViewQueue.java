package reader.servlet;

import reader.task.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ViewQueue extends HttpServlet {

    private final ConcurrentLinkedQueue<Task> queue;

    public ViewQueue(ConcurrentLinkedQueue<Task> queue) {
        this.queue = queue;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html");

        resp.getWriter().println("EmbeddedJetty");
        synchronized (this.queue) {
            if (this.queue.isEmpty()) {
                resp.getWriter().println("Queue empty");
            } else {
                this.queue.forEach((Object c) -> {
                    try {
                        resp.getWriter().println(c.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
