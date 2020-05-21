package com.example.webclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1.用户上线从DhcpServer获取SR前缀，Hash算法，SR碰撞服务器地址
                DhcpClient dchpClient = new DhcpClient();
                String data = dchpClient.requestDhcpServer();
                // 2.监听DhcpServerExt，获取hash冲突列表
                ServerClient serverClient = new ServerClient();
                serverClient.getHashListFromDhcpServerExt(data);
            }
        }).start();

    }
}
