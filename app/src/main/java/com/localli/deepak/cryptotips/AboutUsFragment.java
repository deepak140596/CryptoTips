package com.localli.deepak.cryptotips;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;

/**
 * Created by Deepak Prasad on 01-01-2019.
 */

public class AboutUsFragment extends Fragment {

    LinearLayout githubLL, linkedinLL, emailLL, playstoreLL;
    LinearLayout preferredCurrencyLL, rateUsLL;

    TextView selectedPrefCurrencyTv, privacyPolicyTv, sendFeedbackTv;


    // Insert your Application Package Name
    private final static String PACKAGE_NAME ="com.localli.deepak.cryptotips";

    Context context;
    AppCompatActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        context = getContext();
        activity = (AppCompatActivity)getActivity();

        return inflater.inflate(R.layout.fragment_about_us,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseViews(view);
    }

    private void initialiseViews(View view){
        githubLL = view.findViewById(R.id.profile_github_ll);
        linkedinLL = view.findViewById(R.id.profile_linkedin_ll);
        emailLL = view.findViewById(R.id.profile_email_ll);
        playstoreLL = view.findViewById(R.id.profile_playstore_ll);
        preferredCurrencyLL = view.findViewById(R.id.profile_settings_currency_ll);
        rateUsLL = view.findViewById(R.id.profile_rate_us_ll);
        selectedPrefCurrencyTv = view.findViewById(R.id.profile_selected_currency_tv);
        privacyPolicyTv = view.findViewById(R.id.profile_privacy_policy_tv);
        sendFeedbackTv = view.findViewById(R.id.profile_send_feedback_tv);


        githubLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.github_id));
            }
        });

        linkedinLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.linkedin_id));
            }
        });

        playstoreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.playstore_id));
            }
        });

        privacyPolicyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.privacy_policy_url));
            }
        });

        emailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startEmailIntent(getString(R.string.email_id),"");
            }
        });

        rateUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + PACKAGE_NAME)));
            }
        });

        sendFeedbackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEmailIntent(getString(R.string.email_id),"Feedback");
            }
        });

        preferredCurrencyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyList();
            }
        });

        setupPreferredCurrencyTextView();

    }


    private void startIntent(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void startEmailIntent(String emailId,String subject){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(intent, "Choose Email Client"));
    }

    private void showCurrencyList(){

        final String[] supportedCurrencies = getResources().getStringArray(R.array.currencies);
        String preferredCurrency = SharedPrefSimpleDB.getPreferredCurrency(context);

        int i;
        for(i=0;i<supportedCurrencies.length;i++){
            if(supportedCurrencies[i].equalsIgnoreCase(preferredCurrency))
                break;
        }

        new MaterialDialog.Builder(activity)
                .title("Select Currency")
                .items(supportedCurrencies)
                .buttonRippleColor(Color.RED)
                .itemsCallbackSingleChoice(i, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        SharedPrefSimpleDB.savePreferredCurrency(context,supportedCurrencies[which]);
                        setupPreferredCurrencyTextView();
                        return false;
                    }
                }).show();
    }

    private void setupPreferredCurrencyTextView(){
        String prefCurrency = SharedPrefSimpleDB.getPreferredCurrency(context);
        selectedPrefCurrencyTv.setText(prefCurrency.toUpperCase());
    }

}
