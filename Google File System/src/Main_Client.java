import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;


public class Main_Client {

	
	public static Properties prop;
	public static String read_function(String filename, int start, int offest) {
		
		//read functionality 
		String M_name;
		int M_port;
		int S_port;
		
		BufferedReader input_client;
		PrintWriter output_client;
		
		M_name = prop.getProperty("MHostName");
		M_port = Integer.parseInt(prop.getProperty("MPortNo"));
		S_port = Integer.parseInt(prop.getProperty("SPortNo"));
		try
		{
			int start_server = (start/8192)+1;
			int end_server = ((start+offest)/8192)+1;			
		Socket s = new Socket(M_name,M_port);
		input_client = new BufferedReader(new InputStreamReader(s.getInputStream()));
		output_client = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
		output_client.println("r:"+filename+":"+start_server+":"+end_server);
		String input;

		input = input_client.readLine();
		if(input.equalsIgnoreCase("filenotfound"))
		{
			s.close();
			return "Filename: "+filename+" $not$ found";
			
		}
		else
		{
			input = input_client.readLine();
			if(input.equalsIgnoreCase("serverunavailable"))
			{
				s.close();
				return "Server $not$ Available";
			}
			
			String start_server_name = input_client.readLine();
			String end_server_name = input_client.readLine();
			s.close();
			
			Socket s1 = new Socket(start_server_name, S_port);
			Socket s2 = new Socket(end_server_name, S_port);
			
			input_client = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			output_client = new PrintWriter(new OutputStreamWriter(s1.getOutputStream()),true);
			output_client.println("r:"+filename+start_server);
			input = input_client.readLine();
			s1.close();
			if(!(start_server== end_server))
			{
			
			input_client = new BufferedReader(new InputStreamReader(s2.getInputStream()));
			output_client = new PrintWriter(new OutputStreamWriter(s2.getOutputStream()),true);
			output_client.println("r:"+filename+end_server);
			input = input+" "+input_client.readLine();
			//input contains the entire piece of file.
			
			}
			s2.close();
			//read from start to offset.
			//edit made
			
			return input;
		}
		
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static String write_function(String filename, String msg) {
		
		//write functionality 
		
		String M_name;
		int M_port;
		int S_port;
		
		BufferedReader input_client;
		PrintWriter output_client;
		
		M_name = prop.getProperty("MHostName");
		M_port = Integer.parseInt(prop.getProperty("MPortNo"));
		S_port = Integer.parseInt(prop.getProperty("SPortNo"));
		try{
			
			Socket s = new Socket(M_name,M_port);
			input_client = new BufferedReader(new InputStreamReader(s.getInputStream()));
			output_client = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
			output_client.println("w:"+filename+":"+msg);
			String input;
			input = input_client.readLine();
			s.close();
			String[] servers = input.split(":");
			
			int i;
			
			for(i=0;i<servers.length;i++)
			{
				
				Socket s1 = new Socket(servers[i],S_port);
				System.out.println("Writing to server "+s1.getInetAddress().getHostName());
				String file = filename+(i+1);
				input_client = new BufferedReader(new InputStreamReader(s1.getInputStream()));
				output_client = new PrintWriter(new OutputStreamWriter(s1.getOutputStream()),true);
				if(i==(servers.length-1))
				{
					output_client.println("w:"+file+":"+msg.substring(i*8192));
				}
				else
				{
				output_client.println("w:"+file+":"+msg.substring(i*8192, ((i+1)*8192)-1));
				}
			}
			return "Success\n";
			
		}
		catch(Exception e)
		{

			e.printStackTrace();
		}
		
		
		
		return "Fail\n";
	}
	
	
	public static String append_function(String filename, String msg) {
		
		//getting existing file.
		
		String M_name;
		int M_port;
		int S_port;
		
		String Total_msg = "";
		String Read_msg = "";
		int server_len = 0;
		
		BufferedReader input_client;
		PrintWriter output_client;
		
		M_name = prop.getProperty("MHostName");
		M_port = Integer.parseInt(prop.getProperty("MPortNo"));
		S_port = Integer.parseInt(prop.getProperty("SPortNo"));
		
		try
		{
			
			Socket s = new Socket(M_name,M_port);
			input_client = new BufferedReader(new InputStreamReader(s.getInputStream()));
			output_client = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
			
			output_client.println("a:"+filename);
			
			String input = input_client.readLine();
			if(input.equalsIgnoreCase("filenotfound"))
			{
				s.close();
				return "Fail";
				
			}
			
			input = input_client.readLine();
			if(input.equalsIgnoreCase("serverunavailable"))
			{
				s.close();
				return "Fail";
				
			}
			
			String s_list = input_client.readLine();
			String[] servers = s_list.split(":");
			int i;
			server_len = servers.length-1;
			for(i=0;i<servers.length;i++)
			{
				Socket s1 = new Socket(servers[i],S_port);
				String file = filename+(i+1);
				input_client = new BufferedReader(new InputStreamReader(s1.getInputStream()));
				output_client = new PrintWriter(new OutputStreamWriter(s1.getOutputStream()),true);
				output_client.println("r:"+file);
				Read_msg = Read_msg+input_client.readLine();
			}
			
			
			//write
			
			int r = Read_msg.length() % 8192;
			int ceiling =  (Read_msg.length()/8192)+1;
			int floor =  (Read_msg.length()/8192);
			int Start_index = (floor * 8192)+r;
			int end_index = (ceiling * 8192);
			

			
			
			if((8192-r) < msg.length())
			{
				for(i = Start_index;i<end_index;i++)
				{
					Read_msg = Read_msg + "@";
				}
			}
			
			Total_msg = Read_msg+msg;
			
			String result = write_function(filename, Total_msg);
			return result;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return "fail";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			
		
		prop = new Properties();
		InputStream input = new FileInputStream("/home/004/v/vx/vxg130730/Workspace/AOS_Project2/Config.properties"); 
		prop.load(input);
		
		/*
		  read input from file. filename is specified in command line.
		 */
		
		if(args.length < 1) {
	        System.out.println("Specify file name");
	        System.exit(1);
	    }
		
		
		File inFile = new File(args[0]);
		BufferedReader reader1 = new BufferedReader(new FileReader(inFile));
		String line=null;
		String[] broken_text;
        while((line = reader1.readLine()) != null && !line.isEmpty()) {
        	
        	int line_success_counter =0;
        	Boolean line_success = true;
        		
        		
        	Thread.sleep(3*1000);
        	line_success_counter++;
        	broken_text = line.split("-");
            String first_item = broken_text[0];
            System.out.println(first_item);
            if ( first_item.equals("r")) {
                 //perform read operation
            	int start = Integer.parseInt(broken_text[2]);
            	int offset = Integer.parseInt(broken_text[3]);
            	String input1 =read_function(broken_text[1], Integer.parseInt(broken_text[2]), Integer.parseInt(broken_text[3]));
            	if(input1.contains("$not$"))
            	{
            		System.out.println(input1);
            	}
            	else
            	{
            	System.out.println(input1.substring((start%8192),((start%8192) +offset)));

            	line_success = false;
            	
            	}
            	//if done successfully, set false to line_success.
            	
            }
            else if("w".equals(first_item)){
            	//perform write operation
            	
            	String output = write_function(broken_text[1],broken_text[2]);
            	if(output.equalsIgnoreCase("fail"))
            	{
            		System.out.println("Failed");
            	}
            	else
            	{
            		System.out.println(output);
            	}
            	
            }
            else if("a".equals(first_item)){
            	//perform append operation
            	
            	String output = append_function(broken_text[1],broken_text[2]);
            	if(output.equalsIgnoreCase("fail"))
            	{
            		System.out.println("Failed");
            	}
            	else
            	{
            		System.out.println(output);
            	}
            	
            }
        
        	
        	}
        
		//label - 1
		
		//sleep for 2 sec
		
		
    //    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
