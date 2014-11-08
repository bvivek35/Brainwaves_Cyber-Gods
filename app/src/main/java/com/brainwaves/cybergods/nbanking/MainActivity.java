package com.brainwaves.cybergods.nbanking;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {


    public final static String EXTRA_MESSAGE = "cybergods.brainwaves.com.MESSAGE";

    Activity ma;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);
        ma=this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler for submit button
                String message[] = new String[5];
                Intent intent = new Intent(ma, Screen2.class);
                EditText editText = (EditText) findViewById(R.id.editText);
                String message1 = editText.getText().toString();
                EditText editText1 = (EditText) findViewById(R.id.editText3);
                String message2 = editText1.getText().toString();
                message[1] = message1;
                message[3] = message2;
                message[4] = "foo";
                intent.putExtra(EXTRA_MESSAGE,message);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
