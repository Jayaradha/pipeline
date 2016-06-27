package dei

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}


object App {

  def main(args : Array[String]): Unit = {

    // Parse args
    val DEFAULT_RESOURCE_ROOT = System.getProperty("user.dir") + "/static/index.html"
    val DEFAULT_PORT = 8089
    val USAGE = "USAGE: mvn exec:java -Dexec.mainClass=\"dei.App\"";

    // Create server
    System.setProperty("org.eclipse.jetty.LEVEL","INFO")
    val server = new Server(DEFAULT_PORT)

    // Setup paths
    val servlet = new TitanicServlet()

    // Change resourcesPath to hard-coded value

    // Setup application "context" (handler tree in jetty speak)
    val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
    contextHandler.setResourceBase(DEFAULT_RESOURCE_ROOT)

    // Path in URL to match
    contextHandler.setContextPath("/")
    server.setHandler(contextHandler)

    // Add custom servlet
    val servletName = "app"
    val servletUrlPath = "/app/*"
    val holderDynamic = new ServletHolder(servletName, servlet)
    contextHandler.addServlet(holderDynamic, servletUrlPath)

    // Default servlet for root content (always last)
    val holderPwd = new ServletHolder("default", new DefaultServlet())
    holderPwd.setInitParameter("dirAllowed", "true")
    contextHandler.addServlet(holderPwd, "/")

    // Start server
    server.start()
    server.join()
  }
}