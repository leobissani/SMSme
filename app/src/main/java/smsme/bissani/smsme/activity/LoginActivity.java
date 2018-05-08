package smsme.bissani.smsme.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import smsme.bissani.smsme.R;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText phoneDDI;
    private EditText phone;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.insertNameID);
        phone = (EditText) findViewById(R.id.insertNumberID);
        phoneDDI = (EditText) findViewById(R.id.insertDDIphoneID);
        signIn = (Button) findViewById(R.id.signInID);

        String userName = name.getText().toString();

        SimpleMaskFormatter simpleMaskFormatterPhoneDDI = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskTextWatcherPhoneDDI = new MaskTextWatcher(phoneDDI, simpleMaskFormatterPhoneDDI);
        phoneDDI.addTextChangedListener(maskTextWatcherPhoneDDI);

        SimpleMaskFormatter simpleMaskFormatterPhone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTextWatcherPhone = new MaskTextWatcher(phone, simpleMaskFormatterPhone);
        phone.addTextChangedListener(maskTextWatcherPhone);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneDDI.getText().toString() + phone.getText().toString();

                String phoneFormat;
                phoneFormat = phoneNumber.replace(" ", "");
                phoneFormat = phoneFormat.replace("(", "");
                phoneFormat = phoneFormat.replace(")", "");
                phoneFormat = phoneFormat.replace("-", "");

                Intent intent = new Intent(LoginActivity.this, ValidatorActivity.class);
                intent.putExtra("phoneNumber", phoneFormat);
                startActivity(intent);
            }
        });
    }
}
