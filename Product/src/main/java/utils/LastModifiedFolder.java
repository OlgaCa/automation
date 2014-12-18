package utils;

import java.io.File;
import java.io.IOException;

/**
 * This File will give the latest dynamically generated folder name which will
 * be used by ParsingHTML file to add dynamic folder name.
 * 
 * @author shishir.dwivedi
 * 
 */
public class LastModifiedFolder {
	public static String getLastModifiedFolderName() throws Exception {
		File file = new File(".");
		System.out.println("Current dir : " + file.getCanonicalPath());
		String path = file.getCanonicalPath() + "\\test-output\\archive";
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files.length == 0) {
			throw new Exception("No file found");
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}

		}
		return lastModifiedFile.getName();

	}
	
	
	public static String getCompletePathOfFile() throws Exception {
		File file = new File(".");
		System.out.println("Current dir : " + file.getCanonicalPath());
		String path = file.getCanonicalPath() + "\\test-output\\archive";
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files.length == 0) {
			throw new Exception("No file found");
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}

		}
		return lastModifiedFile.toString();
		
	}

}