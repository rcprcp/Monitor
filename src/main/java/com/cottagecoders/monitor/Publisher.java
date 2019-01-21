package com.cottagecoders.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Publisher {
  private Socket socket;
  private PrintWriter writer;

  Publisher(String hostname, int port) throws IOException {
    socket = new Socket(hostname, port);
    writer = new PrintWriter(socket.getOutputStream(), true);
  }

  void send(String data) {
    writer.println(data);
  }

  void close() {
    try {
      socket.close();
    } catch (IOException ex) {
      System.out.println("exception closing socket: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
