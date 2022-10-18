package org.ice.mp3listsetmaker;

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
 * Hello world!
 *
 */
public class App {
	
	
	public static void main(String[] args) {

		if (args[0] != null && args[1] != null && args[2] != null) {

			Properties prop = new Properties();
			try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")) {

				prop = new Properties();

				if (input == null) {
					System.out.println("Sorry, unable to find config.properties");
					return;
				}
				prop.load(input);

			} catch (IOException ex) {
				ex.printStackTrace();
			}

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

}
