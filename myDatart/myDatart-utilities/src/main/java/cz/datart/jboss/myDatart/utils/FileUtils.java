package cz.datart.jboss.myDatart.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileUtils {

	/**
	 * Přečte soubor s input stremu po řádcích
	 * @param input soubr
	 * @return obsah souboru po řádcích
	 * @throws IOException
	 */
	public static String readFile(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining(String.format("%n")));//"\n" System.lineSeparator()
        }
    }
}
