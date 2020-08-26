package com.twirling.sdk.sdk_test;

import androidx.appcompat.app.AppCompatActivity;
import com.twirling.sdk.capture;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    private Button testButton;
    private TextView textView;

    private capture capture = new capture();

    private int FRAMELEN = 256; // 10ms * 16000 / 1000

    private int MAX16S = 32767;
    private int MIN16S = -32768;

    private long capture_obj;

    private int MAX_SHORT = 32768;

    int wakeup_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview);
        testButton = (Button) findViewById(R.id.button);
        capture.CaptureAuthInit("a5633d85883eccab4a073e44a75bf524","123");
        capture_obj = capture.DnnoiseInit();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAudioFile();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        capture.CaptureAuthRelease();
        capture.DnnoiseRelease(capture_obj);
    }



    private void show(String str)
    {
        textView.setText(str);
    }

    private void readAudioFile()
    {
        FileInputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try{
            inputStream = new FileInputStream("/sdcard/audio/before.pcm");
            fileOutputStream = new FileOutputStream("/sdcard/audio/after.pcm");
             byte buffer[] = new byte[FRAMELEN*2];
            short inputData16s[] = new short[FRAMELEN];
            short outData16s[] = new short[FRAMELEN];
            float inputData32f[] = new float[FRAMELEN];
            int len = 0;
            float scale = (float)1 / MAX_SHORT;
            while(true)
            {
                len = inputStream.read(buffer,0,buffer.length);
                if (len <= 0)
                    break;
                inputData16s = byteArray2ShortArray(buffer);
                for(int i=0;i<FRAMELEN;i++)
                {
                    inputData32f[i] = (float)(inputData16s[i]*scale* 1.0);
                }
                capture.DnnoiseProcess(capture_obj,inputData32f);
                for(int i=0;i<FRAMELEN;i++)
                {
                    int val = (int) (inputData32f[i]*(float)MAX_SHORT);
                    if (val>(int)MAX16S){
                        outData16s[i] = (short) MAX16S;
                    }else if(val<(int)MIN16S){
                        outData16s[i] = (short) MIN16S;
                    }else{
                        outData16s[i] = (short)val;
                    }
                }
                fileOutputStream.write(shortArry2byteArray(outData16s),0,shortArry2byteArray(outData16s).length);

            }
        }catch(IOException e)
        {
            e.printStackTrace();
            show("read error");
        } finally {
            if (inputStream != null)
            {
                try{
                    inputStream.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null)
            {
                try{
                    fileOutputStream.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] shortArry2byteArray(short[] src) {
        int count = src.length;
        byte[] dest = new byte[count*2];
        for (int i = 0; i < count; i++)
        {
            dest[i * 2] = (byte) (src[i] & 0xff);
            dest[i * 2 + 1] = (byte) (src[i] >> 8 & 0xff);
        }
        return dest;
    }
    public static short[] byteArray2ShortArray(byte[] src)
    {
        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++)
        {
            dest[i] = (short) ((src[2 * i + 1] << 8) | (src[2 * i] & 0xff));
        }
        return dest;
    }
}
