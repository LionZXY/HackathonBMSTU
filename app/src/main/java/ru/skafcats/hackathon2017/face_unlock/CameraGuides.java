package ru.skafcats.hackathon2017.face_unlock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Oleg Morozenkov on 31.03.17.
 */

public class CameraGuides extends View {
    public CameraGuides(Context context) {
        super(context);
    }

    public CameraGuides(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), paint);
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, paint);
    }
}
