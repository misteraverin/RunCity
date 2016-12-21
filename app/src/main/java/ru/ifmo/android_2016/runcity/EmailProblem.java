package ru.ifmo.android_2016.runcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by -- on 12.12.2016.
 */

public class EmailProblem extends AppCompatActivity {

    EditText textSubject;
    EditText textMessage;
    Button buttonSend;

    private final String address = "ivart07@mail.ru"; // Here should be an address of organizer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_problem);

        textSubject = (EditText) findViewById(R.id.email_subject_text);
        textMessage = (EditText) findViewById(R.id.email_message_text);
        buttonSend = (Button) findViewById(R.id.email_sent);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                email.putExtra(Intent.EXTRA_SUBJECT, textSubject.getText().toString());
                email.putExtra(Intent.EXTRA_TEXT, textMessage.getText().toString());
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Отправить с помощью"));
            }
        });

    }
}
