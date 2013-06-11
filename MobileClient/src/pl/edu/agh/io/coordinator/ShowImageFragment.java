package pl.edu.agh.io.coordinator;

import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ShowImageFragment extends Fragment {
	private static final String ARG_IMAGE = "image";

	private String imageUrl;
	private ImageView image;

	public static ShowImageFragment newInstance(String image) {
		ShowImageFragment fragment = new ShowImageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_IMAGE, image);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowImageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			imageUrl = getArguments().getString(ARG_IMAGE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_image, container,
				false);
		image = (ImageView) (view.findViewById(R.id.imageFrame));

		new GetImageInBackground().execute();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private class GetImageInBackground extends AsyncTask<Void, Void, Bitmap> {

		protected Bitmap doInBackground(Void... urls) {
			Bitmap bitmapImage = null;
			try {
				InputStream in = new java.net.URL(imageUrl).openStream();
				bitmapImage = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("ShowImageFragment", e.getMessage());
				e.printStackTrace();
			}
			return bitmapImage;
		}

		protected void onPostExecute(Bitmap bitmap) {
			image.setImageBitmap(bitmap);
		}
	}

}
