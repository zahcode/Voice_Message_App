package com.voiceapp.calling.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.voiceapp.calling.R;
import com.voiceapp.calling.databinding.ActivitySettingsBinding;
import com.voiceapp.calling.helper.SharedValues;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // to set actionBar title
        this.setTitle("الإعدادات");
        // to add back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // to set values
        setValues();

        // to save values
        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValues();
            }
        });
    }

    // to set values
    @SuppressLint("SetTextI18n")
    private void setValues(){
        binding.etPhoneLength.setText(new SharedValues(getApplicationContext()).getPhoneLength()+"");
        binding.etCountryCode.setText(new SharedValues(getApplicationContext()).getCountryCode());
        binding.etMessageText.setText(new SharedValues(getApplicationContext()).getMessageText());
        binding.etAccessKey.setText(new SharedValues(getApplicationContext()).getAccessKey());
        binding.etLanguage.setText(new SharedValues(getApplicationContext()).getLanguage());
        binding.etVoiceType.setText(new SharedValues(getApplicationContext()).getVoiceType());
    }

    // to save values
    private void saveValues(){

        if (Objects.requireNonNull(binding.etPhoneLength.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال حد الرقم", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etCountryCode.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال كود الدولة", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etMessageText.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال نص الرسالة", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etMessageText.getText()).toString().length() > 500){
            Toast.makeText(getApplicationContext(), "الرسالة أكبر من 500 حرف", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etAccessKey.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال مفتاح الوصول", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etLanguage.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال لغة الرسالة", Toast.LENGTH_SHORT).show();
        }

        else if (Objects.requireNonNull(binding.etVoiceType.getText()).toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "الرجاء إدخال نوع المتحدث", Toast.LENGTH_SHORT).show();
        }

        // here will save data
        else {
            new SharedValues(getApplicationContext()).setPhoneLength(Integer.parseInt(binding.etPhoneLength.getText().toString()));
            new SharedValues(getApplicationContext()).setCountryCode(binding.etCountryCode.getText().toString());
            new SharedValues(getApplicationContext()).setMessageText(binding.etMessageText.getText().toString());
            new SharedValues(getApplicationContext()).setAccessKey(binding.etAccessKey.getText().toString());
            new SharedValues(getApplicationContext()).setLanguage(binding.etLanguage.getText().toString());
            new SharedValues(getApplicationContext()).setVoiceType(binding.etVoiceType.getText().toString());

            Toast.makeText(getApplicationContext(), "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // when click back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

    // when finish to add animation
    @Override
    public void finish() {
        super.finish();
        // animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}