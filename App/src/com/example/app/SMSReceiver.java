package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.widget.Switch;
import android.widget.Toast;
public class SMSReceiver extends BroadcastReceiver
{
	MediaPlayer song;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		//---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		String num="";
		SmsMessage[] msgs =null;
		String str ="";
		if(bundle !=null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs =new SmsMessage[pdus.length];
			for(int i=0; i<msgs.length; i++)
			{
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				str +="SMS from "+ msgs[i].getOriginatingAddress();
				num=num+msgs[i].getOriginatingAddress();
				str +=" :";
				str += msgs[i].getMessageBody().toString();
				str +="\n";
			}
			//---display the new SMS message---
			Toast.makeText(context,str, Toast.LENGTH_SHORT).show();
			//---send a broadcast intent to update the SMS received in the activity---
			Intent broadcastIntent =new Intent();
			broadcastIntent.setAction("SMS_RECEIVED_ACTION");
			broadcastIntent.putExtra("sms", str);
			broadcastIntent.putExtra("number", num);
			
			String temp=num.substring(3);
			
			
			Storesms entry = new Storesms(context);
			entry.open();
			entry.updateEntry(temp, str);
			entry.close();
			
			changeRingerMode(context);
			
			context.sendBroadcast(broadcastIntent);
		}
	}
	public void changeRingerMode(Context context){

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		switch( audio.getRingerMode() ){
		case AudioManager.RINGER_MODE_NORMAL:
			song = MediaPlayer.create(context, R.raw.aud1);
			song.start();			
		   break;
		case AudioManager.RINGER_MODE_SILENT:
		   break;
		case AudioManager.RINGER_MODE_VIBRATE:
			 Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			 v.vibrate(1500);
		   break;
		}
		}
}