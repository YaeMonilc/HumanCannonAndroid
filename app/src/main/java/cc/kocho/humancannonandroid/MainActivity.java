package cc.kocho.humancannonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cc.kocho.humancannonandroid.config.Config;

public class MainActivity extends AppCompatActivity {

    Button submit;
    TextInputEditText out;

    TextView success;
    TextView failed;
    TextView total;

    String log = "";
    int successI = 0;
    int failedI = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        listening();
    }

    private void init(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        submit = findViewById(R.id.submit);
        out = findViewById(R.id.out);

        success = findViewById(R.id.success);
        failed = findViewById(R.id.failed);
        total = findViewById(R.id.total);

        success.setText(Html.fromHtml(String.format(getString(R.string.success),Config.COLOR_OK, successI)));
        failed.setText(Html.fromHtml(String.format(getString(R.string.failed),Config.COLOR_NO,successI)));
        total.setText(Html.fromHtml(String.format(getString(R.string.total),Config.COLOR_NORMAL,Config.number)));
    }
    private void listening(){
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setVisibility(View.GONE);
            successI = 0;failedI = 0;Config.processingCompleted = 0;
            success.setText(Html.fromHtml(String.format(getString(R.string.success),Config.COLOR_OK, successI)));
            failed.setText(Html.fromHtml(String.format(getString(R.string.failed),Config.COLOR_NO,successI)));
            for (int i = 0;i < Config.number;i++){
                new Thread(() -> {
                        Cannonball cannonball = Cannon.fire(Config.prefix);
                        String color = Config.COLOR_NO;
                        boolean stats = false;
                        if (cannonball.result.contains("OK")){
                            color = Config.COLOR_OK;
                            stats = true;
                        }
                        cannonball.name = cannonball.name.replace(cannonball.Q,String.format("<font color=\"%s\">%s</font>",Config.COLOR_Q,cannonball.Q));
                        setLog(String.format(getString(R.string.message),(new SimpleDateFormat("HH:mm:ss.SSS")).format(new Date()),cannonball.name,color,cannonball.result),stats);
                }).start();
            }
        });
    }


    public void setLog(String log,boolean stats){
        Message message = new Message();
        message.obj = log;
        if (stats){
            message.what = 0;
        }else {
            message.what = 1;
        }
        setLogHandler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    private final Handler setLogHandler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            log += String.format("%s<br>", msg.obj);
            out.setText(Html.fromHtml(log));
            out.setMovementMethod(ScrollingMovementMethod.getInstance());
            out.setSelection(Objects.requireNonNull(out.getText()).length(), out.getText().length());
            switch (msg.what){
                case 0:
                    successI++;
                    success.setText(Html.fromHtml(String.format(getString(R.string.success),Config.COLOR_OK,successI)));
                    break;
                case 1:
                    failedI++;
                    failed.setText(Html.fromHtml(String.format(getString(R.string.failed),Config.COLOR_NO,failedI)));
                    break;
            }
            Config.processingCompleted++;
            if (Config.processingCompleted == Config.number){
                submit.setEnabled(true);
                submit.setVisibility(View.VISIBLE);
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setIcon(R.drawable.ic_baseline_done_all_24)
                        .setTitle(R.string.titleM)
                        .setMessage(R.string.successM)
                        .setPositiveButton(R.string.ok,null)
                        .show();
            }
        }
    };

}