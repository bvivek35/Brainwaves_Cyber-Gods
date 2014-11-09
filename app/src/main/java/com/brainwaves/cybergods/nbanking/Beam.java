package com.brainwaves.cybergods.nbanking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.telephony.TelephonyManager;


import java.nio.charset.Charset;



public class Beam extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    String[] message1 = null;
    String[] message2 = null;
    String[] message = null;
    //public String flag = "";
    private static final String MIME_TYPE = "application/com.brainwaves.cybergods.nbanking";
    private static final int MESSAGE_SENT = 1;
    private TextView textView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent = null;
    private IntentFilter[] intentFiltersArray;
    Context context;
    Button bt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        Intent intent = getIntent();
        message1 = intent.getStringArrayExtra(Screen2.EXTRA_MESSAGE1);
        message2 = intent.getStringArrayExtra(Screen3.EXTRA_MESSAGE2);
        if(message1 != null){
            message = message1;
        }
        else{
            message = message2;
        }
        /*if(message[4].equals("foo")){
            flag = "2";
        }
        else{
            flag="3";
        }*/
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        bt = (Button) findViewById(R.id.button4);
        bt.setVisibility(View.GONE);
        //TextView textView = (TextView) findViewById(R.id.textView);
        //Checking for adapter
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            mNfcAdapter.setNdefPushMessageCallback(this, this); //Calling createNdef
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        /*textView.setText(new String("Create called"));
        Log.d("Utkarsh", "Creating NDEF Message");
        String text = "Foobar";
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord(MIME_TYPE, text.getBytes())
        });
        return msg;*/
        String code = "";
        if(Screen2.flag == true)
        {
         code = "3";
        }
        else
        {
            code = "2";
        }
        message[0] = code;
        String text = ""+message[0];
        TelephonyManager tm = (TelephonyManager) getSystemService(context.TELEPHONY_SERVICE);
        message[2] = tm.getDeviceId();
        for(int i = 1;i<message.length;i++)
        {
            text+=","+message[i];
        }
        NdefRecord mimeRecord = createMimeRecord(MIME_TYPE,
                text.getBytes());
        //NdefRecord appRecord = NdefRecord.createApplicationRecord("com.brainwaves.cybergods.nbanking");
        NdefRecord[] ndefRecords = new NdefRecord[] {
                mimeRecord,
                //appRecord
        };
        NdefMessage ndefMessage = new NdefMessage(ndefRecords);
        return ndefMessage;
    }

    private NdefRecord createMimeRecord(String mime_type, byte[] payload) {
        byte[] mimeBytes = mime_type.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Message not sent",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        Log.d("Utkarsh", "Intent set");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Utkarsh", "On Resume");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
            Log.d("Utkarsh", "Processed intent");
        }
        /*NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        adapter.enableForegroundDispatch(this, pi, null, null);*/
    }

    private void processIntent(Intent intent) {
        // TODO Auto-generated method stub
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Click to read");
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String payload = new String(msg.getRecords()[0].getPayload());
        FileIO.writeTo(payload);
        bt.setVisibility(View.VISIBLE);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = FileIO.readFrom();
                String[] ss = s.split(",");
                int ch = Integer.parseInt(ss[0]);
                switch(ch)
                {
                    case -1 : handleError(ss);
                        break;
                    case 0 : handleWithdrawal(ss);
                        break;
                    case 1 : handleStatement(ss);
                }
            }
        });
    }

    private void handleError(String [] s)
    {
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(s[1]);
    }

    private void handleWithdrawal(String [] s)
    {
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Please collect your money\n" + s[1]);
    }

    private void handleStatement(String [] s)
    {
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(s[1]);
    }

    public void onNdefPushComplete(NfcEvent nfcEvent) {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
