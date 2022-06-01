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

public class ConfigApp {

	public static Properties read(String filename) throws FileNotFoundException, IOException, URISyntaxException {

		File configFile = new File(filename);

		try (FileReader reader = new FileReader(configFile);) {

			Properties props = new Properties();
			props.load(reader);
			return props;
		}
	}

	public static boolean save(String filename, Properties props) throws IOException {
		try (FileWriter writer = new FileWriter(filename);) {
			props.store(writer, String.valueOf(System.currentTimeMillis()));
		}
		return true;
	}

}
