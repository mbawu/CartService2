package com.cn.hongwei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.xqxy.carservice.activity.PhotoSelectDialog;

public abstract class PhotoActivity extends BaseActivity {
	public static final int REQUEST_CODE_SELECT_PHOTO = 1;
	public static final int REQUEST_CODE_CAMERA = 2;
	public static final int REQUEST_CODE_CORP = 3;

	private PhotoSelectDialog dialog;

	private String imgPath;
	Uri imageUri;

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
		imageUri = Uri.parse("file://" + imgPath);
		if (dialog == null) {
			dialog = new PhotoSelectDialog(this, imgPath);
		} else {
			dialog.setImgPath(imgPath);
		}
	}

	public void showSelectPhotoDialog(String imgPath) {
		setImgPath(imgPath);
		dialog.show();
	}

	public void showSelectPhotoDialog() {

		dialog.show();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			try {
				switch (requestCode) {
				case REQUEST_CODE_SELECT_PHOTO:
					Uri uri = data.getData();
					Log.i("imge uri", uri.toString());
					ContentResolver cr = this.getContentResolver();
					try {
						String path = null;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
							String wholeID = DocumentsContract
									.getDocumentId(uri);
							String id = wholeID.split(":")[1];
							String[] column = { MediaStore.Images.Media.DATA };
							String sel = MediaStore.Images.Media._ID + "=?";
							Cursor cursor = this
									.getContentResolver()
									.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											column, sel, new String[] { id },
											null);
							int columnIndex = cursor.getColumnIndex(column[0]);
							if (cursor.moveToFirst()) {
								path = cursor.getString(columnIndex);
							}
							// cursor.close();
						} else {
							String[] proj = { MediaStore.Images.Media.DATA };
							Cursor cursor = managedQuery(uri, proj, null, null,
									null);
							int column_index = cursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							cursor.moveToFirst();
							path = cursor.getString(column_index);
							// cursor.close();
						}
						if (path != null) {
							startPhotoZoom(Uri.fromFile(new File(path)));
						}

					} catch (Exception e) {
						Log.e("Exception", e.getMessage(), e);
					}
					break;
				case REQUEST_CODE_CAMERA:

					startPhotoZoom(Uri.fromFile(new File(imgPath)));
					// tempImagePath = imgPath;
					// uploadImg(imgPath);
					break;
				case REQUEST_CODE_CORP:
					// tempImgUri = Uri.fromFile(new File(imgPath));

					uploadImg(imgPath);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	protected void openAlbum() {
		Intent photoIntent = new Intent();
		photoIntent.setType("image/*");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			photoIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		} else {
			photoIntent.setAction(Intent.ACTION_GET_CONTENT);
		}
		startActivityForResult(photoIntent, REQUEST_CODE_SELECT_PHOTO);
	}

	protected void startCamera() {
		File vFile = new File(imgPath);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		}
		Uri uri = Uri.fromFile(vFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	protected void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(imgPath)));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, PhotoActivity.REQUEST_CODE_CORP);
	}

	public abstract void uploadImg(String imagePath);

	protected String fileToString(String url) {
		String data = null;
		try {
			File file = new File(url);
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			int length = in.read(buffer);
			data = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
			in.close();

			// byte[] byteData = data.getBytes();

			// try {
			// OutputStream os = new FileOutputStream(new File(url + ".txt"));
			// os.write(byteData);
			// os.flush();
			// os.close();
			// } catch (FileNotFoundException e) {
			//
			// e.printStackTrace();
			// } catch (IOException e) {
			//
			// e.printStackTrace();
			// }

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
