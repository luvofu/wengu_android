package com.culturebud.ui.me;

import android.os.Bundle;
import android.webkit.WebView;

import com.culturebud.R;
import com.culturebud.TitleBarActivity;

/**
 * Created by XieWei on 2016/11/2.
 */

public class UserAgreementActivity extends TitleBarActivity {
    private WebView wvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agreement);
        wvAgreement = obtainViewById(R.id.wv_agreement);
        setTitle(R.string.user_agreement);

//        wvAgreement.loadUrl("https://mywengu.com/userAgreement.html");
        wvAgreement.loadUrl("https://www.uvnya.com/doc/userAgreement.html");
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
