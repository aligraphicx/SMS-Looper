package com.example.smsiteration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SmsManager smgr;
    private EditText phoneNumber;
    private EditText textMesg;
    private EditText iterations;
    private TextView errorText;
    private Button sendBtn;
    private SearchView searchView;

    private List<Contact> contactList;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},0);
        }else if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},0);
        }

        contactList=getContactList();

        initObjects();
        smgr= SmsManager.getDefault();

        //getContactList();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num=phoneNumber.getText().toString();
                String mesg=textMesg.getText().toString();
                int ite=0;

                if(num.isEmpty()){
                    errorText.setText("number is empty");
                }else if(mesg.isEmpty()){
                    errorText.setText("message is empty");
                }else if(iterations.getText().toString().isEmpty()){
                    errorText.setText("Iteration number is incorrect");

                }else{
                    ite=Integer.valueOf(iterations.getText().toString());

                    for(int i=0;i<ite;i++){
                        if(smsSender(num,mesg)){
                            errorText.setText("SMS Send Successfully");
                        }
                    }
                }

            }
        });


    }

    private List<Contact> getContactList() {
        ContentResolver cr = getContentResolver();
        List<Contact> contacts=new ArrayList<>();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {

                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Contact contact=new Contact(name,phoneNo);
                        contacts.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return contacts;
    }


    public boolean smsSender(String num,String msg){

        try {
            smgr.sendTextMessage(num,null,msg,null,null);
            Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    public void initObjects(){

        phoneNumber=findViewById(R.id.number);
        textMesg=findViewById(R.id.textArea);
        iterations=findViewById(R.id.iterations);
        sendBtn=findViewById(R.id.sendBtn);
        errorText=findViewById(R.id.errorText);
        searchView=findViewById(R.id.contactSearch);
    }
}
