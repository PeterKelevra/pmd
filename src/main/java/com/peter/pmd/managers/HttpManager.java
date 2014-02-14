package com.peter.pmd.managers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("httpManager")
public class HttpManager {
	

	private static final Logger LOG = LoggerFactory.getLogger(HttpManager.class);
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	/**
	 * Realiza una llamada post
	 * @param url
	 * @param urlParameters
	 */
	public String doPost(String url, String urlParameters) throws Exception{
 
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection)obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		LOG.debug("\nSending 'POST' request to URL : " + url);
		LOG.debug("Post parameters : " + urlParameters);
		LOG.debug("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return response.toString();
 
	}	


}
