package com.suhun.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.HashMap;
import java.util.LinkedList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private String tag = MainActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;
    private TextView scanResultText;
    private Button sendBtn, addBtn;
    private ListView scanResultListView;
    private SimpleAdapter simpleAdapter;
    private LinkedList<HashMap<String, String>> data = new LinkedList<>();
    private String[] from = {"itemKey"};
    private int[] to = {R.id.list_item};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED){
            initZXingQRCodeView();

        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 123);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                initZXingQRCodeView();

            }else{
                finish();
            }
        }
    }

    private void initZXingQRCodeView(){
        mScannerView = findViewById(R.id.zxingView);
    }

    private void initView(){
        sendBtn = findViewById(R.id.send);
        sendBtn.setEnabled(false);
        addBtn = findViewById(R.id.add);
        addBtn.setEnabled(false);
        scanResultText = findViewById(R.id.scanResult);
        scanResultListView = findViewById(R.id.scanList);
        initListviewDataStructure();
    }
    private void initListviewDataStructure(){
        simpleAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        scanResultListView.setAdapter(simpleAdapter);
        //test list view
//        HashMap<String, String> testData = new HashMap<>();
//        testData.put(from[0], "test" );
//        data.add(testData);
//        simpleAdapter.notifyDataSetChanged();
    }

    public void addFun(View view){
        String scanResult = scanResultText.getText().toString();
        HashMap<String, String> result = new HashMap<>();
        result.put(from[0], scanResult);
        data.add(result);
        simpleAdapter.notifyDataSetChanged();
        mScannerView.resumeCameraPreview(this);
        addBtn.setEnabled(false);
    }
    public void sendFun(View view){
        String scanResult[] = scanResultText.getText().toString().split(":");
        Uri uri = Uri.parse("smsto:"+scanResult[1]);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", scanResult[2]);
        startActivity(intent);
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(tag, rawResult.getText()); // Prints scan results
        Log.v(tag, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        scanResultText.setText(rawResult.getText());
        addBtn.setEnabled(true);
        sendBtn.setEnabled(true);
        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);

    }
}