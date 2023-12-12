package org.ice.mp3listsetmaker.test.setlist;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.ice.media.m3u.MediaFile;
import org.ice.media.m3u.io.M3UWriter;
import org.ice.mp3listsetmaker.files.FileFinder;
import org.ice.mp3listsetmaker.setlist.SetListClient;
import org.ice.mp3listsetmaker.setlist.TracksExtractor;
import org.junit.jupiter.api.Test;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

class TracksExtractorTest {

	@Test
	void test() {
		String data = SetListClient.query("4ba17b1e", "https://api.setlist.fm/rest/1.0/setlist/",
				"eOn9MTWhSOMc3SBpfE-1chPDcGyGy461tXqd");

		List<String> list = TracksExtractor.extractTracks(data);

		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}

		List<Mp3File> mp3Files = new ArrayList<>();
		Collection<File> files = FileUtils.listFiles(new File("/mnt/datos/etc/mi musica/Blur/"),
				new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);

		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			try {
				if (file.getName().toLowerCase().endsWith("mp3")) {
					Mp3File mp3File = new Mp3File(file);
					mp3Files.add(mp3File);
				}
			} catch (UnsupportedTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		List<MediaFile> mediaFiles = FileFinder.findMediaFiles(list, mp3Files);
		
		assertNotNull(mediaFiles);

		for (Iterator<MediaFile> iterator = mediaFiles.iterator(); iterator.hasNext();) {
			MediaFile mediaFile = iterator.next();
			System.out.println(String.format("%s - %s", mediaFile.getName(), mediaFile.getUrl()));
		}

		try {
			M3UWriter.writeList(true, true, "/", "blur", mediaFiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
