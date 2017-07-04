package com.maya.android.extra.zxing;

import com.google.zxing.ResultPoint;
import com.maya.android.extra.zxing.scan.camera.CameraManager;
import com.maya.android.extra.zxing.scan.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;
import java.util.HashSet;
public final class ViewfinderView extends View {
  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 355, 192, 128, 64};
  private static final long ANIMATION_DELAY = 7L;
  private static final int OPAQUE = 0xA0;
  private final Paint paint;
  private Bitmap resultBitmap;
  private final int laserColor;
  private final int maskColor;
  private final int resultColor;
  private int scannerAlpha;
  private volatile Collection<ResultPoint> possibleResultPoints;
  private volatile Collection<ResultPoint> lastPossibleResultPoints;
  private int i = 0;
	private Rect mRect;
	private GradientDrawable mDrawable;
	private Drawable lineDrawable;

  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    paint = new Paint();
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    resultColor = resources.getColor(R.color.result_view);
    laserColor = resources.getColor(R.color.viewfinder_laser);
    mRect = new Rect();
    int left = getResources().getColor(R.color.lightgreen);
	int center = getResources().getColor(R.color.green);
	int right = getResources().getColor(R.color.lightgreen);
    lineDrawable = getResources().getDrawable(R.drawable.fwex_zx_code_line);
    mDrawable = new GradientDrawable(
			GradientDrawable.Orientation.LEFT_RIGHT, new int[] { left,
					left, center, right, right });
    scannerAlpha = 0;
    possibleResultPoints = new HashSet<ResultPoint>(5);
  }

  @Override
  public void onDraw(Canvas canvas) {
    Rect frame = CameraManager.get().getFramingRect();
    if (frame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
    canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
    canvas.drawRect(0, frame.bottom, width, height, paint);

    if (resultBitmap != null) {
      paint.setAlpha(OPAQUE);
      canvas.drawBitmap(resultBitmap, null, frame, paint);
    } else {

      paint.setColor(getResources().getColor(R.color.green));
//      canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
//      canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
//      canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
//      canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
   			canvas.drawRect(frame.left, frame.top, frame.left + 35,
   					frame.top + 7, paint);
   			canvas.drawRect(frame.left, frame.top, frame.left + 7,
   					frame.top + 35, paint);
   			canvas.drawRect(frame.right - 35, frame.top, frame.right,
   					frame.top + 7, paint);
   			canvas.drawRect(frame.right - 7, frame.top, frame.right,
   					frame.top + 35, paint);
   			canvas.drawRect(frame.left, frame.bottom - 7, frame.left + 35,
   					frame.bottom, paint);
   			canvas.drawRect(frame.left, frame.bottom - 35, frame.left + 7,
   					frame.bottom, paint);
   			canvas.drawRect(frame.right - 35, frame.bottom - 7, frame.right,
   					frame.bottom, paint);
   			canvas.drawRect(frame.right - 7, frame.bottom - 35, frame.right,
   					frame.bottom, paint);
   			if(sIsShowRedLineInMiddle){
   		      // Draw a red "laser scanner" line through the middle to show decoding is active
	   		      paint.setColor(laserColor);
	   		      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
	   		      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
	   		      int middle = frame.height() / 2 + frame.top;
	   		      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
   	      }
      
      paint.setColor(getResources().getColor(R.color.green));
      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
      if ((i += 5) < frame.bottom - frame.top) {
			mRect.set(frame.left - 6, frame.top + i - 6, frame.right + 6,
					frame.top + 6 + i);
			lineDrawable.setBounds(mRect);
			lineDrawable.draw(canvas);
			invalidate();
		} else {
			i = 0;
		}
      
      
      postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
				frame.right, frame.bottom);
      
      
      
      
//      int middle = frame.height() / 2 + frame.top;
//      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
//      
//      Rect previewFrame = CameraManager.get().getFramingRectInPreview();
//      float scaleX = frame.width() / (float) previewFrame.width();
//      float scaleY = frame.height() / (float) previewFrame.height();
//
//      Collection<ResultPoint> currentPossible = possibleResultPoints;
//      Collection<ResultPoint> currentLast = lastPossibleResultPoints;
//      if (currentPossible.isEmpty()) {
//        lastPossibleResultPoints = null;
//      } else {
//        possibleResultPoints = new HashSet<ResultPoint>(5);
//        lastPossibleResultPoints = currentPossible;
//        paint.setAlpha(OPAQUE);
//        paint.setColor(resultPointColor);
//        synchronized (currentPossible) {
//          for (ResultPoint point : currentPossible) {
//            canvas.drawCircle(frame.left + (int) (point.getX() * scaleX),
//                              frame.top + (int) (point.getY() * scaleY),
//                              6.0f, paint);
//          }
//        }
//      }
//      if (currentLast != null) {
//        paint.setAlpha(OPAQUE / 2);
//        paint.setColor(resultPointColor);
//        synchronized (currentLast) {
//          for (ResultPoint point : currentLast) {
//            canvas.drawCircle(frame.left + (int) (point.getX() * scaleX),
//                              frame.top + (int) (point.getY() * scaleY),
//                              3.0f, paint);
//          }
//        }
//      }
//      postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }
  }

  public void drawViewfinder() {
    resultBitmap = null;
    invalidate();
  }

  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    synchronized (possibleResultPoints) {
      possibleResultPoints.add(point);
    }
  }
  
  private static boolean sIsShowRedLineInMiddle = true;
  /**
   * �����Ƿ���չʾ��չʾ����
   * @param isShowRedLineInMiddle
   */
  public static void setShowRedLineInMiddle(boolean showRedLineInMiddle){
	  sIsShowRedLineInMiddle = showRedLineInMiddle;
  }

}
