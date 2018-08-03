package com.gswstudio.free.drawoncanvas;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  RelativeLayout relativeLayout;
  Paint paint;
  View view;
  Path path;
  Bitmap bitmap;
  Canvas canvas;
  Button clear;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    relativeLayout = findViewById(R.id.relativeLayout);
    clear = findViewById(R.id.button_clear);
    view = new SketchSheetView(MainActivity.this);
    paint = new Paint();
    path = new Path();
    relativeLayout.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    paint.setDither(true);
    paint.setColor(Color.parseColor("#000000"));
    paint.setStyle(Style.STROKE);
    paint.setStrokeJoin(Join.ROUND);
    paint.setStrokeCap(Cap.ROUND);
    paint.setStrokeWidth(2);

    clear.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        path.reset();
        recreate();
      }
    });
  }

  private class SketchSheetView extends View {

    public SketchSheetView(MainActivity activity) {
      super(activity);
      bitmap = Bitmap.createBitmap(820, 480, Config.ARGB_4444);
      canvas = new Canvas(bitmap);
      this.setBackgroundColor(Color.WHITE);
    }

    private ArrayList<DrawingClass> DrawingClassArrayList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
      DrawingClass pathWithPaint = new DrawingClass();
      canvas.drawPath(path, paint);
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        path.moveTo(event.getX(), event.getY());
        path.lineTo(event.getX(), event.getY());
      } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
        path.lineTo(event.getX(), event.getY());
        pathWithPaint.setDrawingClassPath(path);
        pathWithPaint.setDrawingClassPaint(paint);
        DrawingClassArrayList.add(pathWithPaint);
      }

      invalidate();
      return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      if (DrawingClassArrayList.size() > 0) {
        canvas.drawPath(
            DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getDrawingClassPath(),
            DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getDrawingClassPaint());
      }
    }
  }

  private class DrawingClass {

    Path DrawingClassPath;
    Paint DrawingClassPaint;

    Path getDrawingClassPath() {
      return DrawingClassPath;
    }

    void setDrawingClassPath(Path drawingClassPath) {
      DrawingClassPath = drawingClassPath;
    }

    Paint getDrawingClassPaint() {
      return DrawingClassPaint;
    }

    void setDrawingClassPaint(Paint drawingClassPaint) {
      DrawingClassPaint = drawingClassPaint;
    }
  }
}
