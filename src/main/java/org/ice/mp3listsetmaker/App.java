package org.ice.mp3listsetmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.ice.media.m3u.MediaFile;
import org.ice.media.m3u.io.M3UWriter;
import org.ice.mp3listsetmaker.files.FileFinder;
import org.ice.mp3listsetmaker.setlist.SetListClient;
import org.ice.mp3listsetmaker.setlist.TracksExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * App to generate m3u list from setlist.fm args[0] id list args[1] path to
 * music library args[2] name of the list
 */
public class App {
	
	private static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		List<String> list = new ArrayList<>();

		List<MediaFile> mediaFiles = null;

		Properties prop = new Properties();
		String data = "";
		if (args[0] != null && args[1] != null && args[2] != null) {
			prop = loadProperties();
		}

		try {
			if (!prop.isEmpty()) {
				data = SetListClient.query(args[0], prop.getProperty("setlist.URL"),
						prop.getProperty("setlist.APIKEY"));
			}
			if (data != null) {
				list = TracksExtractor.extractTracks(data);
			}
			if (!list.isEmpty()) {
				mediaFiles = FileFinder.findMediaFiles(list, getAudioFiles(args[1]));
				processMediaFiles(mediaFiles);
				M3UWriter.writeList(true, true, "/", args[2], mediaFiles);
			}
		} catch (Exception e) {
			logger.error("Error on execute: {}",e);
		}
	}

	private static void processMediaFiles(List<MediaFile> mediaFiles) {

		for (Iterator<MediaFile> iterator = mediaFiles.iterator(); iterator.hasNext();) {
			MediaFile mediaFile = iterator.next();
			if (mediaFile.getUrl().equals("not found")) {
				System.out.println(String.format("Track: %s not found", mediaFile.getName()));
			}
		}
	}

	private static Properties loadProperties() {
		//try (InputStream input = new FileInputStream("config.properties")) {
		try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")) {	
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			return prop;
		} catch (IOException ex) {
			logger.error("Sorry, unable to find config.properties");
			return new Properties();
		}
	}

	private static List<Mp3File> getAudioFiles(String path) {

		List<Mp3File> mp3Files = new ArrayList<>();
		Collection<File> files = FileUtils.listFiles(new File(path), new RegexFileFilter("^(.*?)"),
				DirectoryFileFilter.DIRECTORY);

		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			try {
				if (file.getName().toLowerCase().endsWith("mp3")) {
					Mp3File mp3File = new Mp3File(file);
					mp3Files.add(mp3File);
				}
			} catch (UnsupportedTagException| InvalidDataException | IOException e) {
				logger.error( "Error on execute: {}",e);
			}
		}
		return mp3Files;
	}

}
