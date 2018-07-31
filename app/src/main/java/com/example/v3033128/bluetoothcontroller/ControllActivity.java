package com.example.v3033128.bluetoothcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ControllActivity extends AppCompatActivity implements BtConnectionStatus {
    private float scale;

    private BluetoothService mBtService = null;

    private static final int REQUEST_ENABLE = 1;
    private static final int REQUEST_DISCOVERABLE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // リラティブレイアウトの設定
        final RelativeLayout layout = new RelativeLayout(this);

        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        // 背景色の設定
        layout.setBackgroundColor(Color.rgb(220,255,240));

        setContentView(layout);

        // dp 設定
        scale = getResources().getDisplayMetrics().density;

        mBtService = new BluetoothService(this);
        mBtService.setup();

        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.TABLE,new String[]{"_id","name","width","height","x","y","send"},null,null,null,null,null);
        while(cursor.moveToNext()) {
            ButtonData bData = new ButtonData();
            bData.setId(cursor.getInt(0));
            bData.setAllData(cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6));
            makeButton(layout,bData,mBtService);
        }
        cursor.close();


        //BluetoothService mBtService = new BluetoothService(this);
        //mBtService.setup();
        Toast.makeText(this,"create",Toast.LENGTH_LONG);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mBtService.dest();
    }

    @Override
    public void onBtNotAvailable(){

    }

    @Override
    public void onBtConnecting(){}

    @Override
    public void onBtConnected(){}
    @Override
    public void onBtDeviceNotFound(){}

    @Override
    public void onBtConnectionFailed(){}

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case REQUEST_ENABLE:
                if(resultCode != Activity.RESULT_OK){
                    Toast.makeText(this,"Bluetooth Not Enabled.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_DISCOVERABLE:
                if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this,"Must be discoverable.",Toast.LENGTH_SHORT).show();
                }else{
                    //startListening();
                }
                break;
            default:
                break;
        }
    }




    private void makeButton(RelativeLayout layout,ButtonData buttonData,BluetoothService bluetoothService){
        final MyButton newButton = new MyButton(this,buttonData,bluetoothService);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBtService.write(newButton.getSendData());
                newButton.clicked();
            }
        });
        layout.addView(newButton);
    }

}