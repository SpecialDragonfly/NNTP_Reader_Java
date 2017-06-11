package reader.action;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class StartServer implements Runnable {

    private final ServletContextHandler servletContextHandler;
    private Server server;

    public StartServer(Server server) {
        this.server = server;
        this.servletContextHandler = new ServletContextHandler(this.server, "/x");
    }

    public void addServlet(Servlet servlet, String pathSpec) {
        this.servletContextHandler.addServlet(new ServletHolder(servlet), pathSpec);
    }

    @Override
    public void run() {

        server.setHandler(servletContextHandler);
        try {
            server.start();
            server.dump();
            server.join();
        } catch (Exception ex) {
            // do nothing
        }
    }
}
