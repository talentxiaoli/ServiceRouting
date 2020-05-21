package com.example.webclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerClient {

    private static final String TAG = "ServerClient";

    public ServerClient() {
        Log.i(TAG, "ServerClient");
    }

    public void getHashListFromDhcpServerExt(String data) {
        Socket socket = null;
        BufferedReader in = null;
        try {
            socket = new Socket("fd15:4ba5:5a2b:1008:bde7:5050:c0bc:42c1", 6666);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String resData = in.readLine();
                Log.i(TAG, "resData = " + resData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAddressFromUrl(String url) {
        return "";
    }
}
