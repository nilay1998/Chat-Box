package com.example.chatbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChatBoxActivity extends AppCompatActivity {

    String name;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.69.242:3000");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        final EditText editText=findViewById(R.id.message2);
        Button button=findViewById(R.id.send);

        name=getIntent().getExtras().getString(MainActivity.NICKNAME);

        mSocket.connect();

        mSocket.emit("join",name);

        final ArrayList<Message> messageArrayList=new ArrayList<>();
        final RecyclerView recyclerView=findViewById(R.id.messageList);
        final CustomAdapter customAdapter=new CustomAdapter(messageArrayList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty())
                {
                    mSocket.emit("messagedetection",name,editText.getText().toString());
                    editText.setText("");
                }
            }
        });

        mSocket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data=(String) args[0];
                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mSocket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data=(String) args[0];
                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try
                        {
                            String nickName=data.getString("senderNickname");
                            String msg=data.getString("message");
                            Message m =new Message(nickName,msg);
                            messageArrayList.add(m);
                            customAdapter.notifyDataSetChanged();
                            //recyclerView.setAdapter(customAdapter);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("HAHAHA","OnDestroy called");
        mSocket.emit("kill",name);
        mSocket.disconnect();
    }
}
