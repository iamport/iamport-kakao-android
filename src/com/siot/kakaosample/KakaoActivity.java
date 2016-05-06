package com.siot.kakaosample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.siot.iamportsdk.KakaoWebViewClient;
import com.siot.iamportsdk.PaymentScheme;

public class KakaoActivity extends Activity {

	private WebView mainWebView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);
        
        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        
//        mainWebView.loadUrl("http://www.iamport.kr/demo");
    	mainWebView.loadUrl("http://192.168.1.8");
    }
    
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	Intent intent = getIntent();
    	if ( intent != null ) {
    		Uri intentData = intent.getData();

    		if ( intentData != null ) {
            	//카카오페이 인증 후 복귀했을 때 결제 후속조치
            	String url = intentData.toString();
            	String scheme = PaymentScheme.KAKAO + "://";
            	
            	if ( url.startsWith(scheme) ) {
            		String path = url.substring(scheme.length());
            		if ( "process".equalsIgnoreCase(path) ) {
            			mainWebView.loadUrl("javascript:IMP.communicate({result:'process'})");
            		} else {
            			mainWebView.loadUrl("javascript:IMP.communicate({result:'cancel'})");
            		}
            	}
            }
    	}
        
    }
}
