/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.weetech.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Utility class for reading and writing configuration properties files.
 */
public class ConfigApp {
	
	
	/**
     * Constructs a new {@code ConfigApp} instance.
     */
	public ConfigApp() {
		
	}

	/**
     * Reads and loads application configuration properties from a specified file.
     *
     * @param filename the path to the configuration file to be read
     * @return a {@link Properties} object containing the key-value pairs loaded from the file
     * @throws FileNotFoundException if the specified file does not exist
     * @throws IOException           if an I/O error occurs while reading the file
     * @throws URISyntaxException    if a syntax error occurs, though not directly thrown 
     *                               by the current file operations
     */
	public static Properties read(String filename) throws FileNotFoundException, IOException, URISyntaxException {

		File configFile = new File(filename);

		try (FileReader reader = new FileReader(configFile);) {

			Properties props = new Properties();
			props.load(reader);
			return props;
		}
	}

	/**
     * Saves the specified properties object to a configuration file, overwriting existing content.
     * The file includes a comment containing the current system time in milliseconds.
     *
     * @param filename the path to the configuration file where properties should be saved
     * @param props    the {@link Properties} object containing the configuration data to write
     * @return {@code true} if the save operation completes successfully
     * @throws IOException if an I/O error occurs while writing to the file
     */
	public static boolean save(String filename, Properties props) throws IOException {
		try (FileWriter writer = new FileWriter(filename);) {
			props.store(writer, String.valueOf(System.currentTimeMillis()));
		}
		return true;
	}

}
