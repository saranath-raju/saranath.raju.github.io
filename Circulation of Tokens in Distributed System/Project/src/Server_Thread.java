/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;


public class Server_Thread extends Thread{

	Socket client;
	Properties prop = new Properties();
	
	public Server_Thread() {
		// TODO Auto-generated constructor stub
	}
	
	public Server_Thread( Socket s) {
		// TODO Auto-generated constructor stub
		super();
		client=s;
	
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		BufferedReader input_client;
		PrintWriter output_client;
		try {
			
			InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/Workspace/AOS_Project_1/Config.properties"); 
			prop.load(input);
			
			input_client = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output_client= new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
			
			String output_msg = input_client.readLine();
			String initial_process,next_path;
			int sum= Server_Main.RANDOM_LABEL;
			
			//if path not done
			//aggregate random label
			if(output_msg.equalsIgnoreCase("compute"))
			{
				initial_process = input_client.readLine();
				sum = sum+Integer.parseInt(input_client.readLine());
				next_path = input_client.readLine();

				
				if(!(next_path.equalsIgnoreCase(";")))
				{
					
					String next_Hop_hostname = prop.getProperty("P"+next_path.substring(0, 1)+"HostName");
					int next_Hop_portno =Integer.parseInt(prop.getProperty("P"+next_path.substring(0, 1)+"PortNo"));
					
					Socket s = new Socket(next_Hop_hostname, next_Hop_portno);
					BufferedReader input_client1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter output_client1= new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
				
				output_client1.println("compute");
				output_client1.println(initial_process);
				output_client1.println(sum);
				output_client1.println(next_path.substring(1));
				
				}
				
				else
				{
					int port =Integer.parseInt( prop.getProperty("P"+initial_process.substring(3, 4)+"PortNo"));
					Socket s1 = new Socket(initial_process,port );
					BufferedReader input_client12 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
					PrintWriter output_client12= new PrintWriter(new OutputStreamWriter(s1.getOutputStream()),true);
					output_client12.println("done");
					output_client12.println(sum);
					
				}
				
				
			}
			
			
			//once done all computation
			if(output_msg.equalsIgnoreCase("done"))
			{
				
				int TOTAL_SUM = Integer.parseInt(input_client.readLine());
				System.out.println("Overall Sum of Tokens Along path is "+ TOTAL_SUM);
				
			}
			
				
			//client closes
			client.close();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
