package com.lithouse.smtp;

import javax.mail.*;

import java.util.Properties;

import javax.mail.internet.*;

import com.lithouse.common.util.Global;
import com.lithouse.writer.WriterExecutor;

public class EmailSenderImpl implements EmailSender {
	static final String FROM = Global.getConfig ( ).getString ( "lithouse.info.smptp.from" );
    static final String RECEPIENTS = Global.getConfig ( ).getString ( "lithouse.info.smptp.recepients" ) ;          
    
    static final String SMTP_USERNAME = Global.getConfig ( ).getString ( "lithouse.info.smptp.username" );  
    static final String SMTP_PASSWORD = Global.getConfig ( ).getString ( "lithouse.info.smptp.password" );;  
    
    static final String HOST = Global.getConfig ( ).getString ( "lithouse.info.smptp.host" );    
    static final int PORT = 587;

    public void sendEmailAsync ( final String subject, final String messageBody ) {
    	WriterExecutor.submit ( new Runnable ( ) {
			
			@Override
			public void run () {
				Properties props = System.getProperties ( );
		    	props.put ( "mail.transport.protocol", "smtp" );
		    	props.put ( "mail.smtp.port", PORT ); 
		    	
		    	props.put ( "mail.smtp.auth", "true" );
		    	props.put ( "mail.smtp.starttls.enable", "true" );
		    	props.put ( "mail.smtp.starttls.required", "true" );

		    	Session session = Session.getDefaultInstance ( props );
		    	Transport transport = null;
		                    
		        try {
		        	MimeMessage message = new MimeMessage ( session );
		        	message.setFrom ( new InternetAddress ( FROM ) );
		        	message.setRecipients ( Message.RecipientType.TO, RECEPIENTS );
		        	message.setSubject ( subject );
		        	message.setContent ( messageBody, "text/plain" );
		        	
		        	transport = session.getTransport ( );
		        	
		        	transport.connect ( HOST, SMTP_USERNAME, SMTP_PASSWORD );        	
		        	transport.sendMessage ( message, message.getAllRecipients ( ) );            
		        } catch ( Exception e ) {
		        	Global.getLogger ( ).error ( "Failed to send email", e );
		        	
		        } finally {
		            if ( transport != null ) {
		            	try {
		            		transport.close();
		            	} catch ( MessagingException e ) {					
		            		e.printStackTrace();
		            	}
		            }
		        }

			}
		} );
     }

}
