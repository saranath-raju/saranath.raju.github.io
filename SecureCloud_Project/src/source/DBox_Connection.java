package source;

import java.util.Locale;

import javax.activation.MailcapCommandMap;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class DBox_Connection {
	
	
	//key and secret
	final String APP_KEY = "z5lz3a0d5prrcts";
    final String APP_SECRET = "27m64imijflhxmw";
    
    DbxAppInfo appInfo;
    DbxRequestConfig config;
    DbxWebAuthNoRedirect webAuth;
    DbxClient client;
    
    /*
     * @param - nil
     * @description - returns the link for the user to authorize
     */
    public String bindConnectionURL() {

    	appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		config = new DbxRequestConfig("SecureCloud_Project", Locale.getDefault().toString());
	    webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		
	    //getting url for user autherntication
	    return webAuth.start();
	}
    
    
    public DbxClient authorizeURL(String code) {
    	DbxAuthFinish authFinish = null;
		try {
			authFinish = webAuth.finish(code);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String accessToken = authFinish.accessToken;
	    client = new DbxClient(config, accessToken);
	    
	    return client;
	}


	public void getFileNames(){
		// TODO Auto-generated method stub
		DbxEntry.WithChildren listing = null;
		try {
			listing = client.getMetadataWithChildren("/");
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (DbxEntry child : listing.children) {
	        MainWindow.FILEINFO.put(child.name, child);
	    }
	}

}
