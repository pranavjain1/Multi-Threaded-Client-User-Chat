/** 
*Purpose : The program will run a GUI for multiple clients chat within a server.
*          Class: ChatClient
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
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class ChatClient extends JFrame implements ActionListener {
    
    private static int port = 1234;              // port number 
    JFrame window = new JFrame("Chat");          
    JButton sendBox = new JButton("Send");
    JTextField inputMsg = new JTextField(35);
    JTextArea outputMsg = new JTextArea(10, 35);
    private static BufferedReader streamIn;
    private static PrintStream streamOut;
    
   private JMenuBar menuBar = new JMenuBar();
   private JMenu fileMenu = new JMenu("File");
   private JMenuItem exitMenuItem = new JMenuItem("Exit");
 
    /* Main */
    
    public static void main(String[] args) throws Exception{
        ChatClient client = new ChatClient();
        client.window.setVisible(true);
        client.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.run();        
    }
    
    
    public ChatClient(){
        
        inputMsg.setSize(40, 20);
        sendBox.setSize(5, 10);
        outputMsg.setSize(35, 50);
        inputMsg.setEditable(false);
        outputMsg.setEditable(false);
        window.getContentPane().add(inputMsg, "South");
        window.getContentPane().add(outputMsg, "East");
        window.getContentPane().add(sendBox, "West");
        window.pack();
        
   /**
     *   Setting up Menu  
     */  
     
      this.setJMenuBar(menuBar);
      menuBar.add(fileMenu);
      fileMenu.add(exitMenuItem);
   
      exitMenuItem.addActionListener(this);  
     
 		setVisible(true);   

/* This method implements the send box button*/

        sendBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                streamOut.println(inputMsg.getText());
                inputMsg.setText("");
            }
        });
 /* This method implements the message area function*/       
        inputMsg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                streamOut.println(inputMsg.getText());
                inputMsg.setText("");
            }
        });           
    }
    /* This method implements the username function*/   
    private String getUsername(){
        return JOptionPane.showInputDialog(window, "Your Name:", "Welcome to Chat", JOptionPane.QUESTION_MESSAGE);
    }
    
   /* This method implements the file exit function*/  
    public void actionPerformed(ActionEvent ae) {
      if(ae.getActionCommand().equals("Exit"))
      {
         System.out.println("Exit Menu Item clicked");
         System.exit(0);
         
      }
    }
  
 /* This method implements the chat function */  
    private void run() throws IOException{
      try {
        Socket clientSocket = new Socket("localhost", port);
        streamIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        streamOut = new PrintStream(clientSocket.getOutputStream(), true);
    
        while(true){
            String line = streamIn.readLine();
            if(line.startsWith("Username")){
                streamOut.println(getUsername());
           }else
             if(line.startsWith("Welcome")){
                inputMsg.setEditable(true);
            }else if(line.startsWith("From")){
                outputMsg.append(line.substring(10)+ "\n");
            }
        }   
    }  catch (Exception aa){
       System.out.println("Server is Closed: Sorry for the inconvenience " +aa);
    
    }
   }
}
