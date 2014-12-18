package Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import com.sun.mail.util.MailConnectException;

public class EmailHelper {
	
	private Config testConfig;
	private String Host;
	private String EmailId;
	private String Password;
	private Properties properties;
	private Session session;
	private Store store;
	int maximumRetries = 3;

	/**For setting email configuration
	 * @param EmailConfigDetails
	 */
	public EmailHelper(Config testConfig, int EmailConfigDetails) {
		this.testConfig = testConfig;
		TestDataReader dataReader = new TestDataReader(testConfig, "EmailConfig");
		EmailId = dataReader.GetData(EmailConfigDetails, "EmailID");
		Password = dataReader.GetData(EmailConfigDetails, "Password");
		Host = dataReader.GetData(EmailConfigDetails, "Host");
		properties = new Properties();
		properties.setProperty("mail.store.protocol", "imaps");
		initiateSession();
	}

	/**Gets unread messages from the inbox
	 * Recent 10 emails are returned in case of more than 10
	 * @return list of messages
	 */
	public List<Message> getUnreadMessages() {

		try {
			Folder inbox = store.getFolder("INBOX");
			// Set as read write so that messages accessed gets set to read
			inbox.open(Folder.READ_WRITE);
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message[] messages = inbox.search(unseenFlagTerm);
			List<Message> messagesList = Arrays.asList(messages);
			if (messagesList.size() > 10) {
				return messagesList.subList(messagesList.size() - 10,
						messagesList.size());
			} else
				return messagesList;

		} catch (MessagingException e) {
			testConfig.logException(e);
			return null;
		}
	}
	
	/**Initiates a session with specified information
	 * For email 
	 * 
	 */
	public void initiateSession() {
		int retries = 3;
		int countOfTries = 0;
		while (countOfTries < retries) {
			try {
				testConfig.logComment("Initiating session for email id "
						+ EmailId);
				session = Session.getInstance(properties, null);
				store = session.getStore();
				store.connect(Host, EmailId, Password);
				testConfig.logComment("Session iniated successfully");
				countOfTries = retries;
			} catch (MailConnectException exception) {
				if (countOfTries==retries) {
					testConfig.logException(exception);
				}
				else{
				testConfig
						.logComment("Could not connect with mail. Trying again.");
				countOfTries++;
				}
			} catch (MessagingException e) {
				testConfig.logException(e);
				countOfTries++;
			}
		}
	}

	/**To set other properties other than mail store protocol
	 * @param PropertyMap - Details of properties to be set in key, value pair
	 */
	public void setProperties(HashMap<String, String> PropertyMap) {

		for (String PropertyName : PropertyMap.keySet()) {
			properties.setProperty(PropertyName, PropertyMap.get(PropertyName));
		}
	}

	/**Gets unread messages by search term from inbox 
	 * @param SearchPattern - pattern in subject of mail
	 * @param waitRetryPeriod - maximum polling period
	 * @return list of matching messages
	 */
	public ArrayList<Message> getMessagesBySearchTerm(String SearchPattern,
			int waitRetryPeriod) {
		int waitInterval = waitRetryPeriod / maximumRetries;
		int tryCount = 0;
		while (tryCount++<maximumRetries) {
			try {
				testConfig.logComment("Searching messages by term "
						+ SearchPattern + " in subject");
				ArrayList<Message> FilteredMessages = new ArrayList<>();
				List<Message> messages = getUnreadMessages();
				for (Message message : messages) {
					if (message.getSubject().contains(SearchPattern))
						FilteredMessages.add(message);
				}
				if (FilteredMessages.size() > 0) {
					return FilteredMessages;
				} else if (FilteredMessages.size() == 0) {
					Browser.wait(testConfig, waitInterval);
				}
			} catch (MessagingException e) {
				testConfig.logException(e);
				return null;
			}
		}
		testConfig.logFail("Failed to find message by term "+ SearchPattern);
		return null;
	}
	
	/**Gets unread messages by search term from inbox 
	 * @param SearchPattern - pattern in content of mail
	 * @param waitRetryPeriod - maximum polling period
	 * @return list of matching messages
	 */
	public ArrayList<Message> getMessagesBySearchTermInContent(String SearchPattern,
			int waitRetryPeriod) {
		
		int waitInterval = waitRetryPeriod / maximumRetries;
		int tryCount = 0;
		while (tryCount++<maximumRetries) {
			try {
				testConfig.logComment("Searching messages by term "
						+ SearchPattern + " in content");
				ArrayList<Message> FilteredMessages = new ArrayList<>();
				List<Message> messages = getUnreadMessages();
				String MessageContent;
				for (Message message : messages) {
					try{
					Multipart multipart = (Multipart) message.getContent();
					MessageContent =multipart.getBodyPart(1).getContent().toString();}
					catch(ClassCastException e){
						MessageContent = (String) message.getContent();
					}
					testConfig.logComment(MessageContent);
					if (MessageContent.contains(SearchPattern))
						FilteredMessages.add(message);
				}
				if (FilteredMessages.size() > 0) {
					return FilteredMessages;
				} else if (FilteredMessages.size() == 0) {
					Browser.wait(testConfig, waitInterval);
				}
			} catch (MessagingException | IOException e) {
				testConfig.logException(e);
				return null;
			}
		}
		testConfig.logFail("Failed to find message by term "+ SearchPattern);
		return null;
	}
}
