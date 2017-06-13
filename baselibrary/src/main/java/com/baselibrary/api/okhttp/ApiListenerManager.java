package com.baselibrary.api.okhttp;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

public class ApiListenerManager
{
    private Handler handler = new Handler(Looper.getMainLooper());

    public void OnErrorBitmapMethod(final obtainBitmapStateListener paramobtainBitmapStateListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramobtainBitmapStateListener != null) {
                    paramobtainBitmapStateListener.onError(paramException);
                }
            }
        });
    }

    public void OnErrorByteArrayMethod(final obtainByteStateListener paramobtainByteStateListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramobtainByteStateListener != null) {
                    paramobtainByteStateListener.onError(paramException);
                }
            }
        });
    }

    public void OnErrorJsonObjectMethod(final obtainJsonObjectListener paramobtainJsonObjectListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramobtainJsonObjectListener != null) {
                    paramobtainJsonObjectListener.onError(paramException);
                }
            }
        });
    }

    public void OnErrorJsonStringMethod(final obtainStringListener paramobtainStringListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramobtainStringListener != null) {
                    paramobtainStringListener.onError(paramException);
                }
            }
        });
    }

    public void OnSuccessBitmapMethod(final Bitmap paramBitmap, final obtainBitmapStateListener paramobtainBitmapStateListener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (paramobtainBitmapStateListener != null) {
                        paramobtainBitmapStateListener.onResponst(paramBitmap);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void OnSuccessByteArrayMethod(final byte[] paramArrayOfByte, final obtainByteStateListener paramobtainByteStateListener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (paramobtainByteStateListener != null) {
                        paramobtainByteStateListener.onResponst(paramArrayOfByte);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void OnSuccessJsonObjectMethod(final String paramString, final obtainJsonObjectListener paramobtainJsonObjectListener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (paramobtainJsonObjectListener != null) {
                        paramobtainJsonObjectListener.onResponst(new JSONObject(paramString));
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void OnSuccessJsonStringMethod(final String paramString, final obtainStringListener paramobtainStringListener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (paramobtainStringListener != null) {
                        paramobtainStringListener.onResponst(paramString);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void onErrorDownFileMethod(final onDownloadProgressListener paramonDownloadProgressListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramonDownloadProgressListener != null) {
                    paramonDownloadProgressListener.onError(paramException);
                }
            }
        });
    }

    public void onErrorDownImageFileMethod(final onDownloadFileListener paramonDownloadFileListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramonDownloadFileListener != null) {
                    paramonDownloadFileListener.onError(paramException);
                }
            }
        });
    }

    public void onErrorUploadFileMethod(final onUploadFileProgressListener paramonUploadFileProgressListener, final Exception paramException)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                if (paramonUploadFileProgressListener != null) {
                    paramonUploadFileProgressListener.onError(paramException);
                }
            }
        });
    }

    public void onSuccessDownFileMethod(final int paramInt, final onDownloadProgressListener paramonDownloadProgressListener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (paramonDownloadProgressListener != null) {
                        paramonDownloadProgressListener.onResponst(paramInt);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void onSuccessDownImageFileMethod(final int i, final onDownloadFileListener listener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (listener != null) {
                        listener.onResponst(i);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public void onSuccessUploadFileMethod(final String s, final onUploadFileProgressListener listener)
    {
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (listener != null) {
                        listener.onResponst(s);
                    }
                    return;
                }
                catch (Exception localException)
                {
                    localException.printStackTrace();
                }
            }
        });
    }

    public static abstract interface obtainBitmapStateListener
    {
        public abstract void onError(Exception e);

        public abstract void onResponst(Bitmap bitmap);
    }

    public static abstract interface obtainByteStateListener
    {
        public abstract void onError(Exception e);

        public abstract void onResponst(byte[] bytes);
    }

    public static abstract interface obtainJsonObjectListener
    {
        public abstract void onError(Exception e);

        public abstract void onResponst(JSONObject jsonObject);
    }

    public static abstract interface obtainStringListener
    {
        public abstract void onError(Exception e);

        public abstract void onResponst(String content);
    }

    public static abstract interface onDownloadFileListener
    {
        public abstract void onError(Exception e);

        public abstract void onResponst(int successCode);
    }

    public static abstract interface onDownloadProgressListener
    {
        public abstract void onError(Exception e);

        public abstract void onProgress(long bytesWritten, long contentLength);

        public abstract void onResponst(int content);
    }

    public static abstract interface onUploadFileProgressListener
    {
        public abstract void onError(Exception e);

        public abstract void onProgress(long bytesWritten, long contentLength);

        public abstract void onResponst(String content);
    }

    public static abstract interface onUploadImageFileProgressListener
    {
        public abstract void onProgress(long bytesWritten, long contentLength);
    }
}
