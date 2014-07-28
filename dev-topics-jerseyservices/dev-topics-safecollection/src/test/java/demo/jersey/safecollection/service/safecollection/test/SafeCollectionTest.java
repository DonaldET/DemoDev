/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.jersey.safecollection.service.safecollection.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import demo.jersey.safecollection.service.SafeCollection;
import demo.jersey.safecollection.service.test.Launcher;

public class SafeCollectionTest
{
  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception
  {
    // start the server
    server = Launcher.startServer();
    // create the client
    Client c = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Launcher.startServer())
    // --
    // c.configuration().enable(new
    // org.glassfish.jersey.media.json.JsonJaxbFeature());

    target = c.target(Launcher.BASE_URI);
  }

  @SuppressWarnings("deprecation")
  @After
  public void tearDown() throws Exception
  {
    server.stop();
  }

  @Test
  public void testVersion()
  {
    final String responseMsg = target.path("safecollection").request()
        .get(String.class);
    assertEquals("Version " + SafeCollection.version, responseMsg);
  }
}
