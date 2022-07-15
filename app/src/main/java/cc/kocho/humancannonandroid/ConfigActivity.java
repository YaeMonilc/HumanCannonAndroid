package cc.kocho.humancannonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import cc.kocho.humancannonandroid.config.Config;

public class ConfigActivity extends AppCompatActivity {

    TextInputEditText severAddress;
    TextInputEditText number;
    TextInputEditText prefix;

    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        init();
        listening();
    }

    private void init(){
        severAddress = findViewById(R.id.severAddress);
        number = findViewById(R.id.number);
        prefix = findViewById(R.id.prefix);
        ok = findViewById(R.id.ok);
    }
    private void listening(){
        ok.setOnClickListener(view -> {
            String serveUrlS = String.valueOf(severAddress.getText());
            if (!serveUrlS.equals("")){
                Config.serveUrl = serveUrlS;
            }
            int numberN = Integer.parseInt(String.valueOf(number.getText()));
            if (!String.valueOf(numberN).equals("")){
                Config.number = numberN;
            }
            String prefixS = String.valueOf(prefix.getText());
            if (!prefixS.equals("")){
                Config.prefix = prefixS;
            }
            startActivity(new Intent(ConfigActivity.this,MainActivity.class));
        });
    }

}