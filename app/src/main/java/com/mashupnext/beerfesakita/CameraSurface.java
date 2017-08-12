//package com.mashupnext.beerfesakita;
//
//import java.io.IOException;
//
//import com.mashupnext.beerfesakita.utility.BitmapUtility;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.graphics.Bitmap;
//import android.hardware.Camera;
//import android.hardware.Camera.AutoFocusCallback;
//import android.hardware.Camera.CameraInfo;
//import android.hardware.Camera.Parameters;
//import android.hardware.Camera.PictureCallback;
//import android.hardware.Camera.ShutterCallback;
//import android.hardware.Camera.Size;
//
//public class CameraSurface extends SurfaceView implements PictureCallback, AutoFocusCallback {
//
//	private Camera _camera;
//	private CameraActivity _cameraActivity;
//	private int _defaultCameraId;
//	private int _cameraRotation;
//
//	private static final int CAMERA_MAX_PICTURE_SIZE = 1024;
//
//	public CameraSurface(Context context) {
//		super(context);
//		init(context);
//	}
//
//	public CameraSurface(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	public CameraSurface(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init(context);
//	}
//
//	public void init(Context context) {
//
//		_cameraActivity = (CameraActivity)context;
//
//		SurfaceHolder holder = this.getHolder();
//		holder.addCallback(new SurfaceHolder.Callback() {
//
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//				_camera.release();
//				_camera = null;
//			}
//
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//
//				int numberOfCameras = Camera.getNumberOfCameras();
//				_defaultCameraId = 0;
//
//			    CameraInfo cameraInfo = new CameraInfo();
//		        for (int i = 0; i < numberOfCameras; i++) {
//		            Camera.getCameraInfo(i, cameraInfo);
//		            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
//		                _defaultCameraId = i;
//		                break;
//		            }
//		        }
//			    _camera = Camera.open(_defaultCameraId);
//
//				try {
//					_camera.setPreviewDisplay(holder);
//				} catch(IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			public void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
//			     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
//			     android.hardware.Camera.getCameraInfo(cameraId, info);
//
//			      _cameraRotation = getRotationValue(activity.getWindowManager().getDefaultDisplay().getRotation());
//
//			     int result;
//			     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//			         result = (info.orientation + _cameraRotation) % 360;
//			         result = (360 - result) % 360;
//			     } else {
//			         result = (info.orientation - _cameraRotation + 360) % 360;
//			     }
//
//			     camera.setDisplayOrientation(result);
//			 }
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//				Parameters params = _camera.getParameters();
//				setPreviewSize(params);
//				setPictureSize(params);
//				setCameraDisplayOrientation(_cameraActivity, _defaultCameraId, _camera);
//
//				_camera.setParameters(params);
//				_camera.startPreview();
//			}
//
//			private void setPictureSize(Parameters params) {
//				Size pictureSize = params.getSupportedPictureSizes().get(0);
//				params.setPictureSize(pictureSize.width, pictureSize.height);
//			}
//
//			private void setPreviewSize(Parameters params) {
//				Size previewSize = params.getSupportedPreviewSizes().get(0);
//				params.setPreviewSize(previewSize.width, previewSize.height);
//			}
//
//		});
//
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
//			_camera.autoFocus(this);
//		}
//
//		return super.onTouchEvent(event);
//	}
//
//	@Override
//	public void onPictureTaken(byte[] data, Camera camera) {
//		Bitmap bitmap = BitmapUtility.createBitmap(data, CAMERA_MAX_PICTURE_SIZE,
//				getRotationValue(this._cameraActivity.getWindowManager().getDefaultDisplay().getRotation()));
//
//		Intent intent = new Intent();
//		Uri bitmapUri = BitmapUtility.savePhoto(_cameraActivity.getContentResolver(), bitmap);
//		intent.setData(bitmapUri);
//
//		_cameraActivity.setResult(Activity.RESULT_OK, intent);
//		_cameraActivity.finish();
//	}
//
//	private int getRotationValue(int rotationLiteral) {
//		int result = 0;
//
//		switch (rotationLiteral) {
//	         case Surface.ROTATION_0: result = 0; break;
//	         case Surface.ROTATION_90: result = 90; break;
//	         case Surface.ROTATION_180: result = 180; break;
//	         case Surface.ROTATION_270: result = 270; break;
//	     }
//		return result;
//	}
//
//	@Override
//	public void onAutoFocus(boolean success, Camera camera) {
//	}
//
//	public void takePicture() {
//		_camera.takePicture(
//				new ShutterCallback() {
//					@Override
//					public void onShutter() {
//					}
//				},
//				null,
//				this);
//	}
//}
//
