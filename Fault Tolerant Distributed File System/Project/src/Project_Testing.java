
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class Project_Testing {

	
	public static void main(String []args)
	{
		
		
	

	try {
		

		int flag=0;
		
		
		for (int k=1;k<7;k++)
		{
			flag=0;
			ArrayList<String> list = new ArrayList<String>();
			//System.out.println("The flag value is "+ flag);
			
			File file = new File(
					"/home/004/s/sx/sxg138930/workspace/AOS_PROJECT2_copy/output_net0"+k+".utdallas.edu.txt");
			FileInputStream fis = null;
			fis = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			int i = 0;
			String line = null;
			
		
		//String [] test_1=new String[8];

		while ((line = br.readLine()) != null) {
			
			
			   String [] parts=line.split(" ");
               list.add(parts[0]);
           
                 
			
			
}
		i=0;
		while (list.size() > i)
		{
			 
			if (list.get(i).equals("Readlock"))
			{
				//System.out.println("It is in Read Lock");
				if((i==0)||(!(list.get(i-1).equals("Writelock"))))
				{
					//System.out.println("it is checking for readlock condition");
					i++;
					continue;
				}
						
				
				else
				{
					
					flag=1;
					break;
				}
				
				
			}
			
			
			if (list.get(i).equals("Writelock"))
			{
				//System.out.println("It is in Write Lock");
				
				if((i==0)||((list.get(i-1).equals("ReadUnlock"))) || ((list.get(i-1).equals("WriteUnlock"))))
				{
				    i++;
					continue;
				
				}
				else
				{
					//System.out.println("Here it breaks for write");
					flag=1;
					break;
				}
				
				
			}
			
			else
			{
				i++;
			}
				
				
				
			}
				
				
		if(flag==1)
		{
			
			System.out.println("For node0"+k+" It is not in correct order");
		}
		else
		{
			System.out.println("For node0"+k+" It is in correct order");
		}
		
		
		}
		
		
		
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

