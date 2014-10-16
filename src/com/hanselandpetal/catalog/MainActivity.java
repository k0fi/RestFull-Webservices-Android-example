package com.hanselandpetal.catalog;

import java.util.ArrayList;
import java.util.List;

import com.hanselandpetal.catalog.model.Flower;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	ProgressBar		progressBar;
	TextView		output;
	List<MyTask>	tasks;
	List<Flower>	flowerList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);
		
		tasks = new ArrayList<>();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_do_task)
		{
			if (isOnline())
			{
				requestData("http://services.hanselandpetal.com/feeds/flowers.json");
			}
			else
			{
				Toast.makeText(this, "No data connection available", Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}
	
	private void requestData(String uri)
	{
		MyTask task = new MyTask();
		task.execute(uri);
	}
	
	protected void updateDisplay(String message)
	{
		output.append(message + "\n");
	}
	
	// To Check network connectivity is available
	protected boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
		
	}
	
	public class MyTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected void onPreExecute()
		{
			
			updateDisplay("Starting Task");
			
			if (tasks.size() == 0)
			{
				progressBar.setVisibility(View.VISIBLE);
			}
			tasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params)
		{
			// The uri passed will be the first parameter
			String content = HTTPManager.getData(params[0]);
			
			return content;
		}
		
		@Override
		protected void onProgressUpdate(String... values)
		{
			updateDisplay(values[0]);
			
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			updateDisplay(result);
			
			tasks.remove(this);
			if (tasks.size() == 0)
			{
				progressBar.setVisibility(View.INVISIBLE);
			}
			
		}
	}
	
}