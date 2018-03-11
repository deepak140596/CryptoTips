package com.localli.deepak.cryptotips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.RIGHT;

/**
 * Created by Deepak Prasad on 23-01-2018.
 */

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        Element madeInIndia=new Element();
        madeInIndia.setTitle("Made with ❤ in India!");
        madeInIndia.setGravity(CENTER_HORIZONTAL);

        Element poweredBy=new Element();
        poweredBy.setTitle("Powered By newsapi.org");
        poweredBy.setGravity(RIGHT);

        //Element psElement=new Element();
        //psElement.setDe

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.crypto_trade)
                .setDescription(getString(R.string.string_about_us_description))
                .addItem(poweredBy)
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("locallidelivers107@gmail.com")
                .addItem(madeInIndia)
                .create();

        setContentView(aboutPage);
    }
}
