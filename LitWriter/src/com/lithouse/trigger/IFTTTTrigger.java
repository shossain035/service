package com.lithouse.trigger;

import com.lithouse.smtp.EmailSenderImpl;

public class IFTTTTrigger implements Trigger {

	@Override
	public void triggerEventAsync ( Event event ) {
		String emailSubject = event.getData ( ).startsWith ( "#" ) ? event.getData ( ) : "#" + event.getData ( );
		new EmailSenderImpl ( ).sendEmailAsync ( emailSubject, "", event.getOwnerIFTTTEmailAddress ( ), "trigger@recipe.ifttt.com" );
	}

}
