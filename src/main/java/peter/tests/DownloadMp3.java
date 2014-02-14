package peter.tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadMp3 {

	private final static String USER_AGENT = "Mozilla/5.0";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://mp3-island.in/site/evaluate";
		sendPost(url, "id=25312548");
	}


	// HTTP POST request
	private static void sendPost(String url, String urlParameters) {
 
		try {
		
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
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}	
	public static void downloadSong() {
		InputStream is = null;
		URL url = null;
		try {
			FileOutputStream bais = new FileOutputStream(new File("song.mp3"));
			url = new URL("http://api.mp3-island.in/api.php?token=a468bPYPMBhmv1ehaDI8HyrWd3B751UW&method=download&file=c4bad748bc25d0af55085f87ceab8038");
		  is = url.openStream ();
		  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
		  int n;

		  while ( (n = is.read(byteChunk)) > 0 ) {
		    bais.write(byteChunk, 0, n);
		  }
		  System.out.println(">>>> Bais"+ bais);
		} catch (Exception e) {
		  System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
		  e.printStackTrace ();
		  // Perform any other exception handling that's appropriate.
		}
		finally {
		  if (is != null) { try{is.close();}catch(Exception e){} }
		}
		
	}

}
