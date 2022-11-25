package edu.skku.gcd.personalcolor;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadUtils {

    public static void send2Server(File file){
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file))
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:4903/sendimg") // Server URL 은 본인 IP를 입력
                .post(requestBody)
                .build();*/
        //https://esrkolmn6l.execute-api.ap-northeast-1.amazonaws.com/dev/sendimg
        Request request = new Request.Builder().url("http://10.0.2.2:4903/getuser").build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("111","failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("111 : ", response.body().string() + "  from request");
            }
        });
    }
}
