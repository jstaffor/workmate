package jstaffor.android.jobsight.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AudioVisualizerView extends View
{
    private static final String TAG = "AudioVisualizerView";

    private static final int MAX_AMPLITUDE = 32767;
    private int insertIdx = 0;
    private Paint pointPaint;
    private Paint linePaint;
    private int width;
    private int height;
    private float[] point;
    private float[] line;

    public AudioVisualizerView(Context context, AttributeSet attributeSet)
    {
        //Ohhhhhhhhhhhhh the Purple and Golddddddddddddddddddddddddddddddddddddddddddddddddddddd
        //Tis me heart and me soulllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
        super(context, attributeSet);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);   //Gold
        linePaint.setStrokeWidth(1);
        pointPaint = new Paint();
        pointPaint.setColor(Color.WHITE);   //Gold
        pointPaint.setStrokeWidth(1);
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight)
    {
        this.width = newWidth;
        height = newHeight;
        point = new float[this.width * 2];
        line = new float[this.width * 4];
    }

    public void addAmplitude(int amplitude)
    {
        super.invalidate();

        final float scaledHeight = ((float) amplitude / MAX_AMPLITUDE) * (height - 1);

        int ampIdx = insertIdx * 2;
        point[ampIdx++] = insertIdx;
        point[ampIdx] = scaledHeight;

        int vectorIdx = insertIdx * 4;
        line[vectorIdx++] = insertIdx;
        line[vectorIdx++] = 0;
        line[vectorIdx++] = insertIdx;
        line[vectorIdx] = scaledHeight;

        insertIdx = ++insertIdx >= width ? 0 : insertIdx;
    }

    public void onDraw(Canvas canvas)
    {
        if(canvas == null)
            throw new IllegalArgumentException("AudioVisualizerView.onDraw(Canvas canvas) - canvas cannot be null");

        canvas.drawLines(line, linePaint);
        canvas.drawPoints(point, pointPaint);
    }
}