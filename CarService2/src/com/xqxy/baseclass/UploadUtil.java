package com.xqxy.baseclass;

import java.io.File;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;

public class UploadUtil {

	//private final String BOUNDARY = UUID.randomUUID().toString(); // 随机生成
	//private final String CONTENT_TYPE = "multipart/form-data";
	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	private int requestTime = 0;
	//private final String CHARSET = "utf-8"; // 设置编码
	public static final int UPLOAD_SUCCESS_CODE = 1;
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;
	private Gson mGson = new Gson();

	public void uploadFile(String filePath, String RequestURL) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			uploadFile(file, RequestURL);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			e.printStackTrace();
			return;
		}
	}

	public void uploadFile(final File file, final String RequestURL) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		}

		Log.i(TAG, "请求的URL=" + RequestURL);
		Log.i(TAG, "请求的fileName=" + file.getName());
		new Thread(new Runnable() { // 开启线程上传文件
					@Override
					public void run() {
						toUploadFile(file, RequestURL);
					}
				}).start();

	}

	private void toUploadFile(File file, String RequestURL) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(RequestURL);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		try {
			// 需要上传的文件
			File uploadFile = file;
			// 定义FileEntity对象
			ContentBody cbFile = new FileBody(uploadFile, "image/jpg");
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("file", cbFile);
			//entity.addPart("identity", new StringBody(MyApplication.identity));
			// 为httpPost设置头信息
			httpPost.setHeader("filename", URLEncoder.encode("meme", "utf-8"));// 服务器可以读取到该文件名
			httpPost.setEntity(entity); // 设置实体对象

			// httpClient执行httpPost提交
			HttpResponse response = httpClient.execute(httpPost);
			// 得到服务器响应实体对象
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				// System.out.println(EntityUtils.toString(responseEntity,
				// "utf-8"));
				try {
					String resp = EntityUtils.toString(responseEntity, "utf-8");
					ResponseWrapper rw = mGson.fromJson(resp, ResponseWrapper.class);
					if("true".equals(rw.getDone())){
						sendMessage(UPLOAD_SUCCESS_CODE, rw.getSrc());
					}else{
						sendMessage(UPLOAD_SERVER_ERROR_CODE, rw.getMsg());
					}
					
				} catch (Exception e) {
					sendMessage(UPLOAD_SERVER_ERROR_CODE, "获取图片保存路径失败");
					e.printStackTrace();
				}
			} else {
				System.out.println("服务器无响应！");
				sendMessage(UPLOAD_SERVER_ERROR_CODE, "服务器无响应");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败");
		} finally {
			// 释放资源
			httpClient.getConnectionManager().shutdown();
		}
	}


	public String getPath(Context ctx, Uri uri) {
		String path = null;
		Log.i("imge uri", uri.toString());
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = ((Activity) ctx).managedQuery(uri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);

		} catch (Exception e) {
			Log.e("Exception", e.getMessage(), e);
		}

		return path;
	}

	private void sendMessage(int responseCode, String responseMessage) {
		if (onUploadProcessListener != null) {
			onUploadProcessListener.onUploadDone(responseCode, responseMessage);
		}
	}

	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * 
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);

		/**
		 * 上传中
		 * 
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);

		/**
		 * 准备上传
		 * 
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}

	private OnUploadProcessListener onUploadProcessListener;

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getRequestTime() {
		return requestTime;
	}

	public static interface uploadProcessListener {

	}

}
