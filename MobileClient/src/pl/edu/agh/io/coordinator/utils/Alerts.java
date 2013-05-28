package pl.edu.agh.io.coordinator.utils;

import pl.edu.agh.io.coordinator.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class Alerts {

	public static void invalidSessionId(final Activity activity) {
		new AlertDialog.Builder(activity).setMessage(R.string.alert_invalid_session_id_logout)
				.setTitle(R.string.alert_invalid_session_id).setCancelable(false).setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
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
