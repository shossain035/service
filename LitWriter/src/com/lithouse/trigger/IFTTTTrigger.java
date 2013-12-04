package com.lithouse.trigger;

import com.lithouse.smtp.EmailSenderImpl;

public class IFTTTTrigger implements Trigger {

	@Override
	public void triggerEventAsync ( Event event ) {
		new EmailSenderImpl ( ).sendEmailAsync ( "#" + event.getData ( ), "", "trigger@ifttt.com" );
	}

}
