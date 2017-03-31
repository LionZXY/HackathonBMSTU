package ru.skafcats.hackathon2017.face_unlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;

/**
 * Created by Oleg Morozenkov on 31.03.17.
 */

public class FaceToByteTransformer {
    public SparseArray<Face> transform(Bitmap image, Context context) {
        ArrayList<Integer> result = new ArrayList<>();

        Frame frame = new Frame.Builder()
                .setBitmap(image)
                .build();

        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        SparseArray<Face> faces = faceDetector.detect(frame);
//        for (int i = 0; i < faces.size(); ++i) {
//            Face face = faces.valueAt(i);
//            for (Landmark landmark : face.getLandmarks()) {
//                int cx = (int) (landmark.getPosition().x * 1);
//                int cy = (int) (landmark.getPosition().y * 1);
//            }
//        }
//
//        return result;
        return faces;
    }
}
