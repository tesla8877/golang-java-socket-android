package com.redlion.go_java_socket_communication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.StringPrepParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.redlion.go_java_socket_communication.databinding.ActivityMainBinding;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import echoserver.Echoserver;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'go_java_socket_communication' library on application startup.
//    static {
//        System.loadLibrary("echoserver");
//    }

    private ActivityMainBinding binding;

    //message send button
    private ImageButton btn_msg_send;

    //message input box
    private EditText box_msg_input;

    //holds message to send
    private String tempString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread server_start = new Thread() {
            @Override
            public void run() {
                //starts server
                Echoserver.echoSocketServer();
            }
        };

        server_start.start();

        setupUI();
    }

    private int setupUI() {

        btn_msg_send = findViewById(R.id.btn_msg_send);
        box_msg_input = findViewById(R.id.box_msg_input);

        btn_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = box_msg_input.getText().toString();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket("127.0.0.1", 8080);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            DataInputStream dis = new DataInputStream(socket.getInputStream());
                            dos.writeUTF(text);
                            dos.flush();
                            tempString = dis.readUTF().toString();
                            alertStringOnUIThread(tempString); // Call the modified method
                            dos.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (StringPrepParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                thread.start();
                emptyMsgSendBox();
            }
        });
        return 1;
    }



    public String alertStringOnUIThread(String alertStr) throws StringPrepParseException {
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Response from server socket")
                        .setMessage(alertStr)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return alertStr;
    }




    //initializes text input box
    public int emptyMsgSendBox() {
        box_msg_input.setText("");
        return 1;
    }

}

