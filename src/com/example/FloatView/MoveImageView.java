package com.example.FloatView;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class MoveImageView extends ImageView implements View.OnTouchListener {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private WindowManager mWindowManager;
    // 当前触摸点距屏幕左上角X坐标
    private float mRawX;
    // 当前触摸点距屏幕坐上角Y坐标
    private float mRawY;
    // 手指刚触摸屏幕时，针对其父视图X轴坐标
    private float mStartX;
    // 手指刚触摸屏幕时，针对其父视图Y轴坐标
    private float mStartY;

    private float mStartRawX;
    // 当前触摸点距屏幕坐上角Y坐标
    private float mStartRawY;

    // 手指刚触摸屏幕时，针对其父视图X轴坐标
    private FloatViewParamsListener mListener;


    private int screenWidth;
    private int screenHeight;
    // ===========================================================
    // Constructors
    // ===========================================================

    public MoveImageView(Context context) {
        this(context, null, 0);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWindowManager = (WindowManager) getContext().getApplicationContext().
                getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        setOnTouchListener(this);
        return super.dispatchTouchEvent(event);


    }


    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * 设置监听器，用于向当前ImageView传递参数
     *
     * @param listener
     */
    public void setFloatViewParamsListener(FloatViewParamsListener listener) {
        mListener = listener;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int titleHeight = 0;
        if (mListener != null) {
            titleHeight = mListener.getTitleHeight();
        }

        // 当前值以屏幕左上角为原点
        mRawX = event.getRawX();
        mRawY = event.getRawY() - titleHeight;


        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 以当前父视图左上角为原点
                mStartX = event.getX();
                mStartY = event.getY();

                mStartRawX = event.getRawX();
                mStartRawY = event.getRawY() - titleHeight;

                break;

            case MotionEvent.ACTION_MOVE:
                updateWindowPosition();

                break;

            case MotionEvent.ACTION_UP:
                updateWindowPositionU();

//               int temp = 20;
//                if (mRawX + temp >= mStartRawX && mStartRawX >= mRawX - temp
//                        && mRawY + temp >= mStartRawY && mStartRawY >= mRawY - temp) {
//                    mListener.onClick();
//                }

                if (mRawX == mStartRawX && mRawY == mStartRawY) {
                    mListener.onClick();
                }


                break;
        }

        // 消耗触摸事件
        return true;
    }

    // ===========================================================
    // Private Methods
    // ===========================================================

    /**
     * 更新窗口参数，控制浮动窗口移动
     */
    private void updateWindowPosition() {
        if (mListener != null) {
            // 更新坐标
            LayoutParams layoutParams = mListener.getLayoutParams();
            layoutParams.x = (int) (mRawX - mStartX);
            layoutParams.y = (int) (mRawY - mStartY);

            // 使参数生效
            mWindowManager.updateViewLayout(this, layoutParams);
        }
    }


    /**
     * 更新窗口参数，控制浮动窗口移动
     */
    private void updateWindowPositionU() {
        if (mListener != null) {
            // 更新坐标
            LayoutParams layoutParams = mListener.getLayoutParams();
            layoutParams.x = (int) (mRawX - mStartX);
            layoutParams.y = (int) (mRawY - mStartY);

            if (layoutParams.x >= screenWidth / 2) {
                layoutParams.x = screenWidth;
            } else {
                layoutParams.x = 0;
            }
            // 使参数生效
            mWindowManager.updateViewLayout(this, layoutParams);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


    /**
     * 当前视图用于获取参数
     */
    public interface FloatViewParamsListener {

        /**
         * 获取标题栏高度
         * 因为需要通过Window对象获取，所以使用此办法
         *
         * @return
         */
        public int getTitleHeight();


        /**
         * 获取当前WindowManager.LayoutParams 对象
         *
         * @return
         */
        public LayoutParams getLayoutParams();


        public void onClick();
    }


}
