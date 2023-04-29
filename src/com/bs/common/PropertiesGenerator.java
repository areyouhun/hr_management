package com.bs.common;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesGenerator {

	public static void load(Properties properties) {
		final String path = PropertiesGenerator.class.getResource(Constants.SQL_PATH).getPath();
		try (FileReader fileReader = new FileReader(path)) {
			properties.load(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
