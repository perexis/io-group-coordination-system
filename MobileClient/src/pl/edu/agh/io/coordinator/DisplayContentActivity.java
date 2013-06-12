package pl.edu.agh.io.coordinator;

import pl.edu.agh.io.coordinator.MainMapActivity.ContentType;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;

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
			if (!videoUrl.startsWith("http://")) {
				videoUrl = JSonProxy.SERVER_NAME + "/videos/" + videoUrl;
				ShowVideoFragment videoFragment = ShowVideoFragment
						.newInstance(videoUrl);
				fragmentTransaction.add(R.id.contentFrame, videoFragment);
			} else {
				final String videoID = new String(videoUrl);
				YouTubePlayerFragment player = YouTubePlayerFragment
						.newInstance();
				player.initialize(DeveloperKey.YOUTUBE_DEVELOPER_KEY,
						new OnInitializedListener() {

							@Override
							public void onInitializationSuccess(Provider arg0,
									YouTubePlayer player, boolean wasRestored) {
								if (!wasRestored) {
									Uri uri = Uri.parse(videoID);
									String vlink = uri.getQueryParameter("v");
									player.setFullscreen(true);
									player.cueVideo(vlink);
								}

							}

							@Override
							public void onInitializationFailure(Provider arg0,
									YouTubeInitializationResult arg1) {
								Log.e("DisplayContentActivity",
										"YouTube initialization failed");

							}
						});
				fragmentTransaction.add(R.id.contentFrame, player);
			}

			break;
		case USER:
			String userName = intent.getStringExtra(MainMapActivity.USER_NAME);
			String userSurname = intent
					.getStringExtra(MainMapActivity.USER_SURNAME);
			String userNick = intent.getStringExtra(MainMapActivity.USER_NICK);
			String userPhone = intent
					.getStringExtra(MainMapActivity.USER_PHONE);
			String userMail = intent.getStringExtra(MainMapActivity.USER_MAIL);
			String userAvatar = intent
					.getStringExtra(MainMapActivity.USER_AVATAR);
			ShowUserFragment userFragment = ShowUserFragment.newInstance(
					userName, userSurname, userNick, userPhone, userMail,
					userAvatar);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
