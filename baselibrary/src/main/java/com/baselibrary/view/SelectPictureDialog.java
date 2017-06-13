package com.baselibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.baselibrary.R;
import com.baselibrary.utils.UIUtils;

public class SelectPictureDialog
        extends Dialog
        implements View.OnClickListener
{
    private OnDialogClickListener mListener;

    public SelectPictureDialog(Context paramContext)
    {
        this(paramContext, R.style.Theme_AppCompat_Dialog);
    }

    public SelectPictureDialog(Context context, int style)
    {
        super(context, style);
        View view = UIUtils.inflate(R.layout.dialog_select_picture);
        view.findViewById(R.id.tv_photo).setOnClickListener(this);
        view.findViewById(R.id.tv_take).setOnClickListener(this);
        view.findViewById(R.id.tv_close).setOnClickListener(this);
        super.setContentView(view);
    }

    public SelectPictureDialog(Context paramContext, boolean paramBoolean, OnCancelListener paramOnCancelListener)
    {
        super(paramContext, paramBoolean, paramOnCancelListener);
    }

    public void onClick(View paramView)
    {
        dismiss();
        if (this.mListener != null) {
            this.mListener.dialogClick(paramView, paramView.getId());
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);
        int screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = screenWidth;
        getWindow().setAttributes(params);
    }

    public void setOnDialogClickListener(OnDialogClickListener paramOnDialogClickListener)
    {
        this.mListener = paramOnDialogClickListener;
    }

    public static abstract interface OnDialogClickListener
    {
        public abstract void dialogClick(View paramView, int paramInt);
    }
}
