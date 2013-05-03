package pl.edu.agh.io.coordinator.utils.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSonProxy {
	private static JSonProxy instance;

	private JSonProxy() {

	}

	public static synchronized JSonProxy getInstance() {
		if (instance == null)
			instance = new JSonProxy();
		return instance;
	}

	public synchronized long login(String user, String password) {
		String jsonString = getJSonString();
		JSONObject jsonObject;
		long sessionID;
		try {
			jsonObject= new JSONObject(jsonString);
			sessionID=jsonObject.getLong("sessionID");
		} catch (JSONException e) {
			e.printStackTrace();
			sessionID=-1;
		}
		return sessionID;
	}

	public synchronized void logout(long sessionNumber) {

	}

	private String getJSonString(/*params?*/) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://perex.pl/json/index.php");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("err:", "Failed to download file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}
