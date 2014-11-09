package com.brainwaves.cybergods.nbanking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Screen2 extends Activity {

    public static boolean flag = false;
    public final static String EXTRA_MESSAGE1 = "cybergods.brainwaves.com.MESSAGE";
    String[] message3 = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message3 = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        setContentView(R.layout.activity_screen2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen2, menu);
        return true;
    }

    public void test(View view) {
        // Do something in response to button

        Spinner s = (Spinner) findViewById(R.id.spinner);
        //Toast.makeText(this,"test"+s.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
        if(s.getSelectedItem().toString().equals("Withdrawal"))
         {
             flag = true;
            // handler for withdrawal
             //Toast.makeText(this,"test"+message3[0].toString(),Toast.LENGTH_LONG).show();
             Intent intent = new Intent(this, Screen3.class);
             intent.putExtra(EXTRA_MESSAGE1,message3);
             startActivity(intent);

         }
        else
        {
            flag = false;
            //handler for anything but withdrawal
            //Send NDEF message containing Code, A/c num, IMEI num
            Intent intent = new Intent(this, Beam.class);
            intent.putExtra(EXTRA_MESSAGE1,message3);
            startActivity(intent);
        }

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
