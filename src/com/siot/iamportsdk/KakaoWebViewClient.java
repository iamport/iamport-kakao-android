package com.siot.iamportsdk;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KakaoWebViewClient extends WebViewClient {
	
	private Activity activity;
	
	public KakaoWebViewClient(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		
		if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
			Intent intent = null;
			
			try {
				intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
				Uri uri = Uri.parse(intent.getDataString());
				
				activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
				return true;
			} catch (URISyntaxException ex) {
				return false;
			} catch (ActivityNotFoundException e) {
				if ( intent == null )	return false;
				
				String packageName = intent.getPackage(); //packageName should be com.kakao.talk
		        if (packageName != null) {
		            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
		            return true;
		        }
		        
		        return false;
			}
		}
		
		return false;
	}
	
	/**
	 * @param scheme
	 * @return 해당 scheme에 대해 처리를 직접 하는지 여부
	 * 
	 * 1. 결제를 위한 3rd-party 앱이 아직 설치되어있지 않을 때, 호출되는 url에 package정보가 없어 market으로 연결이 안되는 경우 사전 처리를 해주어야 합니다.(ActivityNotFoundException에서 처리가 안되는 경우)
	 * 2. 3rd-party에 따라 구매자를 위한 custom message를 제공한다거나 별도의 처리를 해야 할 필요가 있을 때 사용합니다. 
	 * 
	 */
	protected boolean handle3rdPartyPaymentScheme(String scheme) {
		//PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
		if ( PaymentScheme.ISP.equalsIgnoreCase(scheme) ) {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
			return true;
		}
		
		return false;
	}
	
}
