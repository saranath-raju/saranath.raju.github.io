import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Server_Thread implements Runnable {

	Socket client;
	static int read=0;
	
	static ArrayList<String>  file_out= new ArrayList<String>(); 
	
	 private final Object lock = new Object();
	
	public Server_Thread() {
		// TODO Auto-generated constructor stub
	}
	
	public Server_Thread( Socket s) {
		// TODO Auto-generated constructor stub
		super();
		client=s;
	
	}
	
	public void run() {
		BufferedReader input_client;
		try {
			//writer and reader
			input_client = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter output_client = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
			
			//reading incoming text
			String rec_msg = input_client.readLine();
			if(Server_Main.fault_flag)
			{

			
			//unlock msg
			if(rec_msg.substring(0, 6).equalsIgnoreCase("Unlock"))
			{
				if(rec_msg.substring(6).equalsIgnoreCase("Read"))
				{
					
					System.out.println("\nReadlock by "+client.getInetAddress().getCanonicalHostName()+"-- UnLocked<-------");
					
					//writing in the output file
					file_out.add("ReadUnlock by "+client.getInetAddress().getCanonicalHostName());
					
					synchronized (lock) {
						read--;
			        }
					
					synchronized (lock) {
						if(read==0)
						{
							Server_Main.rwl.readLock().unlock();
						}
			        }
					
				}
				else
				{
					System.out.println("\nWriteLock by "+client.getInetAddress().getCanonicalHostName()+"-- UnLocked<-------");
					
					//writing in the output file
					
					file_out.add("WriteUnlock by "+client.getInetAddress().getCanonicalHostName());
				}
			}
			
			
			//readlock msg
			if(rec_msg.substring(0, 4).equalsIgnoreCase("Read"))
			{
				synchronized (lock) {
					read++;
		        }
				Server_Main.rwl.readLock().lock();
				System.out.println("-------> "+rec_msg);
				
				//writing in the output file
				
				file_out.add("Readlock by "+client.getInetAddress().getCanonicalHostName());
				
				output_client.println(Server_Main.VERSION);	
			}
			
			
			//sending file
			if(rec_msg.equalsIgnoreCase("Request"))
			{
			
				//sending file content
				BufferedReader br1 = null;
				String sCurrentLine1;
				br1 = new BufferedReader(new FileReader("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt"));
				System.out.println("---Sending File Content---");
				sCurrentLine1 = br1.readLine();
				output_client.println(sCurrentLine1);
				
			}
			
			
			//write lock
			if(rec_msg.substring(0, 5).equalsIgnoreCase("Write"))
			{
				Server_Main.rwl.writeLock().lock();
				String[] values = rec_msg.split(":");
				System.out.println("------->"+values[0]);
				File fileOut1=new File("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt");
				fileOut1.delete();
				File file = new File("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt");
		        BufferedWriter output = new BufferedWriter(new FileWriter(file));
		        output.write(values[2]);
		        output.close();
				Server_Main.VERSION= Integer.parseInt(values[1]);
				
				file_out.add("Writelock by "+client.getInetAddress().getCanonicalHostName());
				
				Server_Main.rwl.writeLock().unlock();
			}
			
			}
			
			if(rec_msg.equalsIgnoreCase("replica"))
			{
				output_client.println(Server_Main.VERSION);
			}
			
			
			
			client.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
