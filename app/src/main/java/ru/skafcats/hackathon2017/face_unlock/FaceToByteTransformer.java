package ru.skafcats.hackathon2017.face_unlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Morozenkov on 31.03.17.
 */

public class FaceToByteTransformer {
    public byte[] transform(Bitmap image, Context context) {
        Frame frame = new Frame.Builder()
                .setBitmap(image)
                .build();

        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        SparseArray<Face> faces = faceDetector.detect(frame);
        if (faces.size() == 0) {
            return new byte[0];
        }
        Face face = faces.valueAt(0);

        byte[] result = new byte[10];
        int i = 0;
        for (Landmark landmark : face.getLandmarks().subList(0, 5)) {
            float x = landmark.getPosition().x / image.getWidth();
            float y = landmark.getPosition().y / image.getHeight();
            byte a = (byte) (x * 100 / 8);
            byte b = (byte) (y * 100 / 8);
            result[i] = a;
            i++;
            result[i] = b;
            i++;
        }

        return result;
    }
}
