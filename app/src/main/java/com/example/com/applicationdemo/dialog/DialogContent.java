package com.example.com.applicationdemo.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.example.com.application.R;


/**
 * 广告栏提示框
 */
public class DialogContent extends Dialog {
    private Context context = null;
    private static DialogContent customProgressDialog = null;

    public DialogContent(Context context){
        super(context);
        this.context = context;
    }

    public DialogContent(Context context, int theme) {
        super(context, theme);
    }

    public static DialogContent createDialogContent(Context context){
        customProgressDialog = new DialogContent(context, R.style.DialogContent);
        customProgressDialog.setContentView(R.layout.dialog_content);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus){

        if (customProgressDialog == null){
            return;
        }

        // 此处的代码在xml里也有配置
       //  ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
  //      AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
    //    animationDrawable.start();
    }

    /**
     *
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public DialogContent setTitile(String strTitle){
        return customProgressDialog;
    }


}
