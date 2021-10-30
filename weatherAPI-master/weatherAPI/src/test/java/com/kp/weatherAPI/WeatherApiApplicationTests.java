package com.kp.weatherAPI;

import com.google.gson.Gson;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest
class WeatherApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void checkAPI() throws IOException {
		URL url=new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=50&lon=19");
		HttpURLConnection httpConn= (HttpURLConnection) url.openConnection();
		//httpConn.addRequestProperty("User-agent","kpuda.github.com");
		httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
		System.out.println(httpConn.getContentType());
		InputStreamReader reader=new InputStreamReader(url.openStream());
		Weather weather=new Gson().fromJson(reader,Weather.class);
		System.out.println(weather);
	}

}
