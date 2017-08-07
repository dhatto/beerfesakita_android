package com.mashupnext.beerfesakita;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

//import twitter4j.StatusUpdate;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;

//import com.facebook.AccessToken;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.share.ShareApi;
//import com.facebook.share.Sharer;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
import com.mashupnext.beerfesakita.utility.Utility;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mashupnext.beerfesakita.social.TwitterUtils;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class PostActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener {

	private static final int POST_FIX_MASK = 0x00;
	private static final int POST_FACEBOOK_MASK = 0x10;
	private static final int POST_TWITTER_MASK = 0x01;

	private static final String HASH_TAG_BEERFES_AKITA = " #秋田ビアフェス";
	private static final String HASH_TAG = "#";
	private final String PENDING_ACTION_BUNDLE_KEY = "com.mashupnext.beerfesakita:PendingAction";

//	private Twitter _twitter;
	private EditText _postEditText;
	private ImageButton _addPhotoButton;
	private Button _postButton;
	private Uri _photoUri;
	private ByteArrayInputStream _twitterPhotoStream;
	private Bitmap _photo;

	private int _snsPostStatus;
	private AlertDialog _twitterAuthAlertDialog;
	private AlertDialog _facebookAuthAlertDialog;

	private PendingAction pendingAction = PendingAction.NONE;

	private enum PendingAction {
		NONE,
		POST_PHOTO,
		POST_STATUS_UPDATE
	}

//	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
//		@Override
//		public void onCancel() {
//		}
//
//		@Override
//		public void onError(FacebookException error) {
//			String title = getString(R.string.error);
//			String alertMessage = getString(R.string.post_error);
//			showResult(title, alertMessage);
//		}
//
//		@Override
//		public void onSuccess(Sharer.Result result) {
//			if (result.getPostId() != null) {
//				PostActivity.this.finish();
//				//String id = result.getPostId();
//				//String alertMessage = getString(R.string.successfully_posted_post, id);
////				String title = getString(R.string.success);
////				String alertMessage = getString(R.string.post_success);
////				showResult(title, alertMessage);
//			}
//		}
//
//		private void showResult(String title, String alertMessage) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
//
//			builder.setTitle(title);
//			builder.setMessage(alertMessage);
//			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					PostActivity.this.finish();
//				}
//			});
//			builder.show();
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 多分、Activity毎に必要かと？
//		FacebookSdk.sdkInitialize(this.getApplicationContext());

		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.activity_post);
		setOnClickListener();
		initWidget();
	}

	private void setOnClickListener() {
		findViewById(R.id.addPhotoButton).setOnClickListener(this);
		findViewById(R.id.cancelButton).setOnClickListener(this);
		findViewById(R.id.postButton).setOnClickListener(this);
		findViewById(R.id.twitterCheckBox).setOnClickListener(this);
		findViewById(R.id.faceBookCheckBox).setOnClickListener(this);
	}

	private void initWidget() {
		String brewerTitle = " " + HASH_TAG_BEERFES_AKITA + " " + HASH_TAG + (String) getIntent().getExtras().get("title");
		_postEditText = (EditText) findViewById(R.id.postEditText);
		_postEditText.setText(brewerTitle);

		_addPhotoButton = (ImageButton) findViewById(R.id.addPhotoButton);

		_postButton = (Button) findViewById(R.id.postButton);
		_postButton.setEnabled(false);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addPhotoButton:
				startCameraActivity();
				break;
			case R.id.cancelButton:
				finish();
				break;
			case R.id.postButton:
				v.setEnabled(false);
//				postToSns();
				break;
			case R.id.twitterCheckBox:
//				preparePostTwitterIfNeeded(v);
				postButtonEnableSetting();
				break;
			case R.id.faceBookCheckBox:
//				preparePostFacebookIfNeeded(v);
				postButtonEnableSetting();
				break;
		}
	}

	private void preparePostFacebookIfNeeded(View v) {
		CheckBox cb = (CheckBox) v;
		if (cb.isChecked()) {
	//		preparePostFacebook();
		}
	}

	private void preparePostTwitterIfNeeded(View v) {
		CheckBox cb = (CheckBox) v;
		if (cb.isChecked()) {
			preparePostTwitter();
		}
	}

	private void postButtonEnableSetting() {
		CheckBox twitterCheckBox = (CheckBox) findViewById(R.id.twitterCheckBox);
		CheckBox facebookCheckBox = (CheckBox) findViewById(R.id.faceBookCheckBox);
		_postButton.setEnabled(
				twitterCheckBox.isChecked() || facebookCheckBox.isChecked()
		);
	}

	private void startCameraActivity() {
		Intent i = new Intent(this, CameraActivity.class);
		startActivityForResult(i, ActivityCode.REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ActivityCode.REQUEST_CAMERA:
				procActivityResultCamera(resultCode, data);
				break;
			default:
				break;
		}
	}

	private void procActivityResultCamera(int resultCode, Intent data) {
		switch (resultCode) {
			case Activity.RESULT_OK:
				setPhotoImage(data);
				break;
			case Activity.RESULT_CANCELED:
			case ActivityCode.RESULT_CANCEL:
				break;
		}
	}

	private void setPhotoImage(Intent data) {
		_photoUri = data.getData();
		_photo = Utility.scaleBitmap(this, _photoUri);

		_addPhotoButton.setImageBitmap(_photo);
	}

	private void setSnsPostStatus() {
		CheckBox cbTwitter = (CheckBox) findViewById(R.id.twitterCheckBox);
		CheckBox cbFacebook = (CheckBox) findViewById(R.id.faceBookCheckBox);
		_snsPostStatus = POST_FIX_MASK;

		if (cbTwitter.isChecked()) {
			_snsPostStatus |= POST_TWITTER_MASK;
		}
		if (cbFacebook.isChecked()) {
			_snsPostStatus |= POST_FACEBOOK_MASK;
		}
	}

//	private void postToSns() {
//		setSnsPostStatus();
//
//		if (!postToTwitter()) {
//	//		postToFacebook();
//		}
//	}

//	private Boolean postToTwitter() {
//		Boolean result = false;
//
//		if ((_snsPostStatus & POST_TWITTER_MASK) == POST_TWITTER_MASK) {
//			result = true;
//	//		_twitter = TwitterUtils.getTwitterInstance(this);
//			if (_photoUri != null) {
//				postToTwitterWithPhoto();
//			} else {
//				tweet(getPostTextString());
//			}
//		}
//		return result;
//	}

//	private Boolean postToFacebook() {
//		Boolean result = false;
//
//		if ((_snsPostStatus & POST_FACEBOOK_MASK) == POST_FACEBOOK_MASK) {
//			result = true;
//
//			AccessToken accessToken = AccessToken.getCurrentAccessToken();
//			if (accessToken != null) {
//				if (_photoUri != null) {
//					pendingAction = PendingAction.POST_PHOTO;
//				} else {
//					pendingAction = PendingAction.POST_STATUS_UPDATE;
//				}
//				handlePendingAction();
//			}
//		} else {
//			finish();
//		}
//		return result;
//	}

//	private void postToFacebookWall() {
//
//		String message = getPostTextString();
//
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("message", message);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		GraphRequest graphRequest = GraphRequest.newPostRequest(AccessToken.getCurrentAccessToken(), "me/feed",
//				jsonObject, new GraphRequest.Callback() {
//					@Override
//					public void onCompleted(GraphResponse graphResponse) {
//						_snsPostStatus ^= POST_FACEBOOK_MASK;
//						PostActivity.this.finish();
//					}
//				});
//
//		graphRequest.executeAsync();
//	}

//	private void postToFacebookWithPhoto() {
//		SharePhoto.Builder builder = new SharePhoto.Builder();
//		builder.setBitmap(_photo);
//		builder.setCaption(getPostTextString());
//
//		SharePhoto sharePhoto = builder.build();
//
//		ArrayList<SharePhoto> photos = new ArrayList<>();
//		photos.add(sharePhoto);
//
//		SharePhotoContent sharePhotoContent =
//				new SharePhotoContent.Builder().setPhotos(photos).build();
//
//		ShareApi.share(sharePhotoContent, shareCallback);
//	}

//	private boolean hasPublishPermission() {
//		AccessToken accessToken = AccessToken.getCurrentAccessToken();
//		return accessToken != null && accessToken.getPermissions().contains("publish_actions");
//	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
			case NONE:
				break;
			case POST_PHOTO:
	//			postToFacebookWithPhoto();
				break;
			case POST_STATUS_UPDATE:
	//			postToFacebookWall();
				break;
		}
	}

//	private void postToTwitterWithPhoto() {
//		StatusUpdate status = new StatusUpdate(getPostTextString());
//		byte[] byteArray = null;
//		try {
//			byteArray = Utility.scaleImage(this, _photoUri);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		_twitterPhotoStream = new ByteArrayInputStream(byteArray);
//		status.setMedia("myMedia", _twitterPhotoStream);
//		tweet(status);
//	}

//	private void tweet(StatusUpdate status) {
//		AsyncTask<StatusUpdate, Void, Boolean> task = new AsyncTask<StatusUpdate, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(StatusUpdate... params) {
//				try {
//					_twitter.updateStatus(params[0]);
//					return true;
//				} catch (TwitterException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				if (result) {
//					try {
//						_twitterPhotoStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				_snsPostStatus ^= POST_TWITTER_MASK;
//				//PostActivity.this.postToFacebook();
//			}
//		};
//
//		task.execute(status);
//	}

//	private void tweet(String text) {
//		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(String... params) {
//				try {
//		//			_twitter.updateStatus(params[0]);
//					return true;
//				} catch (TwitterException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				_snsPostStatus ^= POST_TWITTER_MASK;
//				//PostActivity.this.postToFacebook();
//			}
//		};
//
//		task.execute(text);
//	}

	private void preparePostTwitter() {
		if (!TwitterUtils.hasAccessToken(this)) {
			alertTwitterAuthError();
		}
	}

//	private void preparePostFacebook() {
//
//		if (!hasPublishPermission()) {
//			alertFacebookAuthError();
//		}
//	}

	private void alertFacebookAuthError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		_facebookAuthAlertDialog = builder.setTitle("確認").setMessage("次の画面でFacebookにログインし、認証を行って下さい。").setPositiveButton("OK", this).show();
	}

	private void alertTwitterAuthError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		_twitterAuthAlertDialog = builder.setTitle("確認").setMessage("Twitterで認証を行います。").setPositiveButton("OK", this).show();
	}

	private String getPostTextString() {
		SpannableStringBuilder sb = (SpannableStringBuilder) _postEditText.getText();
		return sb.toString();
	}

	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		if (dialog == _facebookAuthAlertDialog) {
//			Intent i = new Intent(this, FacebookSettingActivity.class);
//			startActivity(i);
		} else if (dialog == _twitterAuthAlertDialog) {
//			Intent intent = new Intent(this, OAuthActivity.class);
//			startActivity(intent);
		}
	}
}

