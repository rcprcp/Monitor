package com.cottagecoders.monitor;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Map;

public class HttpServer extends NanoHTTPD implements Runnable {
  int port;

  public HttpServer(int port) {
    super(port);
    this.port = port;
  }

  public void run() {
    try {
      start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    } catch (IOException ex) {
      System.out.println("Failed.  " + ex.getMessage());
      ex.printStackTrace();
    }
    System.out.println("NanoHttpd server running.");
  }

  @Override
  public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
    final String header = "<html><body><center><h1>Monitor</h1></center><br></br><br></br>";
    final String footer = "</body></html>";
    Map<String, String> parms = session.getParms();
    StringBuilder sb = new StringBuilder(header);
    if (parms.get("page") == null) {
      // menu.
      sb.append("<br><a href=http://localhost:");
      sb.append(port);
      sb.append("?page=config>Config</a></br>");

      sb.append("<br><a href=http://localhost:");
      sb.append(port);
      sb.append("?page=metrics>Metrics</a></br>");

    } else if (parms.get("page").equalsIgnoreCase("config")) {
      sb.append(Monitor.conf.toHtml());

    } else if (parms.get("page").equalsIgnoreCase("metrics")) {
      sb.append(MetricPool.dump().replaceAll("\n", "<br>"));

    }
    sb.append(footer);

    return newFixedLengthResponse(sb.toString());
  }
}
