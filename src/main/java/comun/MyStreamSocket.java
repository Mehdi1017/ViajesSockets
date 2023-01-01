package comun;

import java.net.*;
import java.io.*;

/**
 * A wrapper class of Socket which contains 
 * methods for sending and receiving messages
 * @author M. L. Liu
 */
public class MyStreamSocket  {
   private final Socket  socket;
   private BufferedReader input;
   private PrintWriter output;

   public MyStreamSocket(InetAddress acceptorHost, int acceptorPort)
		                throws SocketException, IOException{
      socket = new Socket(acceptorHost, acceptorPort );
      setStreams();

   }

   public MyStreamSocket(Socket socket)  throws IOException {
      this.socket = socket;
      String host = socket.getInetAddress().getHostName();
      System.out.println("Creado socket con host: " + host);
      setStreams();
   }

   private void setStreams() throws IOException {
      // get an input stream for reading from the data socket
      InputStream inStream = socket.getInputStream();
      input = new BufferedReader(new InputStreamReader(inStream));
      OutputStream outStream = socket.getOutputStream();
      // create a PrinterWriter object for character-mode output
      output = new PrintWriter(new OutputStreamWriter(outStream));
   }

   public void sendMessage(String message) throws IOException {	
      output.print(message + "\n");   
      //The ensuing flush method call is necessary for the data to
      // be written to the socket data stream before the
      // socket is closed.
      output.flush();               
   } // end sendMessage

   public String receiveMessage() throws IOException {	
      // read a line from the data stream
      return input.readLine();
   } //end receiveMessage

   public void close() throws IOException {	
      socket.close();
   }
} //end class
