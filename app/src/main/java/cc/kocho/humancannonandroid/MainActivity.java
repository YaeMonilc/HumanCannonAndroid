package cc.kocho.humancannonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

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
    int totalI = 0;

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
        total.setText(Html.fromHtml(String.format(getString(R.string.total),Config.COLOR_NORMAL,totalI)));
    }
    private void listening(){
        submit.setOnClickListener(view -> {
            for (int i = 0;i < Config.number;i++){
                new Thread(() -> {
                    Cannonball cannonball = Cannon.fire("123");
                    String color = Config.COLOR_NO;
                    boolean stats = false;
                    if (cannonball.result.equals("OK")){
                        color = Config.COLOR_OK;
                        stats = true;
                    }
                    cannonball.name = cannonball.name.replace(cannonball.Q,String.format("<font color=\"%s\">%s</font>",Config.COLOR_Q,cannonball.Q));
                    setLog(String.format(getString(R.string.message),cannonball.name,color,cannonball.result),stats);
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
    private Handler setLogHandler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            log += String.format("%s<br>", msg.obj);
            out.setText(Html.fromHtml(log));
            switch (msg.what){
                case 0:
                    successI++;
                    success.setText(Html.fromHtml(String.format(getString(R.string.success),Config.COLOR_OK,successI)));
                    break;
                case 1:
                    failedI++;
                    failed.setText(Html.fromHtml(String.format(getString(R.string.failed),Config.COLOR_OK,failedI)));
                    break;
            }
        }
    };

}