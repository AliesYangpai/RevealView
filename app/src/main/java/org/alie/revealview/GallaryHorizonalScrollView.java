package org.alie.revealview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by Alie on 2019/4/7.
 * 类描述
 * 版本
 */
public class GallaryHorizonalScrollView extends HorizontalScrollView implements View.OnTouchListener {

    private LinearLayout container;
    private int icon_width;
    private int centerX;

    public GallaryHorizonalScrollView(Context context) {
        super(context);
        init();
    }

    public GallaryHorizonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GallaryHorizonalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        container = new LinearLayout(getContext());
        container.setLayoutParams(layoutParams);
        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        View v = container.getChildAt(0);
        //得到某一张图片的宽度
        icon_width = v.getWidth();
        //得到hzv的中间x坐标
        centerX = getWidth() / 2;
        //处理下，中心坐标改为中心图片的左边界
        centerX = centerX - icon_width / 2;
        //给LinearLayout和hzv之间设置边框距离
        container.setPadding(centerX, 0, centerX, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            reveal();
        }
        return false;
    }

    private void reveal() {
        // 获取横向滑动的距离
        int scrollX = getScrollX();
        // 获取左边控件的索引
        int index_left = scrollX / icon_width;
        // 获取右边控件的所以
        int index_right = index_left + 1;

        for (int i = 0; i < container.getChildCount(); i++) {
            if (i == index_left || i == index_right) {

                // 获取每张图片所占用的显示比例
                float radio = 5000f / icon_width;
                ImageView iv_left = (ImageView) container.getChildAt(i);
                //scrollX%icon_width:代表滑出去的距离 (这里使用%取处余数，就代表剩余滑动部分的距离)
                int leftLevel = (int) (5000 - scrollX % icon_width * radio);
                iv_left.setImageLevel(leftLevel);
                //右边
                if (index_right < container.getChildCount()) {
                    ImageView iv_right = (ImageView) container.getChildAt(index_right);
                    int rightLevel = (int) (10000 - scrollX % icon_width * radio);
                    iv_right.setImageLevel(rightLevel);
                }
            } else {
                //灰色
                ImageView iv = (ImageView) container.getChildAt(i);
                iv.setImageLevel(0);
            }
        }
    }


    public void addImageViews(Drawable[] revealDrawables) {

        for (int i = 0; i < revealDrawables.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(revealDrawables[i]);
            container.addView(imageView);
            if (i == 0) {
                imageView.setImageLevel(5000);
            }
        }
        addView(container);
    }
}
