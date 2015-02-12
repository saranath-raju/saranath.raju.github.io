import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.management.timer.Timer;
import javax.sound.sampled.ReverbType;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;


public class Server_Main {

	//variables
	static int VERSION =1;
	static int VOTE;
	static int WQ,RQ;
	static boolean fault_flag=true;
	static final ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int port = 1000;
		
		
		final Properties prop = new Properties();
		try {
			
			int i;
			int Total_Vote=0;
			 final String PID="P"+InetAddress.getLocalHost().getHostName().substring(4, 5);
			 String host = null;
			System.out.println(PID);
			
			InputStream input = new FileInputStream("/home/004/s/sx/sxg138930/workspace/AOS_Project2/Config.properties"); 
			prop.load(input);
			
			//loading its values
			host =( prop.getProperty(PID+"HostName"));
			port = Integer.parseInt(prop.getProperty(PID+"PortNo"));
			VOTE =  Integer.parseInt(prop.getProperty(PID+"Vote"));
			
			//computing Quorum size
			for(i=1;i<=6;i++)
			{
				Total_Vote = Total_Vote + Integer.parseInt(prop.getProperty("P"+i+"Vote"));
			}
			WQ = (Total_Vote/2)+(Total_Vote/4) +1;
			RQ= Total_Vote/3 ;
			
			//background listening
			Listening s1 = new Listening(port);
			s1.start();
			
			
			//executing regular task
			new java.util.Timer().schedule(new java.util.TimerTask()
			{
				@Override
				public void run()
				{
					try
					
					{	boolean flag= false;
						boolean abort_flag=false;
						String h=InetAddress.getLocalHost().getHostName().substring(4, 5);
						System.out.println("Server "+ h+" Started...");
						
						//declaring event file ptr
						BufferedReader br = null;
						String sCurrentLine;
						br = new BufferedReader(new FileReader("/home/004/s/sx/sxg138930/workspace/AOS_Project2/Event0"+h+".txt"));
						
						//reading from event file 
						while ((sCurrentLine = br.readLine()) != null) {
							
							//printing name of the operation
							System.out.println("-------------------------------------------------------------------------------------");
							System.out.println(sCurrentLine);
							int k=0;
							//declaration
							Socket[] s = new Socket[10];
							BufferedReader[] input_client = new BufferedReader[10];
							PrintWriter[] output_client = new PrintWriter[10];
							
							
							//initialization
							Server_Thread.read=0;
							
							//1st case - READ
							if(sCurrentLine.equalsIgnoreCase("R"))
							{
								
								//temp variables
								int i=0;
								int[] rand = new int[6];
								int sum =VOTE;
								int[] version_nos= new int[10];
								int max=-1;
								int maxi=-1;
								String a=null;
								
								//selecting random processes to form quorum
								while(sum<RQ)
								{
									
									int y = 1 + (int)(Math.random() * ((6 - 1) + 1));
									if(y== Integer.parseInt(h))
									{
										continue;
									}
									if(i==0) 
									{
										rand[i]= y;
										i++;
										sum = sum + Integer.parseInt(prop.getProperty("P"+y+"Vote"));
									}
									else
										if((new Server_Main().find(rand, y)==-1))
										{
											rand[i]=y;
											i++;
											sum = sum + Integer.parseInt(prop.getProperty("P"+y+"Vote"));
										}
								}
								int j=i;
								
								//sending readlock
								System.out.println("Read Quorum members:");
								for(i=0;i<j;i++)
								{
									
									
									System.out.println(" "+rand[i]);
									
									//establishing connection to form quorum
									s[i]= new Socket(prop.getProperty("P"+rand[i]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[i]+"PortNo")));
									input_client[i]= new BufferedReader(new InputStreamReader(s[i].getInputStream()));
									output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
									output_client[i].println("Readlock by  "+InetAddress.getLocalHost().getHostName());
									
									//temp variables
									
									int counter;
									
									//exponential back off time
									if((a= input_client[i].readLine())==null)
									{
										System.out.println("Not receiving..Entering Exponential Backoff time.....");
										flag=true;
										k=i;
									}	
									//carry on regular task
									if(!flag)
									{
									
									version_nos[i]= Integer.parseInt(a);
									max= version_nos[0];
									maxi=0;
									}
									s[i].close();
								}
								
								//not received after expo backoff time - abort
								if(flag)
								{
									System.out.println("Aborting Read Operation");
									
									//if abort, sending unlock to other processes
									
									for(i=0;i<j;i++)
									{
										if(i!=k)
										{
											s[i]= new Socket(prop.getProperty("P"+rand[i]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[i]+"PortNo")));
											output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
											output_client[i].println("UnlockRead");
											s[i].close();
										}
									}
									
									flag=false;
									Thread.sleep(6*1000);
									continue;
								}
								if(!flag)
								{
									
								
								//finding maximum version no
								for(i=1;i<j;i++)
								{
									if(max<version_nos[i])
									{
										max = version_nos[i];
										maxi=i;
									}
								}
								
								
								//its version is latest
								if(Server_Main.VERSION>=max)
								{
									//read the content of file
									System.out.println("---> Read lock <---");
									BufferedReader br1 = null;
									String sCurrentLine1;
									br1 = new BufferedReader(new FileReader("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt"));
									System.out.println("---Contents---");
									sCurrentLine1 = br1.readLine();
									System.out.println(sCurrentLine1);
								
								}
								
								//getting value from other process
								else
								{
									s[9]= new Socket(prop.getProperty("P"+rand[maxi]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[maxi]+"PortNo")));
									input_client[9]= new BufferedReader(new InputStreamReader(s[9].getInputStream()));
									output_client[9] = new PrintWriter(new OutputStreamWriter(s[9].getOutputStream()),true);
									output_client[9].println("Request");
									String temp;
									System.out.println("---> Read lock <---");
									System.out.println("---Contents---");
									while((temp=input_client[9].readLine())!=null)
									{
										System.out.println(temp);
									}
									
								}
								
								//read unlock
								System.out.println("---> Read Unlock <---");
//								if(Server_Thread.read==0)
//									rwl.readLock().unlock();
								
								//sending readunlock
								for(i=0;i<j;i++)
								{
									s[i]= new Socket(prop.getProperty("P"+rand[i]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[i]+"PortNo")));
									input_client[i]= new BufferedReader(new InputStreamReader(s[i].getInputStream()));
									output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
									output_client[i].println("UnlockRead");
									s[i].close();
								}
								}
							}
							
							//2nd case - WRITE
							if(sCurrentLine.substring(0, 1).equalsIgnoreCase("W"))
							{
								
								//temp variable
								int i=0;
								int[] rand = new int[6];
								int sum =VOTE;
								
								//selecting random processes to form quorum
								while(sum<WQ)
								{
									//random no.
									int y = 1 + (int)(Math.random() * ((6 - 1) + 1));
									
									//checking its value
									if(y== Integer.parseInt(h))
									{
										continue;
									}
									
									
									if(i==0) 
									{
										rand[i]= y;
										i++;
										sum = sum + Integer.parseInt(prop.getProperty("P"+y+"Vote"));
									}
									else
										if((new Server_Main().find(rand, y)==-1))
										{
											rand[i]=y;
											i++;
											sum = sum + Integer.parseInt(prop.getProperty("P"+y+"Vote"));
										}
								}
								
								
								//acquiring lock
								rwl.writeLock().lock();
								System.out.println("---> Write lock <---");
								
								//removing old records
								File fileOut1=new File("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt");
								fileOut1.delete();
								
								
								//updating new records and version no.
								File file = new File("/home/004/s/sx/sxg138930/workspace/AOS_Project2/"+InetAddress.getLocalHost().getHostName()+".txt");
						        BufferedWriter output = new BufferedWriter(new FileWriter(file));
						        output.write(sCurrentLine.substring(2));
						        output.close();
								VERSION++;
								
								int j=i;
								
								//sending writelock
								System.out.println("Write Quorum members:");
								for(i=0;i<j;i++)
								{
									 System.out.println(" "+rand[i]);
									s[i]= new Socket(prop.getProperty("P"+rand[i]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[i]+"PortNo")));
									input_client[i]= new BufferedReader(new InputStreamReader(s[i].getInputStream()));
									output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
									output_client[i].println("WriteLock By "+InetAddress.getLocalHost().getHostName()+":"+VERSION+":"+sCurrentLine.substring(2));
									s[i].close();
								}
								
								//unlock - write
								System.out.println("---> Write Unlock <---");
								rwl.writeLock().unlock();
								
								
								//sending unlock - write 
								for(i=0;i<j;i++)
								{
									s[i]= new Socket(prop.getProperty("P"+rand[i]+"HostName"), Integer.parseInt(prop.getProperty("P"+rand[i]+"PortNo")));
									output_client[i] = new PrintWriter(new OutputStreamWriter(s[i].getOutputStream()),true);
									output_client[i].println("UnlockWrite");
									s[i].close();
								}
							}
							
							
							//3rd case - DOWN
							if(sCurrentLine.equalsIgnoreCase("Down"))
							{
								fault_flag= false;
							}
							
							
							
							//4th case - UP 
							if(sCurrentLine.equalsIgnoreCase("Up"))
							{
								fault_flag = true;
							}
							
							Thread.sleep(6*1000);
							
						}
						
					}
					
					//2nd try- catch
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			,10*1000);
			
			
		}
		
		
		
		// 1st try- catch
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		new java.util.Timer().schedule(new java.util.TimerTask()
		{
			@Override
			public void run()
			{
				
				System.out.println("-----------------End of regular Task------------");
				
				System.out.println("Replica consistency  (any key)  :");
				Scanner s = new Scanner(System.in);
				String a = s.nextLine();
				new Replica().replica_function();
				
				File file;
				try {
					//file = new File("/home/004/s/sx/sxg138930/workspace/AOS_PROJECT2_copy/output_"+InetAddress.getLocalHost().getHostName()+".txt");
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/004/s/sx/sxg138930/workspace/AOS_PROJECT2_copy/output_"+InetAddress.getLocalHost().getHostName()+".txt", true)));
				    
			        for (String string : Server_Thread.file_out) {
			        	//System.out.println(string);
						//output1.write(string+"\n");
			        	out.println(string);
					}
			        out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				
			}	
		},40*1000);
		
			
	}//end of main

	
	
	//find function
	public int find(int[] array, int value) {
	    for(int i=0; i<array.length; i++) 
	         if(array[i] == value)
	             return i;
	    return -1;
	}
}
