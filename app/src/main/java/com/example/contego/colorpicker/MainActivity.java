package com.example.contego.colorpicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final String TEXT_COLOR_KEY = "textColorKey";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomColorPicker picker = (CustomColorPicker) findViewById(R.id.color_picker);
        mTextView = (TextView) findViewById(R.id.hello_world);

        if (savedInstanceState != null) {
            mTextView.setTextColor(savedInstanceState.getInt(TEXT_COLOR_KEY));
        }

        picker.setOnColorPickedListener(new OnColorPickedListener() {
            @Override
            public void onColorPicked(int color) {
                mTextView.setTextColor(color);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TEXT_COLOR_KEY, mTextView.getTextColors().getDefaultColor());
    }
}
