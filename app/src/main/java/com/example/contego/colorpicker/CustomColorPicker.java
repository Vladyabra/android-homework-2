package com.example.contego.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

public class CustomColorPicker extends HorizontalScrollView {
    private final int BOXES_COUNT = 16;
    private final int VISIBLE_BOXES_COUNT = 4;
    private final float MAX_HUE = 360;

    private boolean isScrollLocked;
    private Vibrator mVibrator;

    private OnColorPickedListener mOnColorClickedListener;
    private TextView[] mColorBoxes;

    public CustomColorPicker(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);

        mColorBoxes = new TextView[BOXES_COUNT];
        float sum = BOXES_COUNT + 0.25F * (1 + BOXES_COUNT);

        for (int i = 0; i < mColorBoxes.length; i++) {
            final TextView colorBox = new TextView(context);
            mColorBoxes[i] = colorBox;

            float hue = (1.25F * (1 + i) - 0.5F) * MAX_HUE / sum;

            colorBox.setBackgroundColor(Color.HSVToColor(new float[]{hue, 1, 1}));

            colorBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnColorClickedListener != null) {
                        Drawable drawable = colorBox.getBackground();
                        if (drawable instanceof ColorDrawable) {
                            ColorDrawable colorDrawable = (ColorDrawable) drawable;
                            mOnColorClickedListener.onColorPicked(colorDrawable.getColor());
                        }
                    }
                }
            });

            colorBox.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isScrollLocked = true;
                    mVibrator.vibrate(100);
                    return true;
                }
            });

            colorBox.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            System.out.println("x" + event.getX());
                            System.out.println("y" + event.getY());
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            isScrollLocked = false;
                            Toast.makeText(context, "Scroll released", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            root.addView(colorBox, layoutParams);
        }

        int[] gradientColors = new int[BOXES_COUNT + 1];
        for (int i = 0; i < gradientColors.length; i++) {
            gradientColors[i] = Color.HSVToColor(new float[]{i * MAX_HUE / BOXES_COUNT, 1, 1});
        }

        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors));

        this.addView(root);
    }

    public void setOnColorPickedListener(@Nullable OnColorPickedListener listener) {
        mOnColorClickedListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;

        double sum = VISIBLE_BOXES_COUNT + 0.25 * (1 + VISIBLE_BOXES_COUNT);
        int one = (int) (width / sum);
        int margin = (int) (0.25 * one);

        for (int i = 0; i < mColorBoxes.length; i++) {
            TextView view = mColorBoxes[i];

            view.setWidth(one);
            view.setHeight(one);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(i == 0 ? margin : 0, margin, margin, margin);
            view.setLayoutParams(params);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isScrollLocked && super.onTouchEvent(ev);
    }
}
