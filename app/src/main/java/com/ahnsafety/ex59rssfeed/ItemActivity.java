package com.ahnsafety.ex59rssfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ItemActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent= getIntent();
        String link= intent.getStringExtra("Link");

        //얻어온 링크주소를 웹뷰에 보여주기!
        wv=findViewById(R.id.wv);
        //웹페이지에서 사용하는 Javascript를 동작하도록
        wv.getSettings().setJavaScriptEnabled(true);

        //웹뷰에 페이지를 load하면 안드로이드에서
        //자동으로 새로운 웹브라우저를 열어버림
        //그걸 안하고 내 WebView에 페이지를 보이도록

        wv.setWebViewClient(new WebViewClient());

        //웹 페이지안에 웹다이어로그를 보여주는 등의
        //작업 있다면.. 그걸 동작하도록.
        wv.setWebChromeClient(new WebChromeClient());

        wv.loadUrl(link);
    }
}
