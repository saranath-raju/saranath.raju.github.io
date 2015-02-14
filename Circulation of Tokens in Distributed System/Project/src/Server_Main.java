/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimerTask;

import javax.management.timer.Timer;
import javax.sound.sampled.ReverbType;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;


public class Server_Main {
	
	static final int RANDOM_LABEL = (int) (Math.random()*10);


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		int port = 1000;
		java.util.Timer t = new java.util.Timer();
		
	
		final Properties prop = new Properties();
		
		try {
			 final String PID="P"+InetAddress.getLocalHost().getHostName().substring(3,4);;
			 String host = null;
			
			InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/Workspace/AOS_Project_1/Config.properties"); 
			prop.load(input);
			host = prop.getProperty(PID+"HostName");
			port = Integer.parseInt(prop.getProperty(PID+"PortNo"));
			
			Listening s1 = new Listening(port);
			s1.start();
			
		
		
		new java.util.Timer().schedule(new java.util.TimerTask()
		{
			@Override
			public void run()
			{
				
				String path = null;
				
			try
				
			{
				System.out.println(InetAddress.getLocalHost().getHostName()+" Started...\nRandom label is "+RANDOM_LABEL);
				path = prop.getProperty(PID+"Path");
				System.out.println("Path is "+path);
				String first_Hop_hostname = prop.getProperty("P"+path.substring(0, 1)+"HostName");
				int first_Hop_portno =Integer.parseInt(prop.getProperty("P"+path.substring(0, 1)+"PortNo"));
				Socket s = new Socket(first_Hop_hostname, first_Hop_portno);
				BufferedReader input_client = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter output_client= new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
				
				System.out.println(PID+"->"+path);
				
				//initiator
				output_client.println("compute");
				output_client.println(prop.getProperty(PID+"HostName"));
				output_client.println(Server_Main.RANDOM_LABEL);
				output_client.println(path.substring(1));
				
				
				
				
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				
			
		
			}
		}
		
		, 10*1000);
		
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
