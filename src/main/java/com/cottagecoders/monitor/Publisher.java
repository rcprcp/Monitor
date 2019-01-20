package com.cottagecoders.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Publisher {
  private Socket socket;
  private PrintWriter writer;
  private BufferedReader reader;
  private StringBuilder sb;

  public Publisher(String hostname, int port) throws IOException {
    // make socket connection here.
    socket = new Socket(hostname, port);

    sb = new StringBuilder();
    writer = new PrintWriter(socket.getOutputStream(), true);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  void send(String data) {
    System.out.println("send(): got here " + data);
    writer.println(data);
  }

  void close() {
    try {
      socket.close();
    }catch(IOException ex) {
      System.out.println("exception closing socket: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
