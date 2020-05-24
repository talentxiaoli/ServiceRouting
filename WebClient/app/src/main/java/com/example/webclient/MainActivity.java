package com.example.webclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {


    private DhcpClient dhcpClient;
    private ServerClient serverClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 1.用户上线从DhcpServer获取SR前缀，Hash算法，SR碰撞服务器地址
                dhcpClient = new DhcpClient();
                String data = dhcpClient.requestDhcpServer();
                // 2.监听DhcpServerExt，获取hash冲突列表
                serverClient = new ServerClient(data);
                serverClient.getHashListFromDhcpServerExt();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("MainActivity", serverClient.getAddressFromUrl("www.srtest.com"));
    }
}
