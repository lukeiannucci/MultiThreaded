package com.example.luke.multi_threaded;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private Handler progressBarHandler = new Handler();
    private int itemNum = 0;
    private String message[] = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void progress(View v) {
        progress = new ProgressDialog(this);
        progress.setMessage("Test");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }

    public void onButtonClickCreate(View v) {
        if (v.getId() == R.id.button1) {
            progress = new ProgressDialog(v.getContext());
            progress.setCancelable(true);
            progress.setMessage("Creating File...");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setProgress(0);
            progress.setMax(10);
            progress.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        String filename = "numbers.txt";
                        String string = new String();
                        while (itemNum < 10) {
                            string += Integer.toString(itemNum+1) + "\n";
                            itemNum++;
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setProgress(itemNum);
                                }
                            });
                        }
                        if (itemNum >= 9) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            itemNum = 0;
                            progress.dismiss();
                        }
                        FileOutputStream outputStream;
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(string.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    class runnable implements Runnable {
        public void run() {
            try {

                String sMessage;
                FileInputStream inputStream;
                inputStream = openFileInput("numbers.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();

                while ((sMessage = bufferedReader.readLine()) != null) {
                    message[itemNum] = sMessage;
                    itemNum++;
                    try {
                        Thread.sleep(250);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(itemNum);
                        }
                    });
                }
                if(itemNum >= 9){
                    try{
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    itemNum = 0;
                    progress.dismiss();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onButtonClickLoad(View v) {
        if (v.getId() == R.id.button2) {
            progress = new ProgressDialog(v.getContext());
            progress.setCancelable(true);
            progress.setMessage("File Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setProgress(0);
            progress.setMax(10);
            progress.show();
            Runnable r = new runnable();
            Thread thread = new Thread(r);
            thread.start();

            try{
                thread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
            ListView lv = (ListView) findViewById(R.id.listView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, message);
            lv.setAdapter(adapter);
            lv.setTextFilterEnabled(true);
        }
    }


    public void onButtonClickClear(View v) {
        if(v.getId() == R.id.button3) {
            try{
                String message[] = new String[1];
                message[0] = "";
                ListView lv = (ListView)findViewById(R.id.listView);;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, message);
                lv.setAdapter(adapter);
                lv.setTextFilterEnabled(false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}