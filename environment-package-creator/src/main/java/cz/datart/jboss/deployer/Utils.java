package cz.datart.jboss.deployer;

import java.io.File;
import java.nio.file.Path;

public class Utils {

	public static <T> boolean isArrayEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	public static String getFilePathAsString(String[] pathParts) {

		if (isArrayEmpty(pathParts)) {
			return "."; // return current dir
		}

		String folderSeparator = File.separator;

		return String.join(folderSeparator, pathParts);
	}

	public static boolean isStringEmpty(String text) {

		return text == null || text.trim().isEmpty();
	}

	public static boolean pathExists(Path path) {

		return path != null && path.toFile().exists();
	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}

			}

		}
		// either file or an empty directory
		System.out.println("removing file or directory : " + dir.getName());

		return dir.delete();

	}

	public static String getFileName(Path filePath) {
		
		return String.format("%s", filePath.getFileName());
	}

}
