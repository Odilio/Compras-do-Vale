package util;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;

public class MandrillTeste {

	public static void main(String[] args) {
		MandrillApi mandrillApi = new MandrillApi("vYKpAHel9JppzlxCWB5-bw");
		
		MandrillUserInfo user;
		try {
			user = mandrillApi.users().info();
			// pretty-print w/ gson
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println( gson.toJson(user) );
		} catch (MandrillApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
}
