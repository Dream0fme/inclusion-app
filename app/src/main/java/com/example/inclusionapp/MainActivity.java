package com.example.inclusionapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText editTxt, editPars;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);
        editTxt = findViewById(R.id.editTxt);
        editPars = findViewById(R.id.editParsing);
        btn = findViewById(R.id.btnPars);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioURL:
                        String sURL = editTxt.getText().toString();
                        openURL(sURL);
                        break;
                    case R.id.radioGeo:
                        // 52.2751805, 104.2804326
                        String sGEO = editTxt.getText().toString();
                        openGEO(sGEO);
                        break;
                    case R.id.radioNumber:
                        String sNUM = editTxt.getText().toString();
                        makeCall(sNUM);
                        break;

                    default:
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String s = editPars.getText().toString();

                                       Pattern patternURL = Pattern.compile(
                                               "((ht|f)tp(s?):\\/\\/|www\\.)"
                                                       + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                                                       + "[\\p{Alnum}.%_=?&#\\-+()\\[\\]\\*$~@!:/{}']*)",
                                               Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                                       Matcher urlMatcher = patternURL.matcher(s);

                                       Pattern patternGEO = Pattern.compile("^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$");
                                       Matcher geoMatcher = patternGEO.matcher(s);

                                       Pattern patternNUM = Pattern.compile("^\\s?((\\+[1-9]{1,4}[ \\-]*)|(\\([0-9]{2,3}\\)[ \\-]*)|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?\\s?");
                                       Matcher numMatcher = patternNUM.matcher(s);

                                       if (urlMatcher.matches()) openURL(s);
                                       else if (geoMatcher.matches()) openGEO(s);
                                       else if (numMatcher.matches()) makeCall(s);
                                       else {
                                           Log.d("Intent", "Не получается обработать намерение!");
                                           Toast.makeText(getApplicationContext(), "Не получается обработать намерение!", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }
        );

    }

    public void openURL(String s) {
        Uri address = Uri.parse(s);
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openLinkIntent);
        } else {
            Log.d("Intent", "Не получается обработать намерение!");
            Toast.makeText(getApplicationContext(), "Не получается обработать намерение!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGEO(String s) {
        String geoUriString = "geo:" + s + "?z=1";
        Uri geoUri = Uri.parse(geoUriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Log.d("Intent", "Не получается обработать намерение!");
            Toast.makeText(getApplicationContext(), "Не получается обработать намерение!", Toast.LENGTH_SHORT).show();
        }
    }

    public void makeCall(String s) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s));
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Log.d("Intent", "Не получается обработать намерение!");
            Toast.makeText(getApplicationContext(), "Не получается обработать намерение!", Toast.LENGTH_SHORT).show();
        }
    }
}