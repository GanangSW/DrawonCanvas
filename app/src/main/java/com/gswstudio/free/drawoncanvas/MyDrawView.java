package com.gswstudio.free.drawoncanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Ganang on 7/11/2018.
 */

public class MyDrawView extends View {

  public Bitmap bitmap;
  public Canvas canvas;
  private Path path;
  private Paint bitmapPaint;
  private Paint paint;

  public MyDrawView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);

    path = new Path();
    bitmapPaint = new Paint(Paint.DITHER_FLAG);

    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setDither(true);
    paint.setColor(0xFF000000);
    paint.setStyle(Style.STROKE);
    paint.setStrokeJoin(Join.ROUND);
    paint.setStrokeCap(Cap.ROUND);
    paint.setStrokeWidth(9);

  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
    canvas = new Canvas(bitmap);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    canvas.drawPath(path, paint);
  }

  private float mX, mY;
  private static final float TOUCH_TOLERANCE = 4;

  private void touch_start(float x, float y) {
    path.reset();
    path.moveTo(x, y);
    mX = x;
    mY = y;
  }

  private void touch_move(float x, float y) {
    float dx = Math.abs(x - mX);
    float dy = Math.abs(y - mY);
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
      path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      mX = x;
      mY = y;
    }
  }

  private void touch_up() {
    path.lineTo(mX, mY);
    canvas.drawPath(path, paint);
    path.reset();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        touch_start(x, y);
        break;
      case MotionEvent.ACTION_MOVE:
        touch_move(x, y);
        break;
      case MotionEvent.ACTION_UP:
        touch_up();
        break;
    }
    return true;
  }

  public Bitmap getBitmap() {
    this.setDrawingCacheEnabled(true);
    this.buildDrawingCache();
    Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
    this.setDrawingCacheEnabled(false);
    return bmp;
  }

  public void clear() {
    bitmap.eraseColor(Color.GREEN);
    invalidate();
    System.gc();
  }
}
