package source;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	static DBox_Connection dbx;
	static DbxClient client;
	static String webAuthURL;
	static HashMap<String, DbxEntry> FILEINFO;
	private JTextField textField;
	static DefaultTableModel model;
	static boolean operation = false;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		dbx = new DBox_Connection();
		FILEINFO = new HashMap<>();
		model = new DefaultTableModel(new String[]{"SNo", "FileName"}, 0);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//get link to authorize and make it visible to user
					webAuthURL = dbx.bindConnectionURL();
										
					//make the window visible
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 502, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLink = new JLabel("Link:");
		lblLink.setBounds(10, 11, 27, 14);
		contentPane.add(lblLink);
		
		JLabel lblAuthorizationcode = new JLabel("AuthorizationCode:");
		lblAuthorizationcode.setBounds(10, 36, 94, 14);
		contentPane.add(lblAuthorizationcode);
		
		textField = new JTextField();
		textField.setBounds(113, 33, 226, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		final JRadioButton rdbtnSec = new JRadioButton("Seconds");
		rdbtnSec.setBounds(10, 295, 70, 23);
		
		final JRadioButton rdbtnMinutes = new JRadioButton("Minutes");
		rdbtnMinutes.setBounds(10, 271, 109, 23);
		
		final JRadioButton rdbtnHours = new JRadioButton("Hours");
		rdbtnHours.setBounds(10, 245, 109, 23);
		
		final List list = new List();
		list.setBounds(10, 59, 197, 180);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client = dbx.authorizeURL(textField.getText());
				
				if(client != null)
				{
					//model = (DefaultTableModel) table.getModel();
					//model.setColumnIdentifiers(new String[]{"SNo", "FileName"});
					dbx.getFileNames();
					
					Set set = FILEINFO.entrySet();
				    Iterator i = set.iterator();
				    int j =0;
				    System.out.println("Files:");
				    while(i.hasNext()) {
				         Map.Entry me = (Map.Entry)i.next();
				         list.add(me.getKey().toString());
				         System.out.println(me.getKey().toString());
				    }
				    operation = true;
				    
				}
				
			}
		});
		btnSubmit.setBounds(371, 33, 83, 20);
		contentPane.add(btnSubmit);
		
		JButton btnShare = new JButton("Share");
		btnShare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (operation)
				{
					String filename = list.getSelectedItem();
					if(filename != null)
					{
						final DbxEntry file = FILEINFO.get(filename);
						final ArrayList<String> temp_filename = new ArrayList<>();
						int random =(int) Math.random() * 10;
						try {
							
							
							//download the file
							try {
								FileOutputStream outputStream = new FileOutputStream(file.name);
							    DbxEntry.File downloadedFile = client.getFile(file.path, null,
							        outputStream);
							    System.out.println("downloaded: " + downloadedFile.name);
							} catch(Exception e) {
							    e.printStackTrace();
							}
							
							//upload it again
							System.out.println("Uploading: "+random+file.name);
							
							File inputFile = new File(file.name);
							
							try {
								FileInputStream inputStream = new FileInputStream(inputFile);
							    DbxEntry.File uploadedFile = client.uploadFile("/"+random+filename,
							        DbxWriteMode.add(), inputFile.length(), inputStream);
							    System.out.println("Uploaded: " + uploadedFile.toString());
							    temp_filename.add(uploadedFile.path);
							} catch(Exception e) {
							    e.printStackTrace();
							}
							
							
							String URL = client.createShareableUrl(temp_filename.get(0));
							textField_3.setText(URL);
							
							//calculate time
							int sec = 0;
							int value = Integer.parseInt(textField_1.getText());
							if(rdbtnHours.isSelected())
								sec = 60*60*value;
							if(rdbtnMinutes.isSelected())
								sec = 60*value;
							if(rdbtnSec.isSelected())
								sec = value;
		
							//set timer
							Timer timer = new Timer();
							 timer.schedule(new TimerTask(){
								  public void run(){
									  try {
										client.delete(temp_filename.get(0));
									} catch (DbxException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									  
								  }
								  },sec * 1000);
						} catch (DbxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog (null, "Please select any one of the files from the list", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnShare.setBounds(162, 335, 70, 23);
		contentPane.add(btnShare);
		
		
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnHours);
		bg.add(rdbtnMinutes);
		bg.add(rdbtnSec);
		
		
		rdbtnSec.setSelected(true);
		contentPane.add(rdbtnSec);
		contentPane.add(rdbtnMinutes);
		contentPane.add(rdbtnHours);
		
		textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(47, 338, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setBounds(10, 339, 46, 14);
		contentPane.add(lblTime);
		
		textField_2 = new JTextField();
		textField_2.setBounds(47, 8, 407, 20);
		textField_2.setText(webAuthURL);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(10, 369, 444, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
	
		contentPane.add(list);
		
		setTitle("Main Window");
	}
}
