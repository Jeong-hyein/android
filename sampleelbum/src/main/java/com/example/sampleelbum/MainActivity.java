package com.example.sampleelbum;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    Button button;
    ImageView imageView;

    @Override //앱이 시작되면 onCreate실행
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //화면표시
        setContentView(R.layout.activity_main);

        //view 찾기
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        //버튼이벤트
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //앨범 앱 - intent
                openGallery();
            }
        });
        //권한체크
        AutoPermissions.Companion.loadAllPermissions(this,101);
    }//end of onCreate

    //앨범 선택 후 실행할 메소드
    //갤러리 앱 열기
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101); //forresult:결과를 가져오는거
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) { //정상적으로 가져왔으면 ok
            Uri fileUri= data.getData();
            ContentResolver resolver = getContentResolver();
            try{
                InputStream instream = resolver.openInputStream(fileUri);
                Bitmap imageBitmap = BitmapFactory.decodeStream(instream);
                imageView.setImageBitmap(imageBitmap);
                instream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
}//end of class
