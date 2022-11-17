package org.ice.mp3listsetmaker.test.setlist;

import java.util.Iterator;
import java.util.List;

import org.ice.media.m3u.MediaFile;
import org.ice.media.m3u.io.M3UWriter;
import org.ice.mp3listsetmaker.files.FileFinder;
import org.ice.mp3listsetmaker.setlist.SetListClient;
import org.ice.mp3listsetmaker.setlist.TracksExtractor;
import org.junit.jupiter.api.Test;

class TracksExtractorTest {

	@Test
	void test() {
		String data = SetListClient.query("23b1183f", "https://api.setlist.fm/rest/1.0/setlist/",
				"eOn9MTWhSOMc3SBpfE-1chPDcGyGy461tXqd");

		List<String> list = TracksExtractor.extractTracks(data);

		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}

		List<MediaFile> mediaFiles = FileFinder.findMediaFiles(list, "/mnt/datos/etc/mi musica/Guns n' Roses/");

		for (Iterator<MediaFile> iterator = mediaFiles.iterator(); iterator.hasNext();) {
			MediaFile mediaFile = iterator.next();
			System.out.println(String.format("%s - %s", mediaFile.getName(), mediaFile.getUrl()));
		}

		try {
			M3UWriter.writeList(true, true, "/", "guns", mediaFiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
