package com.example.v3033128.bluetoothcontroller;

interface BtConnectionStatus {
    void onBtNotAvailable();
    void onBtConnecting();
    void onBtConnected();
    void onBtDeviceNotFound();
    void onBtConnectionFailed();
}
