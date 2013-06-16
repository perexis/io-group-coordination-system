/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

public class LoginMethodActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_method);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_method, menu);
		return true;
	}

	public void clickLoginMethod(View view) {
		Intent intent = null;
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupLoginMethod);
		int option = radioGroup.getCheckedRadioButtonId();
		switch (option) {
		case R.id.radioLoginPassword:
			intent = new Intent(LoginMethodActivity.this,
					LoginPasswordActivity.class);
			break;
		case R.id.radioNFC:
		case R.id.radioFingerprint:
		default:
			intent = new Intent(LoginMethodActivity.this,
					NotImplementedYetActivity.class);

		}
		startActivity(intent);
	}

}

