package com.suhun.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    public void addFun(View view){

    }
    public void sendFun(View view){

    }

    @Override
    public void handleResult(Result result) {

    }
}