package org.ice.mp3listsetmaker.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ice.media.m3u.MediaFile;

public class FileFinder {

	public static List<MediaFile> findMediaFiles(List<String> tracks, String mediaPath) {
		Path path = Paths.get(mediaPath);

		List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
		int position = 1;
		for (Iterator<String> iterator = tracks.iterator(); iterator.hasNext();) {
			String string = iterator.next();

			try {
				if (!string.equals("")) {
					List<Path> foundTracks = findByFileName(path, string);
					if (foundTracks.size() > 0) {
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
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return mediaFiles;
	}

	private static List<Path> findByFileName(Path path, String fileName) throws IOException {

		List<Path> result;
		try (Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE, (p, basicFileAttributes) -> p.getFileName()
				.toString().replaceAll("[^A-Za-z0-9 éÉñÑ]", "").toUpperCase().contains(fileName.replaceAll("[^A-Za-z0-9 éÉñÑ]", "").toUpperCase()))) {
			result = pathStream.collect(Collectors.toList());
		}
		return result;

	}

}
