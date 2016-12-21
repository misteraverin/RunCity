package ru.ifmo.android_2016.runcity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.provider.MediaStore;

import java.util.UUID;


public class DrawingCanvas extends AppCompatActivity implements View.OnClickListener {
    private TaskSignature drawView;
    //buttons
    private Button newBtn, saveBtn;
    //sizes
    private float mediumBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        //get drawing view
        drawView = (TaskSignature) findViewById(R.id.drawing);

        mediumBrush = getResources().getInteger(R.integer.medium_size);

        //new button
        newBtn = (Button) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (Button)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.new_btn){
            drawView.startNew();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Image was saved");
            drawView.setDrawingCacheEnabled(true);
            //attempt to save
            String imgSaved = MediaStore.Images.Media.insertImage(
                    getContentResolver(), drawView.getDrawingCache(),
                    UUID.randomUUID().toString()+".png", "drawing");
            //feedback

            drawView.destroyDrawingCache();
            saveDialog.show();
        }
    }

}
