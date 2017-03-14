package us.crimean.maps.servlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

public class LightHttpClient {
    private static final Logger log = Logger.getLogger(LightHttpClient.class.getName());

	public String getRequest(String strUrl, Map<String,String> parameterMap) {
		return getCrossDomainData(strUrl, parameterMap);
	}

	private String getCrossDomainData(String strUrl, Map<String,String> parameterMap) {
        String result = null;
        URL url;
    	String fullUrlString = null;
        try {
        	fullUrlString = getFullUrlString(strUrl, parameterMap);
            url = new URL(fullUrlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                result = null;
            } else {
                InputStream in = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                    //log.info("line:"+line);
                }

                bufferedReader.close();
                result = stringBuilder.toString();
            }

        } catch (MalformedURLException e) {
        	log.severe(fullUrlString + " request MalformedURLException:" + e);
            result = null;
        } catch (Exception e) {
        	log.severe(fullUrlString + " request Exception:" + e);
            result = null;
        }
        return result;
    }

	private String getFullUrlString(String strUrl, Map<String,String> parameterMap) {
        StringBuffer urlWithParamerers = new StringBuffer(strUrl);
        boolean firsParam = !strUrl.contains("?");
        for (Object key : parameterMap.keySet()) {
			urlWithParamerers.append(firsParam ? "?" : "&" + key + "=" + parameterMap.get(key));
			firsParam = false;
        }
		return urlWithParamerers.toString();
	}
}
