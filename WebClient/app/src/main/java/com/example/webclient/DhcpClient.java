package com.example.webclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DhcpClient {

    private static final String TAG = "DhcpClient";

    public DhcpClient() {
        Log.i(TAG, "DhcpClient()");
    }

    public String requestDhcpServer() {
        Socket socket = null;
        BufferedReader in = null;
        try {
            socket = new Socket("fd15:4ba5:5a2b:1008:bde7:5050:c0bc:42c1", 6666);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String resData = in.readLine();
            return resData;

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
        return null;
    }

}
