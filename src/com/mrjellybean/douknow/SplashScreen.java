package com.mrjellybean.douknow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class SplashScreen extends Activity {
	WebView w1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        w1 = (WebView) findViewById(R.id.webView1);
		w1.loadUrl("file:///android_asset/questionmark.gif");
		Thread timer = new Thread() {

			@Override
			public void run() {
				try {

					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					Intent i = new Intent(getBaseContext(), DoUKnow.class);
					startActivity(i);

				}
			}
		};
		timer.start();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }
}
