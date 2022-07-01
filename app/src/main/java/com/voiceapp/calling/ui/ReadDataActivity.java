package com.voiceapp.calling.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.NotFoundException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.VoiceMessageList;
import com.messagebird.objects.VoiceMessageResponse;
import com.voiceapp.calling.R;
import com.voiceapp.calling.databinding.ActivityReadDataBinding;
import com.voiceapp.calling.helper.SharedValues;

import java.util.Objects;

public class ReadDataActivity extends AppCompatActivity {

    String all_phones = "";

    ActivityReadDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // to set actionBar title
        this.setTitle("عرض المعلومات");
        // to add back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getList();
            }
        }).start();
    }

    private void getList(){

        // First create your service object
        MessageBirdService wsr = new MessageBirdServiceImpl(new SharedValues(getApplicationContext()).getAccessKey());

        // Add the service to the client
        final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

        try {
            Log.i("Main_Voice", "Retrieving message list");
            VoiceMessageList messageList = messageBirdClient.listVoiceMessages(0,0);

            Log.i("Main_Voice", "list : " + messageList.toString());

            for (int i = 0; i<messageList.getItems().size(); i++){
                all_phones = all_phones +
                        "Phone : " + messageList.getItems().get(i).getRecipients().getItems().get(0).getRecipient().toString() + "\n"+
                        "Date : " + messageList.getItems().get(i).getRecipients().getItems().get(0).getStatusDatetime().toString()+ "\n"+
                        "State : " + messageList.getItems().get(i).getRecipients().getItems().get(0).getStatus().toString()+ "\n"+
                        "-------------------"+"\n";
            }

            Log.i("Main_Voice", all_phones);
            // to set it to TextView
            binding.tvNumbers.post(new Runnable() {
                @Override
                public void run() {
                    // to hide and show views
                    binding.allNumberLayout.setVisibility(View.VISIBLE);
                    binding.laLoading.setVisibility(View.GONE);
                    // to set text
                    if (!all_phones.equals("")){
                        binding.tvNumbers.setText(all_phones);
                    }
                    else {
                        binding.tvNumbers.setText("فارغ");
                    }
                }
            });

        } catch (UnauthorizedException | GeneralException exception) {
            if (exception.getErrors() != null) {
                System.out.println(exception.getErrors().toString());
            }
            exception.printStackTrace();
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