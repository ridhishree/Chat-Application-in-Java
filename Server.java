import java.io.*;
import java.net.*;
import java.util.*;

public class Server 
{
      static ArrayList<String> userNames = new ArrayList<String>();
      static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();

      public static void main(String[] args) throws Exception
      {
           System.out.println("Waiting for clients..."); 
           ServerSocket ss = new ServerSocket(9846);
           
           while (true)
           {
             Socket soc = ss.accept();
               System.out.println("Connection established");
             ChatThread th = new ChatThread(soc);
             th.start();
           }
      }
};

class ChatThread extends Thread
{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String name;
   public ChatThread(Socket socket) throws Exception {
        this.socket = socket;
    }

   public void run()
   {
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            int count = 0;
            while (true)
            {
               if(count > 0)
                    out.println("NAME ALREADY EXISTS");
               else
                    out.println("NAME REQUIRED");

               name = in.readLine();

               if (name == null)
                   return;          
               if (!Server.userNames.contains(name))
               {
                    Server.userNames.add(name);
                    break;
               }
             count++;
           }
            out.println("NAMEACCEPTED"+name);
            Server.printWriters.add(out);

            while (true)
            {
                String message = in.readLine();
                if (message == null)
                     return;
                for (PrintWriter writer : Server.printWriters) {
                    writer.println(name + ": " + message);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
   }
};