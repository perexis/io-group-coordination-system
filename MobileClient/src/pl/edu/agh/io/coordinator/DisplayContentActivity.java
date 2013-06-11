package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import pl.edu.agh.io.coordinator.MainMapActivity.ContentType;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;

public class DisplayContentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_content);

		Intent intent = getIntent();

		ContentType contentType = ContentType.valueOf(intent
				.getStringExtra(MainMapActivity.EXTRA_CONTENT_TYPE));

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		switch (contentType) {
		case NOTE:
			ShowNoteFragment noteFragment = ShowNoteFragment.newInstance(intent
					.getStringExtra(MainMapActivity.EXTRA_CONTENT));
			fragmentTransaction.add(R.id.contentFrame, noteFragment);
			break;
		case IMAGE:
			String imageUrl = intent
					.getStringExtra(MainMapActivity.EXTRA_CONTENT);
			if (!imageUrl.startsWith("http://"))
				imageUrl = JSonProxy.SERVER_NAME + "/images/" + imageUrl;
			ShowImageFragment imageFragment = ShowImageFragment
					.newInstance(imageUrl);
			fragmentTransaction.add(R.id.contentFrame, imageFragment);
			break;
		case VIDEO:
			String videoUrl = intent
					.getStringExtra(MainMapActivity.EXTRA_CONTENT);
			if (!videoUrl.startsWith("http://"))
				videoUrl = JSonProxy.SERVER_NAME + "/videos/" + videoUrl;
			ShowVideoFragment videoFragment = ShowVideoFragment
					.newInstance(videoUrl);
			fragmentTransaction.add(R.id.contentFrame, videoFragment);
			break;
		case USER:
			String userName = intent.getStringExtra(MainMapActivity.EXTRA_CONTENT);
			ShowUserFragment userFragment = ShowUserFragment.newInstance(userName);
			fragmentTransaction.add(R.id.contentFrame, userFragment);
			
			break;
		default:
			break;
		}
		fragmentTransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
