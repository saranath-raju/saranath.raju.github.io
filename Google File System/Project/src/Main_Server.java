import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.Timer;


public class Main_Server {
	
	
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
		
		String PID="P"+InetAddress.getLocalHost().getHostName().substring(3, 5);
		String host = null;
		int port;
		Properties prop = new Properties();
		//location of config file
		InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/Workspace/AOS_Project2/Config.properties"); 
		prop.load(input);
		host = InetAddress.getLocalHost().getHostName();
		port = Integer.parseInt(prop.getProperty("SPortNo"));

		//started listening -- separate thread created.
		Listening s1 = new Listening(port);
		s1.start();
		
		
		Timer t = new Timer();
		t.scheduleAtFixedRate( 
		        new java.util.TimerTask() {
		            @Override
		            
		            
		            //periodic HB msg
		            public void run() {
		              
		            	try{
		            		
		            
		            	Properties prop1 = new Properties();
		        		InputStream input1 = new FileInputStream("/home/004/s/sx/sxg138930/Workspace/AOS_Project2/Config.properties"); 
		        		prop1.load(input1);
		        		
		        		String m_host = prop1.getProperty("MHostName");
		        		int m_port = Integer.parseInt(prop1.getProperty("MPortNo"));
		        		
		        		
		        		double random_time = Math.random() ;
		        		
		        		
		        		Socket m_server = new Socket(m_host, m_port);
		        		PrintWriter output_mserver;
		        		output_mserver = new PrintWriter(new OutputStreamWriter(m_server.getOutputStream()),true);
		        		
		        		if(random_time<0.5)
		        		{
		        			output_mserver.println("HB:inactive");
		        		}
		        		else
		        		{
		        			output_mserver.println("HB:active");
		        		}
		        		
		            	}
		            	catch(Exception e)
		            	{
		            		e.printStackTrace();
		            	}
		            	
		            	
		            }
		            
		            
		        }, 
		        10*1000, 5*1000
		);
		
		
		
		}
		catch(Exception e)
		{
			
		}

		
	}

	
	
	
}
