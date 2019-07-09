package com.spring;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.stereotype.Service;

@Service
public class StoreSession {	
	private static StoreSession single_instance = null; 
	
	public Store createSession() throws IOException, MessagingException {
		try {
			InputStream input = new FileInputStream("src/main/resources/application.properties");
			Properties props = new Properties();

			props.load(input);

			Session session = Session.getDefaultInstance(props, null);

			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", "jnat51.jg@gmail.com", "myname51");

			return store;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static StoreSession getInstance() 
    { 
        if (single_instance == null) {
            single_instance = new StoreSession(); 
        }
  
        return single_instance; 
    } 
}
