package org.ice.mp3listsetmaker.setlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SetListClient {

	public static String query(String setListId, String setListURL, String apiKey) {

		  try {

			URL url = new URL(setListURL+setListId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("x-api-key", apiKey);

			if (conn.getResponseCode() != 200) {
				System.out.println(url.toString());
				System.out.println(conn.getResponseMessage());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			
			StringBuilder data = new StringBuilder();
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				data.append(output);
			}

			conn.disconnect();
			
			return data.toString();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		  return null;	
		}
	
	}
