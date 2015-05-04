package com.xqxy.carservice.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.PhotoActivity;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.PhotoSelectDialog;
import com.xqxy.carservice.view.TopTitleView;

public class PersonInfoActivity extends PhotoActivity implements
		OnClickListener {
	Dialog dialog;

	private TopTitleView topTitleView;
	private ImageView imgHeadPhoto;
	private String imgPath = Environment.getExternalStorageDirectory()
			.getPath() + "/CarTemp/head.jpg";
	
	private String txtPath = Environment.getExternalStorageDirectory()
			.getPath() + "/CarTemp/head.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_layout);
		topTitleView = new TopTitleView(this);
		imgHeadPhoto = (ImageView) findViewById(R.id.img_person_info_head);
		findViewById(R.id.text_person_info_car).setOnClickListener(this);
		findViewById(R.id.layout_person_info_head).setOnClickListener(this);
		findViewById(R.id.btn_person_info_exit).setOnClickListener(this);
		dialog = new PhotoSelectDialog(this, imgPath);
		setImgPath(imgPath);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_person_info_head:
			showSelectPhotoDialog();
			break;
		case R.id.text_person_info_car:
			startActivity(new Intent(this, CarListActivity.class));
			break;
		case R.id.btn_person_info_exit:
			break;
		}
	}

	public void showSelectPhotoDialog() {

		dialog.show();
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if(requestType == NetworkAction.centerF_head){
			
		}
	}
	
	
	public void uploadImg(String imagePath) {
		Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
		
		try {
			File file = new File(imagePath);  
			FileInputStream in = new FileInputStream(file);  
			byte[] buffer = new byte[(int) file.length()];  
			int length = in.read(buffer);  
			String data = Base64.encodeToString(buffer, 0, length,  
			        Base64.DEFAULT);
			in.close();
			
			
		        byte[] bytexml = data.getBytes();  
		          
		        try {  
		            OutputStream os = new FileOutputStream(new File(txtPath));  
		            os.write(bytexml);  
		            os.flush();  
		            os.close();  
		        } catch (FileNotFoundException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
			
			
			RequestWrapper request = new RequestWrapper();
			request.setIdentity( MyApplication.identity);
			request.setFile(data);
			sendData(request, NetworkAction.centerF_head);
			        
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
