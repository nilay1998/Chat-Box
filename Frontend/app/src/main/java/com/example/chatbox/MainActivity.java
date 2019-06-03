package com.example.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private String name="";
    public static final String NICKNAME = "usernickname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText  editText=findViewById(R.id.name);
        Button button=findViewById(R.id.enter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=editText.getText().toString().trim();
                if(!name.equals(""))
                {
                    Intent intent=new Intent(MainActivity.this,ChatBoxActivity.class);
                    intent.putExtra(NICKNAME,name);
                    startActivity(intent);
                }
            }
        });
    }
}
