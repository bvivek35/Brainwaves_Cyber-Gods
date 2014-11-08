package com.brainwaves.cybergods.nbanking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Screen3 extends Activity {


    String[] message5 = null;
    public final static String EXTRA_MESSAGE2 = "cybergods.brainwaves.com.MESSAGE";
    EditText editText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);
        Intent intent = getIntent();
        editText = (EditText) findViewById(R.id.editText);
        message5 = intent.getStringArrayExtra(Screen2.EXTRA_MESSAGE1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen3, menu);
        return true;
    }


    public void sub(View view)
    {
        String amount = editText.getText().toString();
        message5[4] = amount;
        //handler for submit button
        //Send NDEF message containing Code, A/c num, IMEI num, amount
        Button b = (Button) findViewById(R.id.button3);
        //Start inent for BeamTest activity
        Intent intent = new Intent(this, Beam.class);
        intent.putExtra(EXTRA_MESSAGE2,message5);
        startActivity(intent);

    }
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
