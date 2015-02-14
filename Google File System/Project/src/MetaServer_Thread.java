
/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.ArrayList;
import java.util.Properties;


public class MetaServer_Thread extends Thread {

	Socket client;

	
	public MetaServer_Thread() {
		// TODO Auto-generated constructor stub
	}

	public MetaServer_Thread( Socket s) {
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
			String[] input = s_input.split(":");
			
			
			//if heartbeat message
			if(input[0].equalsIgnoreCase("HB"))
			{
				String host = client.getInetAddress().getHostName();
				if(input[1].equalsIgnoreCase("inactive"))
				{
					if(Main_MetaServer.Heartbeat_counter.containsKey(host))
					{
						int count = Main_MetaServer.Heartbeat_counter.get(host);
						if(count >= 2)
						{
							Main_MetaServer.server_failed.add(host);
							System.out.println(host+" unavailable");
						}
						else
						{
							count++;
							Main_MetaServer.Heartbeat_counter.put(host, count);
						}
					}
					else
					{
						Main_MetaServer.Heartbeat_counter.put(host, 0);
					}
				}
				else {
					if(Main_MetaServer.server_failed.contains(host))
					{
						Main_MetaServer.server_failed.remove(Main_MetaServer.server_failed.indexOf(host));
						System.out.println(host+" available");
					}
				}
			}
			
						
			//read operation
			if(input[0].equalsIgnoreCase("r"))
			{
				if(Main_MetaServer.file_info.containsKey(input[1]))
				{
					
					output_client.println("success1");
					int start = Integer.parseInt(input[2]);
					int end = Integer.parseInt(input[3]);
					
					System.out.println("R server size list :"+Main_MetaServer.file_info.get(input[1]).server_list.size());
					System.out.println("R server list: "+Main_MetaServer.file_info.get(input[1]).server_list);
					
					String start_server = Main_MetaServer.file_info.get(input[1]).server_list.get(start-1);
					
					String end_server = Main_MetaServer.file_info.get(input[1]).server_list.get(end-1);
					
					
					
					if(Main_MetaServer.server_failed.contains(start_server) || Main_MetaServer.server_failed.contains(end_server))
					{
						output_client.println("serverunavailable");
						client.close();
					}
					else
					{
						output_client.println("success2");
						output_client.println(start_server);
						output_client.println(end_server);
						client.close();
					}
					
				}
				else
				{
					
					output_client.println("filenotfound");
					System.out.println("file name "+input[1]+"not found sent");
					client.close();
				}
				
			}
			

			//write operation
			if(input[0].equalsIgnoreCase("w"))
			{
				String filename = input[1];
				String msg = input[2];
				int totalByte = msg.length();
				int totalChunk = (int) (totalByte/8192);
				int totalServer = Main_MetaServer.server_all.size();
				
				totalChunk++;
				//random no generation
				int i;
				String s_list="";
				String s;
				for(i = 0;i<totalChunk;i++)
				{
				int random_time = (int) (Math.random() * (totalServer - 1))+1;
				
				s = "dc0"+random_time+".utdallas.edu";
				while(Main_MetaServer.server_failed.contains(random_time))
				{
					random_time = (int) (Math.random() * (totalServer - 1))+1;
					 s = "dc0"+random_time+".utdallas.edu";
				}
					s_list = s_list+s+":";
				}
				
				
				String[] servers = s_list.split(":");
				Info info = new Info();
				System.out.println("file name :" + filename);
				for(i=0;i<servers.length;i++)
				{
				
					info.server_list.add(servers[i]);
					info.server_ts.put(servers[i], new Timestamp(date.getTime()).toString());
				}
				
				Main_MetaServer.file_info.put(filename, info);
				output_client.println(s_list);
				client.close();
			}
			
			
			
			//append - full read
			if(input[0].equalsIgnoreCase("a"))
			{
				
				if(Main_MetaServer.file_info.containsKey(input[1]))
				{
					output_client.println("success1");
					ArrayList<String> servers = Main_MetaServer.file_info.get(input[1]).server_list;
					boolean server_fail = false;
					for (String string : servers) {
						
					if(Main_MetaServer.server_failed.contains(string))
					{
						server_fail = true;
					}
						
					}
					
					if(server_fail)
					{
						output_client.println("serverunavailable");
						client.close();
					}
					else
					{
					output_client.println("success2");
					int i =0;
					String x ="";
					for(i=0;i<servers.size();i++)
					{
						
						x = x+servers.get(i)+":";
					}
					
					output_client.println(x);
					client.close();
					
					}
				}
				else
				{
					output_client.println("filenotfound");
					client.close();
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
