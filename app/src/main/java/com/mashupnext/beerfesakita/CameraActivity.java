package com.mashupnext.beerfesakita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class CameraActivity extends Activity implements OnClickListener {

	private static final int BUTTON_ID_CANCEL = 1;
	private static final int BUTTON_ID_SELECT_GALARY = 2;
	private static final int BUTTON_ID_START_PHOTO = 3;
	private CameraSurface _surface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = createLinearLayout();
		setContentView(layout);
		
		LinearLayout.LayoutParams params = createLinearLayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		addCameraSurfaceView(layout, params);
		addButtonView(layout, params);
	}

	private LinearLayout createLinearLayout(){
		LinearLayout result = new LinearLayout(this);
		result.setOrientation(LinearLayout.VERTICAL);

		return result;
	}

	private LinearLayout.LayoutParams createLinearLayoutParams(int width, int height) {
		LinearLayout.LayoutParams result = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		result.weight = 1;

		return result;
	}

	private LinearLayout createHorizontalLinearLayout() {
		LinearLayout result = new LinearLayout(this);
		result.setOrientation(LinearLayout.HORIZONTAL);
		result.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		return result;
	}

	private void addCameraSurfaceView(LinearLayout layout, LinearLayout.LayoutParams params) {
		_surface = new CameraSurface(this);
		_surface.setLayoutParams(params);
		layout.addView(_surface);
	}

	private void addButton(LinearLayout layout, LinearLayout.LayoutParams params, String text, int id) {
		Button button = new Button(this);
		button.setText(text);
		button.setId(id);
		button.setLayoutParams(params);
		button.setOnClickListener(this);
		layout.addView(button);
	}

	private void addButtonView(LinearLayout layout, LinearLayout.LayoutParams params) {
		LinearLayout horizontalLayout = createHorizontalLinearLayout();

		addButton(horizontalLayout, params, getResources().getString(R.string.cancel), BUTTON_ID_CANCEL);
		addButton(horizontalLayout, params, getResources().getString(R.string.selectGalary), BUTTON_ID_SELECT_GALARY);
		addButton(horizontalLayout, params, getResources().getString(R.string.start_photo), BUTTON_ID_START_PHOTO);

		layout.addView(horizontalLayout);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case BUTTON_ID_CANCEL:
		    setResult(ActivityCode.RESULT_CANCEL);
			finish();
			break;

		case BUTTON_ID_SELECT_GALARY:
			startGalarySelectActivity();
			break;

		case BUTTON_ID_START_PHOTO:
			_surface.takePicture();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ActivityCode.REQUEST_PICK_EXISTING_PHOTO:
			procActivityResultCamera(resultCode,data);
			break;

		default:
			break;
		}
	}

	private void procActivityResultCamera(int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_CANCELED:
		case Activity.RESULT_OK:
			setResult(resultCode, data);
			break;
			
		default:
			break;
		}

		finish();
	}

	private void startGalarySelectActivity() {
		Intent intent = new Intent(Intent.ACTION_PICK,(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
		startActivityForResult(intent, ActivityCode.REQUEST_PICK_EXISTING_PHOTO);
	}
}
