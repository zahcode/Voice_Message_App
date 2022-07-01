package com.voiceapp.calling.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.NotFoundException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.Balance;
import com.messagebird.objects.VoiceMessage;
import com.messagebird.objects.VoiceMessageResponse;
import com.messagebird.objects.VoiceType;
import com.voiceapp.calling.R;
import com.voiceapp.calling.databinding.ActivityListMakeCallBinding;
import com.voiceapp.calling.dialog.ShowBalance;
import com.voiceapp.calling.helper.SharedValues;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListMakeCallActivity extends AppCompatActivity {

    String all_phones = "";
    List<BigInteger> phones;

    ActivityListMakeCallBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListMakeCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // to set actionBar title
        this.setTitle("عدة أرقام");
        // to add back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // array list to add number in it
        phones = new ArrayList<>();

        // to set country code to edit text
        binding.etlEnterNumber.setPrefixText(new SharedValues(getApplicationContext()).getCountryCode());
        binding.etlEnterNumber.setCounterMaxLength(new SharedValues(getApplicationContext()).getPhoneLength());

        // to save numbers into list
        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (binding.etEnterNumber.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "رجاءًا إدخال الأرقام", Toast.LENGTH_SHORT).show();
                }
                else {
                    // to split text
                    String all_numbersText = binding.etEnterNumber.getText().toString();
                    String[] array = all_numbersText.split("\n");

                    // to save it to big integer array
                    for (int i = 0; i<array.length; i++){
                        try {
                            phones.add(new BigInteger(new SharedValues(getApplicationContext()).getCountryCode()+array[i]));
                        }catch (NumberFormatException e){
                            Log.e("Main_Voice", e.getMessage());
                        }
                    }

                    binding.etEnterNumber.setText("");

                    // to save all phones in all_phones value
                    all_phones = "";
                    for (int counter = 0; counter<phones.size(); counter++){
                        all_phones = all_phones +  (counter+1) +" : "+ phones.get(counter) +"\n";
                    }
                    // to set all phones to text view
                    binding.tvNumbers.setText(all_phones);
                    // to scroll down
                    binding.allPhonesScroll.fullScroll(ScrollView.FOCUS_DOWN);
                    // to set total phones
                    binding.tvTotal.setText(phones.size()+"");
                }
            }
        });

        // to make call
        binding.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phones.size()>0){
                    //new Thread and save call
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            makeCall();
                        }
                    }).start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "الرجاء إدخال الأرقام", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // to show data
        binding.btShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadDataActivity.class);
                startActivity(intent);

                // animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void makeCall(){

        // to hide button
        binding.btCall.post(new Runnable() {
            @Override
            public void run() {
                binding.btCall.setVisibility(View.INVISIBLE);
            }
        });
        // to show progress bar
        binding.progressBar.post(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        // First create your service object
        final MessageBirdService wsr = new MessageBirdServiceImpl(new SharedValues(getApplicationContext()).getAccessKey());

        // Add the service to the client
        final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

        try {
            // Get Hlr using msgId and msisdn
            Log.i("Main_Voice", "sending message");

            // Send a voice message using the VoiceMessage object
            VoiceMessage vm = new VoiceMessage(new SharedValues(getApplicationContext()).getMessageText(), phones);
            vm.setLanguage(new SharedValues(getApplicationContext()).getLanguage());
            vm.setRepeat(1);
            // to set voice type
            String voiceType = new SharedValues(getApplicationContext()).getVoiceType();
            if (voiceType.equals("male")){
                vm.setVoice(VoiceType.male);
            }
            else {
                vm.setVoice(VoiceType.female);
            }
            // send voice
            VoiceMessageResponse response = messageBirdClient.sendVoiceMessage(vm);
            Log.i("Main_Voice", "response : "+response.toString());

            //after finish
            showAndHideViews("تم الإنتهاء");

        } catch (UnauthorizedException unauthorized) {
            if (unauthorized.getErrors() != null) {
                Log.i("Main_Voice", "error : "+ unauthorized.getErrors().toString());
                showAndHideViews(unauthorized.getErrors().toString());
            }
            unauthorized.printStackTrace();
        } catch (GeneralException generalException) {
            if (generalException.getErrors() != null) {
                Log.i("Main_Voice", "error : "+ generalException.getErrors().toString());
                showAndHideViews(generalException.getErrors().toString());
            }
            generalException.printStackTrace();
        }
    }

    // to hide and show elements
    private void showAndHideViews(String message){
        // to show button
        binding.btCall.post(new Runnable() {
            @Override
            public void run() {
                binding.btCall.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        // to hide progress bar
        binding.progressBar.post(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getBalance(){

        // First create your service object
        MessageBirdService wsr = new MessageBirdServiceImpl(new SharedValues(getApplicationContext()).getAccessKey());

        // Add the service to the client
        MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

        try {
            // Get Balance
            Log.i("Main_Voice", "Retrieving your balance:");
            Balance balance = messageBirdClient.getBalance();

            // Display balance
            Log.i("Main_Voice", "balance: " + balance.toString());

            // to show dialog
            String message = "Your Balance:_\nPayment: " + balance.getPayment()+ "\nType: " + balance.getType()+ "\nAmount: " + balance.getAmount();
            ShowBalance confirmDeletePro = new ShowBalance(message);
            confirmDeletePro.show(getSupportFragmentManager(), "dialog");

        } catch (UnauthorizedException unauthorized) {
            if (unauthorized.getErrors() != null) {
                System.out.println(unauthorized.getErrors().toString());
            }
            unauthorized.printStackTrace();
        } catch (GeneralException generalException) {
            if (generalException.getErrors() != null) {
                System.out.println(generalException.getErrors().toString());
            }
            generalException.printStackTrace();
        } catch (NotFoundException notFoundException) {
            if (notFoundException.getErrors() !=null) {
                System.out.println(notFoundException.getErrors().toString());
            }
            notFoundException.printStackTrace();
        }
    }

    // menu ----------------
    // to add option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);

                // animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;

            case R.id.menu_myBalance:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBalance();
                    }
                }).start();
                Toast.makeText(getApplicationContext(), "الرجاء الإنتظار", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_delete:
                phones.clear();
                binding.tvNumbers.setText("Numbers Here");
                Toast.makeText(getApplicationContext(), "تم التفريغ", Toast.LENGTH_SHORT).show();
        }

        return false;
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