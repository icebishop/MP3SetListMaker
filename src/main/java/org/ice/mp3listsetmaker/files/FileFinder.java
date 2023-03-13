package org.ice.mp3listsetmaker.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ice.media.m3u.MediaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

public class FileFinder {
	
	private static Logger logger = LoggerFactory.getLogger(FileFinder.class);

	private FileFinder() {
		super();
	}

	public static List<MediaFile> findMediaFiles(List<String> tracks, List<Mp3File> files ) {
		

		List<MediaFile> mediaFiles = new ArrayList<>();
		int position = 1;
		List<Path> foundTracks = new ArrayList<>();
		for (Iterator<String> iterator = tracks.iterator(); iterator.hasNext();) {
			String string = iterator.next();

			try {
				if (!string.equals("")) {
					foundTracks = findByFileName(files, string);
				}
				if (!foundTracks.isEmpty()) {
					for (Iterator<Path> iterator2 = foundTracks.iterator(); iterator2.hasNext();) {
						Path path2 = iterator2.next();
						if (Files.isRegularFile(path2)) {
							MediaFile mediaFile = new MediaFile();
							mediaFile.setPosition(position);
							mediaFile.setName(string);
							mediaFile.setUrl(path2.toString());
							mediaFiles.add(mediaFile);
							position++;
						}
					}
				} else {
					MediaFile mediaFile = new MediaFile();
					mediaFile.setPosition(position);
					mediaFile.setName(string);
					mediaFile.setUrl("not found");
					mediaFiles.add(mediaFile);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mediaFiles;

	}

	private static List<Path> findByFileName(List<Mp3File> files, String fileName) throws IOException {		
		ID3v1 id3v1Tag = null;
		List<Path> paths = new ArrayList<>();
		String tofind =  cleanString(fileName).replaceAll("[^A-Za-z0-9]", "").toUpperCase();
		for (Iterator<Mp3File> iterator = files.iterator(); iterator.hasNext();) {
			Mp3File mp3File =  iterator.next();
			
			if (mp3File.hasId3v1Tag()) {
				id3v1Tag =   mp3File.getId3v1Tag();				  
			}
			
			if(id3v1Tag != null) {
				String song = cleanString(id3v1Tag.getTitle()).replaceAll("[^A-Za-z0-9]", "").toUpperCase();
				logger.info(String.format("to find %s in song %s",tofind,song));
				if(song.contains(tofind)) {
					Path path = Paths.get(mp3File.getFilename());
					paths.add(path);
				}
			}			
		}
		return paths;
	}
	
	private static String cleanString(String text) {		
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
		text = text.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		    return text;
	}

}
