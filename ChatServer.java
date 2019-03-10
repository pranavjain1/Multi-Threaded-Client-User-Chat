/** 
 *Purpose : The program will run a GUI for multiple clients chat within a server.
 *          Class: ChatServer
 *          
 *Caveats : N/A
 *               
 *                    
 *Date:     Dec 12, 2018
 *@author:  Pranav Jain
 *@version: 1.0
 */

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.lang.*;

public class ChatServer {

 private static int port = 1234; //port number
 private static ServerSocket server = null;
 private static Socket clientSocket;
 private static String line;
 private static BufferedReader Brt;
 private static PrintStream pwt;


 private static Vector < String > usernames = new Vector < String > (); // Vector for storing clients
 private static Vector < PrintStream > streams = new Vector < PrintStream > ();  //Vector for writing data to output streams

 /* Main */

 public static void main(String[] args) throws IOException {

  try {
   System.out.println("Connecting to port " + port + " ....");
   server = new ServerSocket(port);
   System.out.println("Chat application server is now running..");
   while (true) {
    clientSocket = server.accept();
    chatHandler c = new chatHandler(clientSocket);
    c.start();
   }
  } catch (IOException e) {
   System.out.println("Couldn't connect to the port! " + e);
  } finally {
   server.close();
  }
 }

 /* This method implements the sockets for clients*/

 private static class chatHandler extends Thread {

  private Socket clientSocket;

  public chatHandler(Socket clientSocket) {
   super("chatHandler");
   this.clientSocket = clientSocket;
  }
  
  /* This method implements the chatting function for the clients*/
  public void run() {

   try {
    Brt = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    pwt = new PrintStream(clientSocket.getOutputStream(), true);

    while (true) {
     pwt.println("Username");
     String line = Brt.readLine();
     if (line == null) {
      return;
     }
     try {
      synchronized(usernames) {
       if (!usernames.contains(line)) {
        usernames.add(line);
        break;
       }
      }
     } catch (Exception e) {
      System.out.println(e);
     }
    }
    pwt.println("Welcome");
    streams.add(pwt);

    while (true) {
     String message = Brt.readLine();
     if (message == null) {
      return;
     }
     for (PrintStream stream: streams) {
      stream.println("From " + line + ": " + message);
     }

    }
   } catch (IOException e) {
    System.out.println(e);

   } finally {
    if (line != null && pwt != null) {
     usernames.remove(line);
     streams.remove(pwt);
    }
    try {
     clientSocket.close();
    } catch (IOException e) {
     System.out.println(e);
    }

   }
  }

 }

}