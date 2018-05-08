package smsme.bissani.smsme.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import smsme.bissani.smsme.R;

public class ValidatorActivity extends AppCompatActivity {

    private EditText editTextCode;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationCode;
    private Button verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        auth = FirebaseAuth.getInstance();
        editTextCode = (EditText) findViewById(R.id.codeID);
        verifyCode = (Button) findViewById(R.id.validatorID);
        String phoneNumber;

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
            phoneNumber = (String) savedInstanceState.getSerializable("phoneNumber");
        }
        else {
            Bundle extras = getIntent().getExtras();
            phoneNumber = extras.getString("phoneNumber");
        }

        System.out.println(phoneNumber);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
            }
        };

        verifyPhone(phoneNumber, mCallbacks);
        Log.i("PhoneVerified", "Phone: " + phoneNumber);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputCode = editTextCode.getText().toString();
                verifyPhoneNumberWithCode(verificationCode, inputCode);
            }
        });
    }

    private void verifyPhone(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                ValidatorActivity.this,
                mCallbacks
        );
    }

    private void verifyPhoneNumberWithCode(String verificationCode, String inputCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, inputCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Success
                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();;
                        }
                        else {
                            if(task.getException() instanceof FirebaseApiNotAvailableException) {
                                Toast.makeText(getApplicationContext(), "Código incorreto!", Toast.LENGTH_LONG).show();;
                            }
                        }
                    }
                });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = editTextCode.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            editTextCode.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
}

