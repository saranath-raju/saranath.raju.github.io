import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.print.attribute.standard.Severity;


public class Main_MetaServer {

	static ArrayList< String> server_failed = new ArrayList<String>();
	static ArrayList< String> server_all = new ArrayList<String>();
	static HashMap<String, Integer> Heartbeat_counter = new HashMap<String, Integer>();
	static HashMap<String, Info> file_info = new HashMap<String, Info>();
	static ArrayList<String> server_HB_received = new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
		String PID="P"+InetAddress.getLocalHost().getHostName().substring(3, 5);
		
		String host = null;
		int port;
		Properties prop = new Properties();
		InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/Workspace/AOS_Project2/Config.properties"); 
		prop.load(input);
		host = prop.getProperty("MHostName");
		port = Integer.parseInt(prop.getProperty("MPortNo"));
		int i =0;
		for(i=1;i<4;i++)
		{
			server_all.add(prop.getProperty("S"+i+"HostName"));
		}
		System.out.println("Servers: "+server_all);
		//started listening -- separate thread created.
		Listening_Metaserver s1 = new Listening_Metaserver(port);
		s1.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//

	}

}
