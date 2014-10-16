package com.hanselandpetal.catalog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class HTTPManager
{
	// A method to get the data from the URI
	public static String getData(String uri)
	{
		AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
		HttpGet request = new HttpGet(uri);
		HttpResponse response;
		
		try
		{
			// Sending the request to server and getting back the respnse object
			response = client.execute(request);
			return EntityUtils.toString(response.getEntity());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			// To prevent the leak we need to close the client
			client.close();
		}
		
	}
}
