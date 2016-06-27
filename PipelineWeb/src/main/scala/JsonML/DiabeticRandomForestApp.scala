package JsonML

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}

/**
  * Created by Jayaradha on 6/21/16.
  */
object DiabeticRandomForestApp {
  def main(args : Array[String]): Unit = {

    // Parse args
    val DEFAULT_RESOURCE_ROOT = System.getProperty("user.dir") + "/static/DiabeticRF.html"
    val DEFAULT_PORT = 8089
    val USAGE = "USAGE: mvn exec:java -Dexec.mainClass=\"JsonML.DiabeticRandomForestApp\"";

    // Create server
    System.setProperty("org.eclipse.jetty.LEVEL","INFO")
    val server = new Server(DEFAULT_PORT)

    // Setup paths
    val servlet = new DiabeticRandomForestServelet()

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
