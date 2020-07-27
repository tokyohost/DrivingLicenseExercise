package com.example.drivinglicenseexercise.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;

import com.example.drivinglicenseexercise.R;
import com.example.drivinglicenseexercise.Utils.UiUtils;

/**
 * @desciption: Dialog 基类
 */
public class BaseDialog extends AppCompatDialog {

    private static BaseDialog mBaseDialog;
    private Builder mBuilder;

    private BaseDialog(Builder builder) {
        this(builder, R.style.BaseDialogStyle);
    }

    private BaseDialog(Builder builder, int theme) {
        super(builder.mContext, theme);
        mBuilder = builder;
    }

    /**
     * 取消Dialog
     */
    public void dismissDialog() {
        mBaseDialog.dismiss();
        mBaseDialog = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mBuilder.mView);
        setCanceledOnTouchOutside(mBuilder.cancelTouchout);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            if (mBuilder.width > 0) {
                params.width = mBuilder.width;
            }
            if (mBuilder.height > 0) {
                params.height = mBuilder.height;
            }
            params.gravity = mBuilder.gravity;
            window.setAttributes(params);
        }
    }

    @Override
    public void show() {
        if (!mBaseDialog.isShowing()) {
            super.show();
        }
    }

    public static final class Builder {
        private Context mContext;
        private boolean cancelTouchout = false;
        private int width, height;
        private int gravity;
        private View mView;
        private int resStyle = -1;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setView(int layoutId) {
            mView = LayoutInflater.from(mContext).inflate(layoutId, null);
            return this;
        }

        public Builder setView(View view) {
            mView = view;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setWidthdp(int val) {
            width = UiUtils.getInstance().dip2px(val);
            return this;
        }

        public Builder setHeightdp(int val) {
            height = UiUtils.getInstance().dip2px(val);
            return this;
        }

        public Builder setWidthDimenRes(int dimenRes) {
            width = UiUtils.getInstance().getDimens(dimenRes);
            return this;
        }

        public Builder setHeightDimenRes(int dimenRes) {
            height = UiUtils.getInstance().getDimens(dimenRes);
            return this;
        }

        public Builder setStyle(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder setCancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnClick(int viewRes, View.OnClickListener listener) {
            mView.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public <T extends View> T findViewById(int id) {
            return mView.findViewById(id);
        }

        /**
         * 防止Dialog show两次
         */
        public BaseDialog build() {
            if (mBaseDialog == null) {
                if (resStyle != -1) {
                    mBaseDialog = new BaseDialog(this, resStyle);
                } else {
                    mBaseDialog = new BaseDialog(this);
                }
            }
            return mBaseDialog;
        }
    }
}