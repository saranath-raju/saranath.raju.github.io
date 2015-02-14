//this file is to do background listening
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
			server = new ServerSocket(port);
			while(true)
			{
				Socket client = server.accept();
				Server_Thread thread = new Server_Thread(client);
				thread.run();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
