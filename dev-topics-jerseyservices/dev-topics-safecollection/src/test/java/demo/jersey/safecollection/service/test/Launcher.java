/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.jersey.safecollection.service.test;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Grizzly Launcher for testing.
 *
 * @author Don Trummell
 */
public class Launcher implements Serializable
{
  private static final long serialVersionUID = 8136851469546220850L;

  // The resource base for the Grizzly HTTP
  public static final String APP_BASE = "services";

  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8080/" + APP_BASE
      + "/";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
   * application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer()
  {
    // create a resource config that scans for JAX-RS resources and providers
    // in demo.jersey.safecollection.service package
    final ResourceConfig rc = new ResourceConfig()
        .packages("demo.jersey.safecollection.service");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   * 
   * @param args
   * @throws IOException
   */
  @SuppressWarnings("deprecation")
  public static void main(String[] args) throws IOException
  {
    HttpServer server = null;
    Throwable error = null;
    try
    {
      server = startServer();
    }
    catch (Throwable th)
    {
      error = th;
    }

    if (error == null)
    {
      System.out.println(String.format(
          "The Jersey Launcher application, started with WADL available at "
              + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
      try
      {
        System.in.read();
      }
      catch (final Throwable th)
      {
        System.err.println("Read failed: " + th.getMessage());
      }

      try
      {
        server.stop();
        System.out.println("\nServer stopped");
      }
      catch (Throwable th)
      {
        System.err.println("\n*** Unable to stop Grizzly server: "
            + th.getMessage());
      }
    }
    else
      System.err.println("\n*** Unable to start Grizzly server: "
          + error.getMessage());
  }
}
