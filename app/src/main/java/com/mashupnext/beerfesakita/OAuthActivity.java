//package com.mashupnext.beerfesakita;
//
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.auth.AccessToken;
//import twitter4j.auth.RequestToken;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.view.Menu;
//import com.mashupnext.beerfesakita.social.*;
//
//public class OAuthActivity extends Activity implements OnClickListener {
//	private String mCallbackURL;
//	private Twitter mTwitter;
//	private RequestToken mRequestToken;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_oauth);
//
//		mCallbackURL = getString(R.string.twitter_callback_url);
//        mTwitter = TwitterUtils.getTwitterInstance(this);
//
//        startAuthorize();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.oauth, menu);
//		return true;
//	}
//
//	private void startAuthorize() {
//        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                try {
//                    mRequestToken = mTwitter.getOAuthRequestToken(mCallbackURL);
//                    return mRequestToken.getAuthorizationURL();
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String url) {
//                if (url != null) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                } else {
//    				AlertDialog.Builder builder = new AlertDialog.Builder(OAuthActivity.this);
//    				builder.setMessage("Twitter").setPositiveButton("",OAuthActivity.this).show();
//                }
//            }
//        };
//        task.execute();
//    }
//
//	@Override
//	public void onNewIntent(Intent intent) {
//        if (intent == null
//                || intent.getData() == null
//                || !intent.getData().toString().startsWith(mCallbackURL)) {
//            return;
//        }
//        String verifier = intent.getData().getQueryParameter("oauth_verifier");
//
//        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
//            @Override
//            protected AccessToken doInBackground(String... params) {
//                try {
//                    return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(AccessToken accessToken) {
//                if (accessToken != null) {
//                    successOAuth(accessToken);
//                } else {
//    				AlertDialog.Builder builder = new AlertDialog.Builder(OAuthActivity.this);
//    				builder.setMessage("Twitter").setPositiveButton("",OAuthActivity.this).show();
//                }
//            }
//        };
//        task.execute(verifier);
//    }
//
//    private void successOAuth(AccessToken accessToken) {
//        TwitterUtils.storeAccessToken(this, accessToken);
//        finish();
//    }
//
//	@Override
//	public void onClick(DialogInterface dialog, int which) {
//        finish();
//	}
//}
//
//
