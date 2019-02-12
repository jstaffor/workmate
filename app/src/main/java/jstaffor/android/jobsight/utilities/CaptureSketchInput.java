package jstaffor.android.jobsight.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class CaptureSketchInput extends View
{
    private static final String TAG = "CaptureSketchInput";
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paintPaint;
    private float axisX;
    private float axisY;
    private float impactOfTouch = 4;
    private float densityOfInput = 2;

    public CaptureSketchInput(Context context, AttributeSet attr, Intent intent) {
        super(context, attr);

        if(intent == null)
            throw new IllegalArgumentException("CaptureSketchInput.CaptureSketchInput(Context context, AttributeSet attr, Intent intent)  - intent cannot be null");

        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        paintPaint = new Paint();
        paintPaint.setAntiAlias(true);
        paintPaint.setDither(true);
        paintPaint.setColor(Color.argb(255, 0, 0, 0));
        paintPaint.setStyle(Paint.Style.STROKE);
        paintPaint.setStrokeJoin(Paint.Join.ROUND);
        paintPaint.setStrokeCap(Paint.Cap.ROUND);
        paintPaint.setStrokeWidth(densityOfInput);

        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, (h > 0 ? h : ((View) this.getParent()).getHeight()), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(canvas == null)
            throw new IllegalArgumentException("CaptureSketchInput.onDraw(Canvas canvas) - canvas cannot be null");

        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paintPaint);
    }

    private void TouchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        axisX = x;
        axisY = y;
    }

    private void TouchMove(float x, float y) {
        float dx = Math.abs(x - axisX);
        float dy = Math.abs(y - axisY);

        if (dx >= impactOfTouch || dy >= impactOfTouch) {
            path.quadTo(axisX, axisY, (x + axisX) / 2, (y + axisY) / 2);
            axisX = x;
            axisY = y;
        }
    }

    private void TouchUp() {
        if (!path.isEmpty()) {
            path.lineTo(axisX, axisY);
            canvas.drawPath(path, paintPaint);
        } else {
            canvas.drawPoint(axisX, axisY, paintPaint);
        }

        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        if(canvas == null)
            throw new IllegalArgumentException("CaptureSketchInput.onTouchEvent(MotionEvent motionEvent) - motionEvent cannot be null");

        super.onTouchEvent(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TouchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                TouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TouchUp();
                invalidate();
                break;
        }

        return true;
    }

    public void clearCanvasAndBitmap() {
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public byte[] getBytes() {
        Bitmap b = getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap getBitmap()
    {
        View curView = (View) this.getParent();
        Bitmap curBitmap = Bitmap.createBitmap(curView.getWidth(), curView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas curCanvas = new Canvas(curBitmap);
        curView.layout(curView.getLeft(), curView.getTop(), curView.getRight(), curView.getBottom());
        curView.draw(curCanvas);

        return curBitmap;
    }
}