package com.example.v3033128.bluetoothcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothService {
    static String connectDevice = "SBDBT-001bdc0391e6";
    private static BluetoothAdapter mBtAdapter = null;
    private static BluetoothDevice mBtDevice = null;
    private BluetoothSocket mBtSocket = null;
    private Handler mHandler = new Handler();

    private BtConnectionStatus mStatus = null;
    private BluetoothConnection mConnection = null;

    public BluetoothService(BtConnectionStatus status){
        mStatus = status;
    }

    public void postConnecting(){
        mHandler.post(new Runnable(){
            @Override
            public void run(){mStatus.onBtConnecting();}
        });
    }

    public void postConnectionfailed(){
        mHandler.post(new Runnable(){
            @Override
            public void run(){mStatus.onBtConnectionFailed();}
        });
    }

    public void postConnected(){
        mHandler.post(new Runnable(){
            @Override
            public void run(){mStatus.onBtConnected();}
        });
    }

    void setup(){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter.isEnabled() == false){
            mStatus.onBtNotAvailable();
            return;
        }

        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        for(BluetoothDevice dev : devices){
            if(dev.getName().equals(connectDevice)){
                mBtDevice = dev;
                break;
            }
        }

        if(mBtDevice == null){
            mStatus.onBtDeviceNotFound();
            return;
        }

        mConnection = new BluetoothConnection();
        mConnection.start();
    }

    void stop(){
        mConnection.close();
    }

    private class BluetoothConnection extends Thread{
        private final UUID mmSppUuid
                = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public BluetoothConnection(){
            BluetoothSocket tBtSocket = null;
            try{
                tBtSocket = mBtDevice.createRfcommSocketToServiceRecord(mmSppUuid);
            }catch(IOException e){
                Log.e("BtLed","BluetoothConnection()",e);
            }
            mBtSocket = tBtSocket;
        }

        @Override
        public void run(){
            postConnecting();
            mBtAdapter.cancelDiscovery();

            try{
                mBtSocket.connect();
            }catch (IOException e){
                this.close();
                Log.e("BtLed","socket.connect error",e);
                postConnectionfailed();
                return;
            }

            postConnected();
        }

        public void close(){
            try{
                mBtSocket.close();
            }catch (IOException e){
                Log.e("BtLed","socket.close()",e);
            }
        }
    }

    void write(byte[] bytes){
        BluetoothWrite w = new BluetoothWrite(bytes);
        w.start();
    }

    private class BluetoothWrite extends Thread{
        private OutputStream mmOutputStream = null;
        private byte[] mmBuffer = null;

        public  BluetoothWrite(byte[] bytes){
            mmBuffer = bytes;
        }
        @Override
        public void run(){
            if(mmBuffer == null)return;
            try{
                mmOutputStream = mBtSocket.getOutputStream();
                mmOutputStream.write(mmBuffer);
            }catch(IOException e){
                Log.e("BtLed","outputstream.write",e);
                postConnectionfailed();
            }
        }
    }

    void dest(){
        mBtSocket = null;
    }


}
