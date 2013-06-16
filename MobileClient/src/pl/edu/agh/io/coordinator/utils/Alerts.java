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

package pl.edu.agh.io.coordinator.utils;

import pl.edu.agh.io.coordinator.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class Alerts {

	private static volatile boolean isInvalidSessionAlertViewed = false;
	
	public static void invalidSessionId(final Activity activity, final AsyncTask<Intent, Void, Exception> task) {
		if (isInvalidSessionAlertViewed) {
			return;
		}
		isInvalidSessionAlertViewed = true;
		new AlertDialog.Builder(activity).setMessage(R.string.alert_invalid_session_id_logout)
				.setTitle(R.string.alert_invalid_session_id).setCancelable(false).setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (task != null) {
							Intent intent = new Intent();
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, intent);
						}
						activity.finish();
						isInvalidSessionAlertViewed = false;
					}
				}).create().show();
	}

	public static void networkProblem(final Activity activity) {
		Toast.makeText(activity.getApplicationContext(), R.string.alert_network_problem, Toast.LENGTH_LONG).show();
	}

	public static void invalidLayer(final Activity activity) {
		Toast.makeText(activity.getApplicationContext(), R.string.alert_invalid_layer, Toast.LENGTH_LONG).show();
	}

	public static void invalidMapItem(final Activity activity) {
		Toast.makeText(activity.getApplicationContext(), R.string.alert_invalid_map_item, Toast.LENGTH_LONG).show();
	}

	public static void invalidUser(final Activity activity) {
		Toast.makeText(activity.getApplicationContext(), R.string.alert_invalid_user, Toast.LENGTH_LONG).show();
	}
	
	public static void groupCreated(final Activity activity) {
		new AlertDialog.Builder(activity).setMessage(R.string.alert_group_created).setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok, null).create().show();
	}

	public static void mapItemCreated(final Activity activity) {
		new AlertDialog.Builder(activity).setMessage(R.string.alert_map_item_created).setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				}).create().show();
	}
	
}

