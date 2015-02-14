/*

Author: Saranath G Raju
Course: Advanced Operating System
University: University of Texas at Dallas

*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Listening extends Thread {

	ServerSocket server = null;
	int port;
	Listening()
	{
		
	}
	
	Listening(int p)
	{
		port = p;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		
			//creating a server end and if any client comes, accept it and perform operation in separate thread
			server = new ServerSocket(port);
			while(true)
			{
				Socket client = server.accept();
				Server_Thread thread = new Server_Thread(client);
				thread.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
