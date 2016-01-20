package com.example.pedro.qrinteractivegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.NfcAdapter;



//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import java.lang.reflect.Array;

public class MainActivity extends Activity implements AccelerometerListener{

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static String QR_DATA;
    static String QR_FORMAT;
    private Creature currentCreature;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    //private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        getIntent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        AccelerometerManager.startAccelerometerListening(this, this);

    }

    @Override
    public void onStart() {
        super.onStart();
        //client.connect();
        AccelerometerManager.startAccelerometerListening(this, this);
    }

    @Override
    public void onStop() {
        super.onStop();
       // AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
        AccelerometerManager.stopAccelerometerListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        AccelerometerManager.startAccelerometerListening(this, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
        AccelerometerManager.stopAccelerometerListening();

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        ((TextView)findViewById(R.id.main_text)).setText("Texto recibido: ");
        for (int i=0; i<rawMsg.length; i++){
            NdefMessage msg = (NdefMessage)rawMsg[i];
            NdefRecord[] records = msg.getRecords();
            byte [] payload = new byte[records.length];
            for (int j=0; j<records.length; j++){
                payload = records[j].getPayload();
            }
            TextView text = (TextView)findViewById(R.id.main_text);

            String code=new String(payload).substring(5);
            //Toast.makeText(this, code.length()-2, Toast.LENGTH_SHORT).show();
            String trun_code = code.replaceAll("([^0-9,])", "");
            byte [] bytecode=new byte[31];
            String [] split_code=trun_code.split(",");
            for (int k =0; k<split_code.length;k++){
                try {
                   bytecode[k] = Byte.parseByte(split_code[k]);
                }catch(Exception e) {
                       Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                    }
            }

            currentCreature=Creature_Factory.generate_creature(bytecode);
            text.setText(currentCreature.toString());
        }

    }

    //product qr code mode
    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void sendText(View v) throws UnsupportedEncodingException {
        String text="";
        if(currentCreature==null){
            Toast toast = Toast.makeText(this, "No Creature available.", Toast.LENGTH_SHORT);
        }else {
            //for(int i=0;i<Creature_Factory.creature_to_byte(currentCreature).length;i++) {
             //   text += Creature_Factory.creature_to_byte(currentCreature)[i];
            //}
            //text=new String(Creature_Factory.creature_to_byte(currentCreature));
            text=Arrays.toString(Creature_Factory.creature_to_byte(currentCreature));
            ((TextView) findViewById(R.id.main_text)).setText(text+currentCreature.toString());

            if (nfcAdapter == null) {
                Toast toast = Toast.makeText(this, "No NFC available.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                NdefRecord record = createRecord(text);
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                nfcAdapter.setNdefPushMessage(message, this);
            }
        }
    }

    public static NdefRecord createRecord(String text) {
        Locale locale = Locale.ENGLISH;
        boolean encodeInUtf8 = false;
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public void onShake(float force) {
    }

    public void onAccelerationChanged(float x, float y, float z) {
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                QR_DATA = intent.getStringExtra("SCAN_RESULT");
                QR_FORMAT = intent.getStringExtra("SCAN_RESULT_FORMAT");
                TextView t = (TextView) findViewById(R.id.main_text);
                Toast.makeText(this, "toast",Toast.LENGTH_SHORT).show();
                currentCreature=Creature_Factory.generate_creature(byteParser.qr_to_byte(QR_DATA));
                t.setText(currentCreature.toString());
            }
        }
    }
}