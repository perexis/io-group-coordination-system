package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import pl.edu.agh.io.coordinator.MainMapActivity.ContentType;

public class DisplayContentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_content);
		
		Intent intent = getIntent();
		
		ContentType contentType = ContentType.valueOf(intent.getStringExtra(MainMapActivity.EXTRA_CONTENT_TYPE));

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		switch(contentType){
		case NOTE:
			ShowNoteFragment noteFragment = ShowNoteFragment
					.newInstance(intent.getStringExtra(MainMapActivity.EXTRA_CONTENT));
			fragmentTransaction.add(R.id.contentFrame, noteFragment);
			break;
		case IMAGE:
			ShowImageFragment imageFragment = ShowImageFragment
					.newInstance("http://www.google.pl/images/srpr/logo2w.png");
			fragmentTransaction.add(R.id.contentFrame, imageFragment);
			break;
		case VIDEO:
			ShowVideoFragment videoFragment = ShowVideoFragment
					.newInstance("/videos/aaa.mpg");
			fragmentTransaction.add(R.id.contentFrame, videoFragment);
		break;
		default:
			break;
		}
		fragmentTransaction.commit();

		// MediaController media = new MediaController(this);
		// media = (MediaController)findViewById(R.id.mediaController);
		// media.setBackground(getWallpaper());

		// layout = (RelativeLayout) findViewById(R.id.contentContainer);

		// layout.addView(media);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
