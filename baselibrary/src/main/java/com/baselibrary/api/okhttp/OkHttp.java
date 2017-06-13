package com.baselibrary.api.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baselibrary.utils.AESUtil;
import com.baselibrary.utils.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data");
    private static final String TAG = OkHttp.class.getSimpleName();
    private static final MediaType json = MediaType.parse("appliection/json;charset=utf-8");
    private String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "mcbDownload" + File.separator + "image";
    private okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
    private ApiListenerManager mListenerManager = new ApiListenerManager();

    private OkHttp()
    {

    }
    @Nullable
    public static OkHttp getInstance() {
        OkHttp manager=null;
        if (manager == null) {
            synchronized (OkHttp.class) {
                manager=new OkHttp();
                return manager;
            }
        }
        return null;
    }
    public long obtainFileContentLengByUrl(String url)
    {
        if(TextUtils.isEmpty(url))
        {
            return 0;
        }
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.code()==404)
            {
                return 0;
            }
            return  response.body().contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void asyncBitmapByUrl(String url, final ApiListenerManager.obtainBitmapStateListener listener) {
        Request request = new Request.Builder().url(url).build();
        this.client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttp.this.mListenerManager.OnErrorBitmapMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful())) {
                     byte[] bys = response.body().bytes();

                    if (listener != null) {
                       Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                        OkHttp.this.mListenerManager.OnSuccessBitmapMethod(bitmap, listener);
                    }
                }
            }
        });
    }

    public void asyncByteArrayByUrl(String url, final ApiListenerManager.obtainByteStateListener listener) {
      Request   request = new Request.Builder().url(url).build();
        this.client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttp.this.mListenerManager.OnErrorByteArrayMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful())) {
                   byte[]  bys = response.body().bytes();
                    if (listener != null) {
                        OkHttp.this.mListenerManager.OnSuccessByteArrayMethod(bys, listener);
                    }
                }
            }
        });
    }

    public void asyncFileByServer(final String url, final ApiListenerManager.onDownloadProgressListener listener) {

        Interceptor local2 = new Interceptor() {
            public Response intercept(Chain chain)
                    throws IOException {
                Response  response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgerssResponseBody(response.body(), listener)).build();
            }
        };
        Request localRequest = new Request.Builder().url(url).get().build();
        this.client.newBuilder().addInterceptor(local2).build().newCall(localRequest).enqueue(new Callback() {
            public void onFailure(Call callback, IOException e) {
                OkHttp.this.mListenerManager.onErrorDownFileMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful()) && (listener != null)) {
                   String  encode = AESUtil.encode(url);
                    String suffix = OkHttp.this.getSuffix(url);
                    String time = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(Calendar.getInstance().getTime());
                    String   downfileName = encode + time + (String) suffix;
                   File file = new File(OkHttp.this.DOWNLOAD_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    int i = OkHttp.this.getFileReturnCode(response.body().byteStream(), new File(file, downfileName));
                    OkHttp.this.mListenerManager.onSuccessDownFileMethod(i, listener);
                }
            }
        });
    }

    public void asyncStringByUrl(String url, final ApiListenerManager.obtainStringListener listener) {
        Request request = new Request.Builder().url(url).build();
        this.client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call callback, IOException e) {
                mListenerManager.OnErrorJsonStringMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful())) {
                 String   jsonString = response.body().string();
                    if ((callback != null) && (listener != null)) {
                      mListenerManager.OnSuccessJsonStringMethod(jsonString, listener);
                    }
                }
            }
        });
    }

    public void asyncUserImageByServer(String url, final String account, final String savePath ,final ApiListenerManager.onDownloadFileListener listener) {
        Request request = new Request.Builder().url(url).get().build();
        this.client.newBuilder().build().newCall(request).enqueue(new Callback() {
            public void onFailure(Call callback, IOException e) {
                OkHttp.this.mListenerManager.onErrorDownImageFileMethod(listener, e);
            }

            public void onResponse(Call paramAnonymousCall, Response paramAnonymousResponse)
                    throws IOException {
                if ((paramAnonymousResponse != null) && (paramAnonymousResponse.isSuccessful()) && (listener != null)) {
                   String encodeAccount = MD5Util.encode(account);
                   String downFileName  = encodeAccount + ".jpg";
                    File localFile = new File(savePath);
                    if (!localFile.exists()) {
                        localFile.mkdirs();
                    }
                    int i = getFileReturnCode(paramAnonymousResponse.body().byteStream(), new File(savePath, downFileName));
                    OkHttp.this.mListenerManager.onSuccessDownImageFileMethod(i, listener);
                }
            }
        });
    }

    /* Error */
    public int getFileReturnCode(java.io.InputStream inputStream, File downFile) {
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream = new FileOutputStream(downFile);

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bys)) != -1)
            {
                fileOutputStream.write(bys,0,len);
                fileOutputStream.flush();
            }
            return 102;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 101;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 103;
    }

    public String getSuffix(String url) {
        return url.substring(url.lastIndexOf("."));
    }

    public String syncStringByUrl(String url) {
       Request request = new Request.Builder().url(url).build();
        try {
          Response  response = this.client.newCall(request).execute();
            if (response.isSuccessful()) {
               String content = response.body().string();
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadFromDataToServer(String url, Map<String, String> map, final ApiListenerManager.obtainStringListener listener) {
        FormBody.Builder fromBody = new FormBody.Builder();
        if ((map != null) && (!map.isEmpty())) {
            for (Map.Entry item:map.entrySet()
                 ) {
                fromBody.add((String) item.getKey(), (String) item.getValue());
            }
        }
        RequestBody requestBody = fromBody.build();
       Request request = new Request.Builder().url(url).post(requestBody).build();
        this.client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call callback, IOException e) {
                OkHttp.this.mListenerManager.OnErrorJsonStringMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful()) && (listener != null)) {
                    OkHttp.this.mListenerManager.OnSuccessJsonStringMethod(response.body().string(), listener);
                }
            }
        });
    }

    public void uploadStringTOServer(String url, String content, final ApiListenerManager.obtainStringListener listener) {
       Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        this.client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call callback, IOException e) {
                OkHttp.this.mListenerManager.OnErrorJsonStringMethod(listener, e);
            }

            public void onResponse(Call callback, Response response)
                    throws IOException {
                if ((response != null) && (response.isSuccessful()) && (listener != null)) {
                    OkHttp.this.mListenerManager.OnSuccessJsonStringMethod(response.body().string(), listener);
                }
            }
        });
    }
}
