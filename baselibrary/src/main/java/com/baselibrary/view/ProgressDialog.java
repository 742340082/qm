package com.baselibrary.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baselibrary.R;
import com.baselibrary.utils.UIUtils;

public class ProgressDialog
        extends Dialog {

    private TextView mTextView;
    private ProgressBar pb_dialog;

    public ProgressDialog(Context context) {
        this(context, R.style.CustomDialog);
        setOwnerActivity((Activity) context);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(true);
    }


    public ProgressDialog(Context context, int style) {
        super(context, style);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        mTextView = (TextView) view.findViewById(R.id.tv_sundry_state);
        pb_dialog = (ProgressBar) view.findViewById(R.id.pb_dialog);
        super.setContentView(view);
    }
    public void setMax(int max)
    {
        pb_dialog.setMax(max);
    }
    public  void setProgress(int progress)
    {
        pb_dialog.setProgress(progress);
    }
    public ProgressDialog(Context context, boolean b, OnCancelListener listener) {
        super(context, b, listener);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.9F;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(params);
    }

    public void setTitle(String title) {
        mTextView.setText(title);
    }

    public void setTitle(int resId) {
        String title = UIUtils.getString(resId);
        mTextView.setText(title);
    }
}
