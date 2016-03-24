package com.example.app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread()
        {
        	public void run()
        	{
        		try{
        			sleep(2200);
        		}
        		catch(InterruptedException e)
        		{
        			e.printStackTrace();
        		}
        		finally
        		{
        			finish();
        			Intent re=new Intent("com.example.app.MENU");
        			startActivity(re);
        		}
        	}
        };
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
