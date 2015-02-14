/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Listening_Metaserver extends Thread {

	ServerSocket server = null;
	int port;
	
	//empty constructor
	Listening_Metaserver()
	{
		
	}
	
	Listening_Metaserver(int p)
	{
		port = p;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
	
			//server listening to port no.
			
			server = new ServerSocket(port);
			while(true)
			{
				//if any client comes, accept it and run a separate thread
				Socket client = server.accept();
				MetaServer_Thread thread = new MetaServer_Thread(client);
				thread.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
