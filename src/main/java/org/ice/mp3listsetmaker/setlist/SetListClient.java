package org.ice.mp3listsetmaker.setlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetListClient {
	
	private static Logger logger = LoggerFactory.getLogger(SetListClient.class);
	
	private SetListClient() {
		super();
	}

	public static String query(String setListId, String setListURL, String apiKey) {

		  try {

			URL url = new URL(setListURL+setListId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("x-api-key", apiKey);

			if (conn.getResponseCode() != 200) {
				logger.info(url.toString());
				logger.info(conn.getResponseMessage());
				return "";
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			
			StringBuilder data = new StringBuilder();
			logger.info("Output from Server ....");
			while ((output = br.readLine()) != null) {
				data.append(output);
			}

			conn.disconnect();
			
			return data.toString();

		  } catch (IOException e) {
			  	logger.error("Error on call client", e);
		  }
		  return null;	
		}
	
	}
