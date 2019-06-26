import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client 
{
	  static BufferedReader in;
      static PrintWriter out;
      
         static JFrame chatWindow = new JFrame("Chat Application");
	     static JTextArea chatArea = new JTextArea(30,40);
	     static JTextField textField = new JTextField("Enter your message",40);
	     static JLabel blankLabel = new JLabel("           ");
   	     static JButton sendButton = new JButton("Send");
		 static JLabel nameLabel = new JLabel("         ");

    Client()
    {
            chatWindow.setLayout(new FlowLayout());
            chatWindow.add(nameLabel);
            chatWindow.add(new JScrollPane(chatArea));
            chatWindow.add(blankLabel);
		    chatWindow.add(textField);
		    chatWindow.add(sendButton);
		    chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    chatWindow.setSize(500,670);
		    chatWindow.setVisible(true);
	        textField.setEditable(false);
	        chatArea.setEditable(false);
	        sendButton.addActionListener(new Listener());
	        textField.addActionListener(new Listener());

    }

   

   

    void startChat() throws Exception
    {
            String ipAddress = JOptionPane.showInputDialog(chatWindow,
														             "Enter IP Address:",
														              "IP Address Required!!",
														              JOptionPane.PLAIN_MESSAGE
														              );    
       Socket soc = new Socket(ipAddress, 9846);
       in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
       out = new PrintWriter(soc.getOutputStream(), true);

       while (true)
       {
           String str = in.readLine();
           if (str.equals("NAME REQUIRED"))
           {
               String name = JOptionPane.showInputDialog(chatWindow,
											                       "Enter a unique name:",
											                       "Name Required!!",
											                       JOptionPane.PLAIN_MESSAGE
											                       );
               out.println(name);
           }
           else if(str.equals("NAME ALREADY EXISTS"))
           {
               String name = JOptionPane.showInputDialog(chatWindow,
											                      "Enter another name:",
											                      "Name Already Exits!!",
											                      JOptionPane.WARNING_MESSAGE
											                      );
               out.println(name);
           }
           else if (str.startsWith("NAMEACCEPTED"))
           {
               textField.setEditable(true);
               nameLabel.setText("You are logged in as: "+str.substring(12));
           }
           else
           {
               chatArea.append(str + "\n");
           }
       }
   }
public static void main(String[] args) throws Exception {
            Client client = new Client();
            client.startChat();
      }
};

class Listener implements ActionListener
{
      public void actionPerformed(ActionEvent e) 
      {
            Client.out.println(Client.textField.getText());
            Client.textField.setText("");
      }
};
