/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.jersey.safecollection.service;

import java.io.Serializable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * SafeCollectioon Root resource (exposed at "safecollection" path)
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
@Path("safecollection")
public class SafeCollection implements Serializable
{
  private static final long serialVersionUID = -490962943048803155L;

  /**
   * Static source version
   */
  public static final String version = "0.1.1";

  /**
   * An HTTP GET requests returning the version to the client as "text/plain"
   * media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String versionInfo()
  {
    return "Version " + version;
  }
}
