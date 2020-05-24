package com.example.webclient;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerClient {

    private static final String TAG = "ServerClient";

    private String sr_prev = null;
    private String sr_hash = null;
    private String sr_ext_ip = null;


    public ServerClient(String data) {
        Log.i(TAG, "ServerClient(String)");
        try {
            JSONObject jb = new JSONObject(data);
            sr_prev = jb.getString("sr_prev");
            sr_hash = jb.getString("sr_hash");
            sr_ext_ip = jb.getString("sr_ext_ip");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getHashListFromDhcpServerExt() {
        Socket socket = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            socket = new Socket(sr_ext_ip, 7777);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            JSONObject jb = new JSONObject();
            jb.put("type", 1);
            out.write(jb.toString());
            out.write('\n');
            out.flush();
            while(true) {
                String line_data = in.readLine();
                if (TextUtils.isEmpty(line_data)) {
                    Log.i(TAG,"get hash conflict list end");
                    break;
                }
                Log.i(TAG, "line_data = " + line_data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAddressFromUrl(String url) {
        String sid = getSHA256(url);
        return this.sr_prev + ":" + sid.substring(0,4) + ":" + sid.substring(4,8) + ":" + sid.substring(8,12) + ":" + sid.substring(12,16);
    }

    private String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
