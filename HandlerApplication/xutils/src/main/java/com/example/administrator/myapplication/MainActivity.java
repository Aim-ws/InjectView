package com.example.administrator.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.annotation.ContentView;
import com.example.administrator.myapplication.annotation.OnClick;
import com.example.administrator.myapplication.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.tv_1)
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectView.inject(this);

        Log.i("", "  tv is " + (null == tv ? "null" : tv.toString()));

        tv.setText("Handler Application  ");
    }

    @OnClick(R.id.tv_1)
    public void onTextViewClick(View view) {
        Toast.makeText(MainActivity.this, "hello word", Toast.LENGTH_LONG).show();
    }
}
