package com.example.v3033128.bluetoothcontroller;

import android.content.Context;
import android.widget.RelativeLayout;

public class MyButton extends android.support.v7.widget.AppCompatButton {
    private byte[] sendData;
    private RelativeLayout.LayoutParams mLayoutParams;
    private BluetoothService mBluetoothService = null;
    public MyButton(Context context){
        super(context);
    }
    public MyButton(Context context,ButtonData buttonData){
        super(context);
        mLayoutParams = new RelativeLayout.LayoutParams(buttonData.getWidth(),buttonData.getHeight());
        mLayoutParams.setMargins(buttonData.getMarginX(),buttonData.getMarginY(),0,0);
        this.setLayoutParams(mLayoutParams);
        this.setAllCaps(false);
        this.setText(buttonData.getName());
        sendData = buttonData.getData().getBytes();
    }
    public MyButton(Context context,ButtonData buttonData,BluetoothService bluetoothService){
        super(context);
        mLayoutParams = new RelativeLayout.LayoutParams(buttonData.getWidth(),buttonData.getHeight());
        mLayoutParams.setMargins(buttonData.getMarginX(),buttonData.getMarginY(),0,0);
        this.setLayoutParams(mLayoutParams);
        this.setAllCaps(false);
        this.setText(buttonData.getName());
        sendData = buttonData.getData().getBytes();
        this.mBluetoothService = bluetoothService;
    }

    public byte[] getSendData() {
        return sendData;
    }
    public void clicked(){
        mBluetoothService.write(this.getSendData());
    }
}
