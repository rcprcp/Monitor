package com.cottagecoders.monitor;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class HttpServer extends NanoHTTPD implements Runnable {
  int port;
  String hostName;

  public HttpServer(int port) {
    super(port);
    this.port = port;
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
      hostName = "localhost";
    }
  }

  public void run() {
    try {
      start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    } catch (IOException ex) {
      System.out.println("Failed.  " + ex.getMessage());
      ex.printStackTrace();
    }
    System.out.println("NanoHttpd server running. hostname " + hostName + " port " + port);
  }

  @Override
  public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
    final String header = "<html><body><center><h1>Monitor</h1></center><br></br><br></br>";
    final String footer = "</body></html>";
    Map<String, String> parms = session.getParms();
    StringBuilder sb = new StringBuilder(header);
    if (parms.get("page") == null) {
      // menu.
      sb.append("<br><a href=http://");
      sb.append(hostName);
      sb.append(":");
      sb.append(port);
      sb.append("?page=config>Config</a></br>");

      sb.append("<br><a href=http://");
      sb.append(hostName);
      sb.append(":");
      sb.append(port);
      sb.append("?page=metrics>Metrics</a></br>");

    } else if (parms.get("page").equalsIgnoreCase("config")) {
      sb.append(Monitor.conf.toHtml());

    } else if (parms.get("page").equalsIgnoreCase("metrics")) {
      sb.append("MetricPool size " + MetricPool.size());
      sb.append(MetricPool.htmlTable().replaceAll("\n", "<br>"));

    }
    sb.append(footer);

    return newFixedLengthResponse(sb.toString());
  }
}
