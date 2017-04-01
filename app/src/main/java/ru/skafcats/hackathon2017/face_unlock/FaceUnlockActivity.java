package ru.skafcats.hackathon2017.face_unlock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;

import java.util.ArrayList;
import java.util.Arrays;

import ru.skafcats.hackathon2017.R;

public class FaceUnlockActivity extends AppCompatActivity implements Camera.PictureCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_unlock);

        final Camera camera = getCameraInstance();

        CameraPreview cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, FaceUnlockActivity.this);
                    }
                }
        );
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length);

        FaceToByteTransformer faceToByteTransformer = new FaceToByteTransformer();
        int code = faceToByteTransformer.transformToInt(photo, this);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("code", code);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
