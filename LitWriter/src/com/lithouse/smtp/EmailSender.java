package com.lithouse.smtp;

public interface EmailSender {
	void sendEmailAsync ( String subject, String messageBody );
}
