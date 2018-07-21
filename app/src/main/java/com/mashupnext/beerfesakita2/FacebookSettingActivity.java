//package com.mashupnext.beerfesakita;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.widget.TextView;
//
//import com.facebook.*;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.login.widget.ProfilePictureView;
//
//public class FacebookSettingActivity extends FragmentActivity {
//
//	private static final String PERMISSION = "publish_actions";
//
//	private ProfilePictureView profilePictureView;
//	private TextView greeting;
//	private CallbackManager callbackManager;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		FacebookSdk.sdkInitialize(this.getApplicationContext());
//
//		setContentView(R.layout.activity_facebook_setting);
//
//		// publish_actions権限を追加する
//		LoginButton button = (LoginButton)findViewById(R.id.myAccountlogin);
//		button.setPublishPermissions(PERMISSION);
//
//		callbackManager = CallbackManager.Factory.create();
//
//		LoginManager.getInstance().registerCallback(callbackManager,
//				new FacebookCallback<LoginResult>() {
//					@Override
//					public void onSuccess(LoginResult loginResult) {
//						//updateUI();
//						showAlert();
//					}
//
//					@Override
//					public void onCancel() {
//						updateUI();
//					}
//
//					@Override
//					public void onError(FacebookException exception) {
//						updateUI();
//					}
//
//					private void showAlert() {
//						AlertDialog.Builder builder = new AlertDialog.Builder(FacebookSettingActivity.this);
//						builder.setTitle(R.string.authentication_title);
//						builder.setMessage(R.string.authentication_ok);
//						builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								FacebookSettingActivity.this.finish();
//							}
//						});
//
//						builder.show();
//					}
//				});
//
//		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
//		greeting = (TextView) findViewById(R.id.greeting);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		updateUI();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		callbackManager.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	private void updateUI() {
//		boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
//		Profile profile = Profile.getCurrentProfile();
//
//		if (enableButtons && profile != null) {
//			profilePictureView.setProfileId(profile.getId());
//			greeting.setText(R.string.authentication_ok);
//		} else {
//			profilePictureView.setProfileId(null);
//			greeting.setText(null);
//		}
//	}
//}
