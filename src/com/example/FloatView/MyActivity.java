package com.example.FloatView;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取Service
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);


        // 设置窗口类型，一共有三种Application windows, Sub-windows, System windows
        // API中以TYPE_开头的常量有23个

        mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_STARTING;
        // 设置期望的bitmap格式
        mWindowParams.format = PixelFormat.RGBA_8888;
        //设置Window flag
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;

        // 以下属性在Layout Params中常见重力、坐标，宽高
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = 0;

        mWindowParams.width = 80;
        mWindowParams.height = 80;

        // 自定义图片视图
        MoveImageView imageView = new MoveImageView(this);
        imageView.setImageResource(R.drawable.floatview_appicon_normal);

        imageView.setFloatViewParamsListener(new FloatViewListener());

        // 添加指定视图
        mWindowManager.addView(imageView, mWindowParams);

        imageView.setOnClickListener(this);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MyActivity.this, "点击tupian-onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void onClick(View view) {
        Toast.makeText(this, "点击tupian-onClick", Toast.LENGTH_SHORT).show();
    }


    private class FloatViewListener implements MoveImageView.FloatViewParamsListener {
        public int getTitleHeight() {
            // 获取状态栏高度。不能在onCreate回调方法中获取
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentTop - statusBarHeight;

            return titleBarHeight;
        }

        public WindowManager.LayoutParams getLayoutParams() {
            return mWindowParams;
        }

        public void onClick() {
            Toast.makeText(MyActivity.this, "点击tupian", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //  删除视图
        //     mWindowParams.removeView(mImageView);
    }

    public void click(View v) {

        Toast.makeText(this, "点击按钮", Toast.LENGTH_SHORT).show();
    }


}
