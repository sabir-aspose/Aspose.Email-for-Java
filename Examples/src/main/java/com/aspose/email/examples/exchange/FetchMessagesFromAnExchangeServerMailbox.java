package com.aspose.email.examples.exchange;

import com.aspose.email.Attachment;
import com.aspose.email.EWSClient;
import com.aspose.email.ExchangeClient;
import com.aspose.email.ExchangeMessageInfo;
import com.aspose.email.ExchangeMessageInfoCollection;
import com.aspose.email.ExchangeMessagePageInfo;
import com.aspose.email.IEWSClient;
import com.aspose.email.MailMessage;
import com.aspose.email.system.IDisposable;
import com.aspose.email.system.collections.generic.List;

public class FetchMessagesFromAnExchangeServerMailbox {

	public static void main(String[] args) {
		// Fetching Messages from an Exchange Server Mailbox
		fetchMessagesFromAnExchangeServerMailbox();
		
		// Uses Exchange Web Services (EWS) to connect and retrieve messages from the Exchange Server
		fetchMessagesFromAnExchangeServerMailboxUsingEWS();
		
		// Enumerating Messages with Paging in EWS
		enumerateMessagesWithPagingInEWS();
	}

	public static void fetchMessagesFromAnExchangeServerMailbox() {
		// Create instance of ExchangeClient class by giving credentials
		ExchangeClient client = new ExchangeClient("http://ex07sp1/exchange/Administrator", "username", "password", "domain");

		// Call ListMessages method to list messages info from Inbox
		ExchangeMessageInfoCollection msgCollection = client.listMessages(client.getMailboxInfo().getInboxUri());

		// Loop through the collection to get Message URI
		for (ExchangeMessageInfo msgInfo : msgCollection) {
			String strMessageURI = msgInfo.getUniqueUri();

			// Now get the message details using FetchMessage()
			MailMessage msg = client.fetchMessage(strMessageURI);

			// Display message details
			System.out.println("Subject: " + msg.getSubject());
			System.out.println("HTML Body: " + msg.getHtmlBody());
			System.out.println("Number of attachments: " + msg.getAttachments().size());
			for (Attachment att : msg.getAttachments()) {
				System.out.println("Attachment Name: " + att.getName());
			}
		}
	}

	public static void fetchMessagesFromAnExchangeServerMailboxUsingEWS() {
		// Create instance of ExchangeWebServiceClient class by giving credentials
		IEWSClient client = EWSClient.getEWSClient("https://outlook.office365.com/ews/exchange.asmx", "testUser", "pwd", "domain");

		// Call ListMessages method to list messages info from Inbox
		ExchangeMessageInfoCollection msgCollection = client.listMessages(client.getMailboxInfo().getInboxUri());

		// Loop through the collection to get Message URI
		for (ExchangeMessageInfo msgInfo : msgCollection) {
			String strMessageURI = msgInfo.getUniqueUri();

			// Now get the message details using FetchMessage()
			MailMessage msg = client.fetchMessage(strMessageURI);

			// Display message details
			System.out.println("Subject: " + msg.getSubject());
			//Console.WriteLine("HTML Body: " + msg.HtmlBody);

			// How many attachments
			System.out.println("Number of attachments: " + msg.getAttachments().size());

			// List the attachments
			for (Attachment att : msg.getAttachments()) {
				System.out.println("Attachment Name: " + att.getName());
			}
		}
	}

	public static void enumerateMessagesWithPagingInEWS() {
		IEWSClient client = EWSClient.getEWSClient("exchange.domain.com", "username", "password");
		try {
			// Number of items per page
			int itemsPerPage = 5;
			
			// Create a storage list
			List<ExchangeMessagePageInfo> pages = new List<ExchangeMessagePageInfo>();
			ExchangeMessagePageInfo pageInfo = client.listMessagesByPage(client.getMailboxInfo().getInboxUri(), itemsPerPage);
			pages.addItem(pageInfo);

			// Retrieve further pages
			while (!pageInfo.getLastPage()) {
				pageInfo = client.listMessagesByPage(client.getMailboxInfo().getInboxUri(), itemsPerPage, pageInfo.getPageOffset() + 1);
				pages.addItem(pageInfo);
			}

			// Verify the number of items retrieved
			int retrievedItems = 0;
			for (ExchangeMessagePageInfo pageCol : (Iterable<ExchangeMessagePageInfo>) pages)
				retrievedItems += pageCol.getItems().size();

			System.out.println("Items retrieved: " + retrievedItems);
		} finally {
			if (client != null)
				((IDisposable) client).dispose();
		}
	}
}
