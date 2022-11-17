package org.ice.mp3listsetmaker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.ice.media.m3u.MediaFile;
import org.ice.media.m3u.io.M3UWriter;
import org.ice.mp3listsetmaker.files.FileFinder;
import org.ice.mp3listsetmaker.setlist.SetListClient;
import org.ice.mp3listsetmaker.setlist.TracksExtractor;

/**
 * App to generate m3u list from setlist.fm 
 * args[0] id list 
 * args[1] path to music library
 * args[2] name of the list
 */
public class App {
	
	
	public static void main(String[] args) {

		if (args[0] != null && args[1] != null && args[2] != null) {

			Properties prop = loadProperties();
			String data = "";
			try {
				if (!prop.isEmpty()) {
					data = SetListClient.query(args[0], prop.getProperty("setlist.URL"),
							prop.getProperty("setlist.APIKEY"));
					if (data != null) {
						List<String> list = TracksExtractor.extractTracks(data);
						if (list != null) {
							List<MediaFile> mediaFiles = FileFinder.findMediaFiles(list, args[1]);
							for (Iterator<MediaFile> iterator = mediaFiles.iterator(); iterator.hasNext();) {
								MediaFile mediaFile =  iterator.next();
								if(mediaFile.getUrl().equals("not found")) {
									System.out.println(String.format("Track: %s not found", mediaFile.getName()));
								}
							}
							
							M3UWriter.writeList(true, true, "/", args[2], mediaFiles);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Properties  loadProperties() {
		try (InputStream input = new FileInputStream("config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			return prop;
		} catch (IOException ex) {
			System.out.println("Sorry, unable to find config.properties");
			//logger.error("Error get connection to for %s", ex);
			return null;
		}
	}
	
}
