package com.doopp.gauss.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ApplicationModule extends AbstractModule {

	private String propertiesConfig;

	public ApplicationModule(String propertiesConfig) {
		this.propertiesConfig = propertiesConfig;
	}

	@Override
	protected void configure() {
	}

	@Provides
	public Properties applicationProperties() {
		Properties properties = new Properties();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(propertiesConfig));
			properties.load(bufferedReader);
			return properties;
		}
		catch(IOException e) {
			return null;
		}
	}
}
