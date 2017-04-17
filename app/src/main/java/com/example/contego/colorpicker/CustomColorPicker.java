package com.example.contego.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomColorPicker extends HorizontalScrollView {
    public final String TAG = "CustomColorPicker";

    private OnColorPickedListener mOnColorClickedListener;
    private TextView[] mColorBoxes;

    public CustomColorPicker(final Context context, AttributeSet attrs) {
        super(context, attrs);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);

        mColorBoxes = new TextView[16];

        for (int i = 0; i < mColorBoxes.length; i++) {
            final TextView colorBox = new TextView(context);
            mColorBoxes[i] = colorBox;

            float sum = 16 + 17 * 0.25F;
            colorBox.setBackgroundColor(Color.HSVToColor(new float[]{
                    (1.25F * (1 + i) - 0.5F) * 360 / sum, 1, 1}));

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

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            root.addView(colorBox, layoutParams);
        }

        int[] gradientColors = new int[17];
        for (int i = 0; i < gradientColors.length; i++) {
            gradientColors[i] = Color.HSVToColor(new float[]{i * 360 / 16F, 1, 1});
        }

        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors));

        this.addView(root);
    }

    public void setOnColorPickedListener(OnColorPickedListener l) {
        mOnColorClickedListener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;

        double sum = 0.25 + 1 + 0.25 + 1 + 0.25 + 1 + 0.25;
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
}
