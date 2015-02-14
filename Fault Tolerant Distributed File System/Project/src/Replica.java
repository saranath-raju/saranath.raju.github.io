import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.TimerTask;


public class Replica {

	
	public void replica_function() {
		// TODO Auto-generated method stub
			
			Properties prop = new Properties();
			int i;
			
		//	boolean f = false;
			
			Socket[] s = new Socket[10];
			int[] versions = new int[10];
			int max=-1;
			BufferedReader[] input_client = new BufferedReader[10];
			PrintWriter[] output_client = new PrintWriter[10];
			try
			{
			InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/workspace/AOS_Project2/Config.properties");
			prop.load(input);
			
			//broadcasting
			for(i=1;i<=6;i++)
			{
				s[i]= new Socket(prop.getProperty("P"+i+"HostName"), Integer.parseInt(prop.getProperty("P"+i+"PortNo")));
				input_client[i]= new BufferedReader(new InputStreamReader(s[i].getInputStream()));
				output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
				output_client[i].println("Replica");
				String a=input_client[i].readLine();
				versions[i]=Integer.parseInt(a);
				max= versions[1];
			}
			
			//looking for max - latest
			for(i=2;i<=6;i++)
			{
				if(max<versions[i])
				{
					max = versions[i];
				}
			}
			int j=0;
			
			//checking consistency
			for(i=1;i<6;i++)
			{
				if(max == versions[i])
				{
					j++;
				}
				if(j>=3)
				{
					System.out.println("-------------Replica Consistency Achieved-------------------");
					break;
				}
			}
			
			for(i=1;i<=6;i++)
			{
				output_client[i].println("Replica_cons");
			}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//Server_Main.np++;
	
		
	}

}
