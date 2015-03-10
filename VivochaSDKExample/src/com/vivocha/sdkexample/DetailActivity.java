package com.vivocha.sdkexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vivocha.sdk.Vivocha;
import com.vivocha.sdk.Vivocha.VivochaContactCreationCallback;
import com.vivocha.sdk.VivochaContact;

public class DetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
	
		
		Button customEngagement = (Button) findViewById(R.id.customEngagement);
		Button buttonWebLead = (Button) findViewById(R.id.buttonWeblead);
		Button buttonCBN = (Button) findViewById(R.id.buttonCBN);

		
		if (customEngagement != null) {
			customEngagement.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(Vivocha.getContact() != null){
						//there is already a contact in progress
						
						Vivocha.getContact().showView(true);
					}
					else{
						//there is no contact... let's create one!
						
						Vivocha.createChat(Vivocha.getDataCollection(), new VivochaContactCreationCallback() {
							
							@Override
							public void onCreationSuccess(VivochaContact contact) {
								if(contact != null){
									contact.connect();
									contact.showView(true);
								}
							}
							
							@Override
							public void onCreationFailure() {
								//Aww, snap! Show an alert to notify the failure
							}
						});
					}
				}
			});
		}
		
		if (buttonWebLead != null) {
			buttonWebLead.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//we don't need to check for existing webleads because the only type of contact that can remain 'alive' is the chat one
					Vivocha.createWeblead(Vivocha.getDataCollection(), "email@example.com", new VivochaContactCreationCallback() {
						
						@Override
						public void onCreationSuccess(VivochaContact contact) {
							//WebLead created!
						}
						
						@Override
						public void onCreationFailure() {
							//There was an error creating the weblead!
						}
					});
				}
			});
		}
		
		if (buttonCBN != null) {
			buttonCBN.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Vivocha.createCallBackNow(Vivocha.getDataCollection(), "+1555123456", new VivochaContactCreationCallback() {
						
						@Override
						public void onCreationSuccess(VivochaContact contact) {
							//CBN created!
						}
						
						@Override
						public void onCreationFailure() {
							//There was an error creating the CallBackNow!
						}
					});
					
				}
			});
		}
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		
		//Call this method when you show your own custom engagement button in order to track the widget on the Stats.
		Vivocha.manager().notifyCustomEngagementButtonShown();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//Call this method when you hide your own custom engagement button in order to track the widget on the Stats.
		Vivocha.manager().notifyCustomEngagementButtonHidden();
	}
}
