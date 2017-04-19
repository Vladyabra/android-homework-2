package com.example.contego.colorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final static String TEXT_VALUE_KEY = "textValueKey";
    private final static String TEXT_BG_COLOR_KEY = "textBgColorKey";
    private TextView mColorDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomColorPicker picker = (CustomColorPicker) findViewById(R.id.color_picker);
        mColorDisplay = (TextView) findViewById(R.id.color_display);

        picker.setOnColorPickedListener(new OnColorPickedListener() {
            @Override
            public void onColorPicked(int color) {
                mColorDisplay.setBackgroundColor(color);
                StringBuilder sb = new StringBuilder();

                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                sb.append("Red = ").append(r).append('\n');
                sb.append("Green = ").append(g).append('\n');
                sb.append("Blue = ").append(b).append('\n');

                sb.append('\n');

                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);

                sb.append("Hue = ").append(hsv[0]).append('\n');
                sb.append("Saturation = ").append(hsv[1]).append('\n');
                sb.append("Value = ").append(hsv[2]);

                mColorDisplay.setText(sb.toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Drawable drawable = mColorDisplay.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            outState.putInt(TEXT_BG_COLOR_KEY, colorDrawable.getColor());
        }

        outState.putCharSequence(TEXT_VALUE_KEY, mColorDisplay.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mColorDisplay.setText(savedInstanceState.getCharSequence(TEXT_VALUE_KEY));
            if (savedInstanceState.containsKey(TEXT_BG_COLOR_KEY)) {
                mColorDisplay.setBackgroundColor(savedInstanceState.getInt(TEXT_BG_COLOR_KEY));
            }
        }
    }
}
