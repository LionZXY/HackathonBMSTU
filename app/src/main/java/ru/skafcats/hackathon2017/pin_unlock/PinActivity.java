package ru.skafcats.hackathon2017.pin_unlock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.EditText;

import ru.skafcats.hackathon2017.R;

public class PinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        final EditText pinEdit = (EditText) findViewById(R.id.pin_edit);

        int[] ids = {R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9};
        AppCompatButton[] buttons = new AppCompatButton[10];
        for (int i = 0; i < 10; i++) {
            final int num = i;
            buttons[i] = (AppCompatButton) findViewById(ids[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pinEdit.append(String.valueOf(num));
                }
            });
        }

        AppCompatImageButton pinDelete = (AppCompatImageButton) findViewById(R.id.pin_delete);
        pinDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinEdit.setText(pinEdit.getText().subSequence(0, pinEdit.getText().length() - 1));
            }
        });

        AppCompatImageButton pinComplete = (AppCompatImageButton) findViewById(R.id.complete_pin);
        pinComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("code", Integer.parseInt(pinEdit.getText().toString()));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } catch (Exception e) {
                }
            }
        });
    }
}
