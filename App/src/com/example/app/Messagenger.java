package com.example.app;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.Selection;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Messagenger extends Activity implements android.view.View.OnClickListener
{
	String phone_number="9999999999";
	EditText edittext1;
	EditText edittext2,edittext3;
	Button button2;
	IntentFilter intentFilter;
	String earlier_messages;

	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override public void onReceive(Context context, Intent intent) {
		//---display the SMS received in the TextView---
		//edittext3 = (EditText) findViewById(R.id.edittext3);
		String rmessage=intent.getExtras().getString("sms");
		String number=intent.getExtras().getString("number");
		if(number!=null)
		{
			Toast.makeText(context, "Shreyas,Message received from "+ number, Toast.LENGTH_SHORT).show();
			
			MediaPlayer song = MediaPlayer.create(context, R.raw.aud1);
			song.start();
		}
		
		}
		};	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messanger);
		
		intentFilter=new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		
		Bundle bundle = getIntent().getExtras();
		phone_number = bundle.getString("phone_number");
		
		edittext1 = (EditText) findViewById(R.id.edittext1);
		edittext2 = (EditText) findViewById(R.id.edittext2);
		edittext3 = (EditText) findViewById(R.id.edittext3);
		button2 = (Button) findViewById(R.id.button2);
		
		button2.setOnClickListener(this);
		
		edittext1.setText(phone_number);
		edittext1.setEnabled(false);
		

        Storesms entry = new Storesms(Messagenger.this);
		entry.open();
		earlier_messages = entry.getmessages(phone_number);
		entry.close();
		
		edittext3.setText(earlier_messages);
		int position = edittext3.length();
		Editable etext = edittext3.getText();
		Selection.setSelection(etext, position);
	}
	@Override protected void onResume() {
		//---register the receiver---
		registerReceiver(intentReceiver,intentFilter);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
	//---unregister the receiver---
	unregisterReceiver(intentReceiver);
	super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void sendSMS(String phoneNumber,String message)
	{
		String SENT ="SMS_SENT";
		String DELIVERED ="SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);
	//---when the SMS has been sent---
	registerReceiver(new BroadcastReceiver(){
	@Override
	public void onReceive(Context arg0, Intent arg1) {
	switch (getResultCode())
	{
		case Activity.RESULT_OK:
			Toast.makeText(getBaseContext(),"SMS sent",Toast.LENGTH_SHORT).show();
			break;
	
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Toast.makeText(getBaseContext(),"Generic failure",Toast.LENGTH_SHORT).show();
			break;
			
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Toast.makeText(getBaseContext(),"No service",Toast.LENGTH_SHORT).show();
			break;
			
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Toast.makeText(getBaseContext(),"Null PDU",Toast.LENGTH_SHORT).show();
			break;
			
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Toast.makeText(getBaseContext(),"Radio off",Toast.LENGTH_SHORT).show();
			break;
	}
	}
	},
	new IntentFilter(SENT));
	//---when the SMS has been delivered---
	registerReceiver(new BroadcastReceiver(){
	@Override
	public void onReceive(Context arg0, Intent arg1) {
	switch
	(getResultCode())
	{
		case Activity.RESULT_OK:
			Toast.makeText(getBaseContext(),"SMS delivered",Toast.LENGTH_SHORT).show();
			break;
		case Activity.RESULT_CANCELED:
			Toast.makeText(getBaseContext(),"SMS not delivered",Toast.LENGTH_SHORT).show();
			break;
	}
	}
	},new IntentFilter(DELIVERED));
	SmsManager sms = SmsManager.getDefault();
	sms.sendTextMessage(phoneNumber,null, message, sentPI, deliveredPI);
	}
	
	@SuppressLint("ShowToast")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
			case R.id.button2:
			{	
				boolean diditwork=true;
				try
				{
				String message="Me : \n"+edittext2.getText().toString();
				String phoneNumber=edittext1.getText().toString();
		
				Storesms entry = new Storesms(Messagenger.this);
				entry.open();
				entry.updateEntry(phoneNumber , message);
				entry.close();
				sendSMS(phoneNumber, message);
				}
				catch(Exception e)
				{
					diditwork=false;
					String error = e.toString();
					Dialog d = new Dialog(Messagenger.this);
					TextView v = new TextView(this);
					d.setTitle("Checker");
					v.setText(error);
					d.setContentView(v);
					d.show();
				}
				finally
				{
					if(diditwork)
					{
						Dialog d = new Dialog(Messagenger.this);
						Button v = new Button(this);
						d.setTitle("Message sent!!");
						v.setText("  OK  ");
						v.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								finish();
							}
						});
						d.setContentView(v);
						d.show();
					}
				}
				break;
			}			
		}
	}
}
