package notifiers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.FileUtils;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;

import play.Play;
import play.mvc.Mailer;

public class Notifier extends Mailer {


	
    public static void welcomeMandrill(String email, String nome) throws Exception {
    	File outputFile = Play.getFile("/public/templateemail/welcome.html");
    	
    	String mail = "";
    	mail = FileUtils.readFileToString( outputFile, "Utf8");
    	
    	 MandrillApi mandrillApi = new MandrillApi("vYKpAHel9JppzlxCWB5-bw");
			
			// create your message
				MandrillMessage message = new MandrillMessage();
				message.setSubject("Cadastro Compras do Vale!");
				message.setHtml(mail);
				message.setAutoText(true);
				message.setFromEmail("odilio@comprasdovale.com.br");
				message.setFromName("Contato Compras do Vale!");
				// add recipients
				ArrayList<Recipient> recipients = new ArrayList<Recipient>();
				Recipient recipient = new Recipient();
				recipient.setEmail(email);
				recipient.setName(nome);
				recipients.add(recipient);
				message.setTo(recipients);
				message.setPreserveRecipients(true);
				ArrayList<String> tags = new ArrayList<String>();
				tags.add("Bem Vindo!");
				tags.add("Contato");
				message.setTags(tags);
				// ... add more message details if you want to!
				// then ... send
				try {
					MandrillMessageStatus[] messageStatusReports = mandrillApi
					        .messages().send(message, false);
				} catch (MandrillApiError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
    }

	
    public static boolean welcome(String email, String nome) throws Exception {
        
    	setFrom(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setReplyTo(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setSubject("Bem vindo %s",nome);
        addRecipient(email, new InternetAddress(email, "Bem Vindo"));
        return sendAndWait(email);
    }
    
    
    public static boolean contato(String user,String mail) throws Exception {
        setFrom(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setReplyTo(new InternetAddress("contato@comprasdovale.com.br", "Help"));
        setSubject("Welcome %s", "odilio");
        addRecipient(mail, new InternetAddress(mail, "Contato"));
        return sendAndWait(user);
    }
   
    public static boolean compraEfetuada(String email, String nome) throws Exception {
        setFrom(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setReplyTo(new InternetAddress("contato@comprasdovale.com.br", "Help"));
        setSubject("Contato Compras do Vale %s", nome);
        addRecipient(email, new InternetAddress(email, "Bem Vindo"));
        return sendAndWait(email);
    }
    
    public static boolean propor(String email, String nome) throws Exception {
        setFrom(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setReplyTo(new InternetAddress("contato@comprasdovale.com.br", "Help"));
        setSubject("Contato Compras do Vale %s", nome);
        addRecipient(email, new InternetAddress(email, "Proposta"));
        return sendAndWait(email);
    }
    
    
    public static boolean confirmacaoPagamento(String email, String nome) throws Exception {
        setFrom(new InternetAddress("contato@comprasdovale.com.br", "Contato Compras do Vale"));
        setReplyTo(new InternetAddress("contato@comprasdovale.com.br", "Help"));
        setSubject("Contato Compras do Vale %s", nome);
        addRecipient(email, new InternetAddress(email, "Proposta"));
        return sendAndWait(email);
    }
}

