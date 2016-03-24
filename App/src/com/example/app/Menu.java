package com.example.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends Activity implements OnItemClickListener,OnItemLongClickListener{
  
    private Button btnAdd;
    private ListView lvItem;
    private ArrayList<String> itemArrey,dbnumbers,itemnameArray;
    private ArrayAdapter<String> itemAdapter;
    private String new_number="-1";
    private String name = "-1";
	private Button button3,button4,button5,button6;
	private EditText edittext4;
	Dialog d,d1,d2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        setUpView();
    }

    private void setUpView() {
        // TODO Auto-generated method stub
        btnAdd = (Button)this.findViewById(R.id.button1);
        
        lvItem = (ListView)this.findViewById(R.id.list);
       

        itemArrey = new ArrayList<String>();
        itemArrey.clear();
        
        dbnumbers = new ArrayList<String>();
        dbnumbers.clear();

        Storesms entry = new Storesms(Menu.this);
		entry.open();
		dbnumbers = entry.getnumbers();
		entry.close();
        
		itemArrey.addAll(dbnumbers);
		
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        lvItem.setAdapter(itemAdapter);

        lvItem.setOnItemClickListener(this);
        lvItem.setOnItemLongClickListener(this);        
        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            	d2 = new Dialog(Menu.this);
            	d2.setTitle("Is recipient....");
            	d2.setContentView(R.layout.dialog2);
            	
            	button5 = (Button)d2.findViewById(R.id.button5);
            	button6 = (Button)d2.findViewById(R.id.button6);
            	
            	button5.setText("a new number");
            	button6.setText("already exists in contact");
            	
            	button5.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						d2.cancel(); 
						addItemList();
					}
				});
            	
            	button6.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						d2.cancel();
						Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
						startActivityForResult(intent, 1);
					}
				});
            	d2.show();
            }
        });
    }
    
    protected void addItemList() {
    	d = new Dialog(Menu.this);
    	d.setTitle("Phone number please!!!");
    	d.setContentView(R.layout.dialog);
    	
    	edittext4 = (EditText)d.findViewById(R.id.edittext4);
    	edittext4.setHint("Number");
    	button3 = (Button)d.findViewById(R.id.button3);
    	
        button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new_number=edittext4.getText().toString();
				
				if(new_number!="-1")
				{
					int len=itemArrey.size();
					int flag=0;
					
					for(int i=0;i<len;i++)
					{
						String bush=itemArrey.get(i);
						if(bush.equals(new_number))
						{
							flag=1;
						}
					}
					
					if(flag==0)
					{
						itemArrey.add(0,new_number);
						itemAdapter.notifyDataSetChanged();
				
						Storesms entry = new Storesms(Menu.this);
						entry.open();
						entry.createEntry(new_number);
						entry.close();
				
						d.cancel();
					
						new_number="-1";
					}
					else
					{
						d.cancel();
						
						final Dialog d2 = new Dialog(Menu.this);
						d2.setTitle("Number already present");
						Button bu = new Button(Menu.this);
						bu.setText("   OK   ");
						bu.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								d2.cancel();
							}
						});
						d2.setContentView(bu);
						d2.show();
					}
				}
			}
		});
    	d.show();
    }
    //---------------------------------------------------------------------------------------------
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (1) :
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
	        if (c.moveToFirst()) {
	          name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	          //TODO Whatever you want to do with the selected contact name.
	          new_number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	          
	          if(new_number!="-1")
				{
					int len=itemArrey.size();
					int flag=0;
					
					for(int i=0;i<len;i++)
					{
						String bush=itemArrey.get(i);
						if(bush.equals(new_number))
						{
							flag=1;
						}
					}
					
					if(flag==0)
					{
						itemArrey.add(0,new_number);
						itemAdapter.notifyDataSetChanged();
				
						Storesms entry = new Storesms(Menu.this);
						entry.open();
						entry.createEntry(new_number);
						entry.close();
									
						new_number="-1";
					}
					else
					{		
						final Dialog d2 = new Dialog(Menu.this);
						d2.setTitle("Number already present");
						Button bu = new Button(Menu.this);
						bu.setText("   OK   ");
						bu.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								d2.cancel();
							}
						});
						d2.setContentView(bu);
						d2.show();
					}
				}
	        }
	      }
	      break;
	  }
	}
	//------------------------------------------------------------------------------------------------
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String number=itemArrey.get(arg2);
		Intent e = new Intent("com.example.app.messanger");
		e.putExtra("phone_number", number);
		startActivity(e);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		d1 = new Dialog(Menu.this);
    	d1.setContentView(R.layout.dialog1);
    	
    	button4 = (Button)d1.findViewById(R.id.button4);
    	final String number=itemArrey.get(arg2);
    	final int index = arg2;
        button4.setOnClickListener(new View.OnClickListener() {
        		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String temp_number=number;
				int idex = index;
				
				Storesms entry = new Storesms(Menu.this);
				entry.open();
				boolean ret= entry.deleteEntry(temp_number);
				entry.close();
				
				if(ret==true)
				{
					itemArrey.remove(idex);
					itemAdapter.notifyDataSetChanged();
					Toast.makeText(Menu.this, "Deletion successful", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(Menu.this, "Deletion not successful", Toast.LENGTH_SHORT).show();
				}
				d1.cancel();
			}
		});
    	d1.show();
		return false;
	}
}