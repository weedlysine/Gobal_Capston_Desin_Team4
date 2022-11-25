package edu.skku.gcd.personalcolor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imgVwSelected;
    Button btnImageSend, btnImageSelection;
    File tempSelectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Log.i("111","여기까지0");


        btnImageSend = findViewById(R.id.button4);
        btnImageSend.setEnabled(false);
        btnImageSelection = findViewById(R.id.button2);
        btnImageSelection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Intent를 통해 이미지를 선택
                Intent intent = new Intent();
                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        imgVwSelected = findViewById(R.id.imageView2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.item1:
                return true;
            case R.id.item2:
                return true;
            case R.id.item3:
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    public void startNewActivity(View v)
    {
        Intent intent = new Intent(MainActivity.this, take_video.class);
        startActivity(intent);
    }

    public void test_button(View v)
    {
        Log.i("111",tempSelectFile.getName());
        FileUploadUtils.send2Server(tempSelectFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }

        Uri dataUri = data.getData();
        imgVwSelected.setImageURI(dataUri);

        try {
            // ImageView 에 이미지 출력
            InputStream in = getContentResolver().openInputStream(dataUri);
            Bitmap image = BitmapFactory.decodeStream(in);
            imgVwSelected.setImageBitmap(image);
            in.close();

            // 선택한 이미지 임시 저장
            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            tempSelectFile = new File(Environment.getExternalStorageDirectory() + "/Pictures/Test/", "temp_" + date + ".jpeg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        btnImageSend.setEnabled(true);
    }


}