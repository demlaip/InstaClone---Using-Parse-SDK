package com.example.instaclone;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FriendFeed extends Activity{

	private List<PicList> picarraylist = null;
	ImageLoader imageLoader;
	int i = 0;
	ImageView iv;
	GridView gridView;
	GridViewAdapter adapter;
	//int numFollowers = 0;
	int numImages = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_own);
		
		new RemoteDataTask().execute("");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		iv = (ImageView) findViewById(R.id.photo_view);
		gridView = (GridView) findViewById(R.id.images_gridview);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
		query.include("User.username");
		query.whereEqualTo("from", ParseUser.getCurrentUser());

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> followList, ParseException e) {
				picarraylist = new ArrayList<PicList>();
				
				if(e==null) {
					
				for (ParseObject obj : followList) {
					ParseUser usr = ((Follow) obj).getTo();

					try {
						usr.fetchIfNeeded();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					//Log.i("Feed-- ", " " + usr.getUsername());
					
					/*
					 * CREATE A NEW PIC ARRAYLIST AND ADD PICS ITERATING FOR
					 * EACH USER
					 */
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Pic");
					query.whereEqualTo("author", usr);

					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> picList,
								ParseException e) {
							if (e == null) {
								numImages += picList.size();
								for (i = 0; i < picList.size(); i++) {

									ParseFile file = (ParseFile) picList.get(i)
											.get("photo");
									PicList map = new PicList();
									map.setPic(file.getUrl());
									picarraylist.add(map);
									file.getDataInBackground(new GetDataCallback() {

										@Override
										public void done(byte[] picBytes,
												ParseException arg1) {

											String imageUri = null;
											try {
												imageUri = new String(picBytes,
														"UTF-8");
												imageLoader.displayImage(
														picBytes.toString(), iv);
											} catch (UnsupportedEncodingException e) {

												e.printStackTrace();
											}

										}

									});

								}
								Log.i("Feed",
										"Single user size"
												+ picarraylist.size());
							}
						}

					});

				}
				
				Log.i("Feed" , "Num -- ArraySize " + numImages + " " + picarraylist.size());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				updateAdapter();
				
				}
				//updateAdapter();	
			}
		});
		
		
	}
	
	public void updateAdapter() {
		
		Log.i("Feed",
				"Total Size"
						+ picarraylist.size());
		adapter = new GridViewAdapter(FriendFeed.this, picarraylist);
		gridView.setAdapter(adapter);
	}
	
}

class RemoteDataTask extends AsyncTask<String, Void, String>{
	
	private List<PicList> picarraylist = null;
	ImageLoader imageLoader;
	int i = 0;
	ImageView iv;
	GridView gridView;
	GridViewAdapter adapter;

	RemoteDataTask() {
	}


	@Override
	protected String doInBackground(String... params) {
		Log.i("Feed ", "Do In Background");
		
		return null;
	}
	
	@Override
    protected void onPostExecute(String result) {
		Log.i("Feed ", "Ok this should be last");
    }
	
}





















