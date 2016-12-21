package ru.ifmo.android_2016.runcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.ifmo.android_2016.runcity.loader.LoginParser;


public class RegistrationActivity extends AppCompatActivity {
    private EditText loginField;
    private EditText passwordField;
    private Button Register;

    Pair<String, String> user = null;

    private final String URI = "http://www.pastegraph.esy.es/user.json";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginField = (EditText) findViewById(R.id.loginField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        Register = (Button) findViewById(R.id.buttonRegistration);

        final File file = new File(getFilesDir(), "user.json");
        if (!file.exists()) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            InputStream in = null;
                            FileOutputStream out = null;
                            try {
                                in = new BufferedInputStream((new URL(URI)).openStream());
                                out = new FileOutputStream(file);
                                int counter;
                                byte[] buffer = new byte[1024];
                                while ((counter = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, counter);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (in != null) {
                                        in.close();
                                    }
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            ).start();
        }




        try {
            user = LoginParser.parseLogin(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginField.getText().toString();
                String password = passwordField.getText().toString();

                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    if (user.first.equals(login)&& user.second.equals(password)) {
                        startActivity(new Intent(getApplicationContext(), Competitions.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Неправильный логин/пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

}
