package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateMapItemActivity extends Activity implements OnItemSelectedListener{

	private Location location;
	
	private NoteCreateDataFragment noteFragment = new NoteCreateDataFragment();
	private ImageCreateDataFragment imageFragment = new ImageCreateDataFragment();
	private VideoCreateDataFragment videoFragment = new VideoCreateDataFragment();
	
	private Spinner typeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_map_item);
		
		Intent intent = getIntent();
		location = intent.getParcelableExtra(MainMapActivity.MY_POSITION);
		
		TextView positionView = (TextView)findViewById(R.id.textViewPosition);
		double latitide = location.getLatitude();
		double longitude = location.getLongitude();
		positionView.setText(latitide + (latitide>=0?"N":"S") + longitude + (longitude>=0?"E":"W"));
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.additionalData, noteFragment);
		fragmentTransaction.commit();
		
		typeList = (Spinner)findViewById(R.id.spinnerType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.item_type_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeList.setAdapter(adapter);
		typeList.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	//Spinner listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		
	}

	//Spinner listener
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
