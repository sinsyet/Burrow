package com.example.ygx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.burrowlib.Burrow;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Burrow.init(getApplicationContext());
    }
}
