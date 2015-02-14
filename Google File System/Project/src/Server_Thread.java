/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Properties;


public class Server_Thread extends Thread {

	Socket client;

	
	
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

		
		//reader and writer declaration
		
		//server and client interaction - server side
		
		BufferedReader input_client;
		PrintWriter output_client;
		try {
			
			input_client = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output_client = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
			String s_input = input_client.readLine();
		
			String[] in = s_input.split(":");
			
			//read operation
			if(in[0].equalsIgnoreCase("r"))
			{
				System.out.println("Read :"+in[1]);
				String filename = in[1]+InetAddress.getLocalHost().getHostName()+".txt";
				String x ="", line = "";
				File inFile = new File(filename);
				BufferedReader reader1 = new BufferedReader(new FileReader(inFile));
				while((line = reader1.readLine())!= null)
				{
					x = x+line;
				}
				output_client.println(x);
				reader1.close();
				System.out.println("read done");
				client.close();
			}
			
			//write operation
			if(in[0].equalsIgnoreCase("w"))
			{
				String filename = in[1]+InetAddress.getLocalHost().getHostName()+".txt";
				String text = in[2];
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				writer.println(text);
				writer.close();
				client.close(); 
			}

			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}

}
