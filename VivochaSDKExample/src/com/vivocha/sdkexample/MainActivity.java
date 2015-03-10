package com.vivocha.sdkexample;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vivocha.sdk.Vivocha;
import com.vivocha.sdk.Vivocha.VivochaCustomActionCallback;
import com.vivocha.sdk.VivochaUtils;
import com.vivocha.sdk.VivochaValues;
import com.vivocha.sdk.internal.managers.VivochaNotificationManager;
import com.vivocha.sdk.internal.managers.VivochaNotificationManager.VivochaNotificationManagerCallbacks;
import com.vivocha.sdk.model.VivochaCustomAction;
import com.vivocha.sdk.model.datacollection.VivochaDataCollection;
import com.vivocha.sdk.model.datacollection.VivochaDataCollectionField;
import com.vivocha.sdk.model.datacollection.VivochaDataCollectionForm;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Create a service inside the Vivocha Agent Desktop Settings area
	    //Be sure to set the right bundle identifier (e.g. In this project the bundle id is com.vivocha.sdkesample)
		
	    //You will find a string like this one in your Android Service Settings
        Vivocha.start(this, "YOUR_ACCOUNT_ID", "YOUR_SERVICE_ID");

        //if you want to use your custom engagement button uncomment the following line
        //Vivocha.setBlockSideButton(true); //this line disables the automatic side tab
        
        
        //During a contact you can send an action from the Agent Desktop
        //using !action|TestAction|[{"activity":"detail"}]
        
        Vivocha.manager().bindCustomAction("TestAction", new VivochaCustomActionCallback() {
			
			@Override
			public void onAction(VivochaCustomAction action) {
				
				if(action != null){
					JSONObject data = (JSONObject) VivochaUtils.getObjectFromJSONArray((JSONArray) action.actionData, 0);
					
					String which = VivochaUtils.getJSONString(data, "activity");
					
					if(which != null){
						if (which.equalsIgnoreCase("detail")) {
							doDetailAction();
						}
					}					
				}
			}
		});
        
        
        //Set a data collection
        
        VivochaDataCollectionForm formOne = new VivochaDataCollectionForm();
        formOne.name = "My Form Title";
        formOne.description = "My Form Description";
        
        formOne.addField(new VivochaDataCollectionField("Field Title", VivochaValues.VivochaDataCollectionFieldTypeNumber, "NumberValue -e.g 3.99", "Field Description", true, false));
        formOne.addField(new VivochaDataCollectionField("AnotherField Title", VivochaValues.VivochaDataCollectionFieldTypeText, "TextValue", "AnotherField Description", true, false));

        //Set a nick name for your user (It will be shown on the agent desktop when a contact is requested)
        formOne.addField(new VivochaDataCollectionField("Nickname", VivochaValues.VivochaDataCollectionFieldTypeNickname, "User Nickname", "", true, false));
        
        VivochaDataCollection dc = new VivochaDataCollection();
        dc.addForm(formOne);

        Vivocha.setDataCollection(dc);
        
        //If you want to enable Push notifications
        VivochaNotificationManager.manager().register("YOUR_GOOGLE_API_KEY", new VivochaNotificationManagerCallbacks() {
			
			@Override
			public void onSuccess(String registrationID) {
				//save your registration ID
			}
			
			@Override
			public void onFailure() {
				
			}
		}, true); //True if you want to display a popup that request the user to install Google Play Services


		Button openDetailButton =  (Button) findViewById(R.id.buttonDetail);
		openDetailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doDetailAction();
			}
		});
	}

	void doDetailAction(){
		startActivity(new Intent(this, DetailActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
