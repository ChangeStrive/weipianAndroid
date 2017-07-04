package com.maya.android.cropimage.activity;

import java.util.concurrent.CountDownLatch;

import com.maya.android.cropimage.util.OcrImageHelper;
import com.maya.android.utils.Helper;

import eu.janmuller.android.cropimage.CropImageView;
import eu.janmuller.android.cropimage.HighlightView;
import eu.janmuller.android.cropimage.MonitoredActivity;
import eu.janmuller.android.cropimage.Util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
/**
 * 图片裁剪 Abstract Class
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-11-14
 *
 */
public abstract class AbstractCropImageActivity extends MonitoredActivity {
	private static final String TAG = AbstractCropImageActivity.class.getSimpleName();
	private CropImageView mCropImageView;
	private Bitmap mCropBitmap;
	private Handler mHandler = new Handler();
    private HighlightView mHighlightView;
    private boolean mWaitingToPick;
    private boolean mCircleCrop;
    private int mAspectX = 3;
    private int mAspectY = 2;
    private Bitmap mCroppedBitmap = null;
    Runnable mRunFaceDetection = new Runnable() {
        @SuppressWarnings("hiding")
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {

            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mCropImageView);

            int width = mCropBitmap.getWidth();
            int height = mCropBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right,
                        faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                        faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mCropImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {

            HighlightView hv = new HighlightView(mCropImageView);

            int width = mCropBitmap.getWidth();
            int height = mCropBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {

                if (mAspectX > mAspectY) {

                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {

                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mCropImageView.mHighlightViews.clear(); // Thong added for rotate

            mCropImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {

            if (mCropBitmap == null) {

                return null;
            }

            // 256 pixels wide is enough.
            if (mCropBitmap.getWidth() > 256) {

                mScale = 256.0F / mCropBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            return Bitmap.createBitmap(mCropBitmap, 0, 0, mCropBitmap.getWidth(), mCropBitmap.getHeight(), matrix, true);
        }

        public void run() {

            mImageMatrix = mCropImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
                        faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mCropBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {

                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mCropImageView.invalidate();
                    if (mCropImageView.mHighlightViews.size() == 1) {
                        mHighlightView = mCropImageView.mHighlightViews.get(0);
                        mHighlightView.setFocus(true);
                    }

//                    if (mNumFaces > 1) {
//                        Toast.makeText(CropImage.this,
//                                "Multi face crop help",
//                                Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        }
    };
    @SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.mCropImageView = setCropImageView();
//		if(Helper.isNotNull(this.mCropImageView)){
//			
//		}else{
//			Log.e(TAG, "setCropImageView() return is NULL, Please setCropImageView() again!");
//		}
	}
	/**
	 * 获取CropImageView
	 * @return
	 */
	protected CropImageView getCropImageView(){
		return this.mCropImageView;
	}
	public HighlightView getHighlightView() {
		return mHighlightView;
	}
	public void setHighlightView(HighlightView highlightView) {
		this.mHighlightView = highlightView;
	}
	public void setWaitingToPick(boolean waitingToPick){
		this.mWaitingToPick = waitingToPick;
	}
	public boolean getWaitingToPick() {
		return mWaitingToPick;
	}
	/**
	 * 初始化
	 * @param imagePath
	 */
	@SuppressLint("NewApi")
	protected void init(String imagePath){
		if(Helper.isNull(this.mCropImageView)){
			CropImageView view = setCropImageView();
			if(Helper.isNull(view)){
				Log.e(TAG, "setCropImageView() return is NULL, Please setCropImageView() again!");
				return;
			}else{
				this.mCropImageView = view;
			}
		}
		if(Helper.isNotEmpty(imagePath)){
			this.mCropBitmap = OcrImageHelper.getValidOcrBitmap(imagePath);
			if(Helper.isNotNull(this.mCropBitmap)){
				this.mCropImageView.setImageBitmapResetBase(this.mCropBitmap, true);
				Util.startBackgroundJob(this, null, "Please wait\u2026",
		                new Runnable() {
		                    public void run() {

		                        final CountDownLatch latch = new CountDownLatch(1);
		                        final Bitmap b = mCropBitmap;
		                        mHandler.post(new Runnable() {
		                            public void run() {

		                                if (b != mCropBitmap && b != null) {
		                                	mCropImageView.setImageBitmapResetBase(mCropBitmap, true);
		                                	mCropBitmap.recycle();
		                                	mCropBitmap = b;
		                                }
		                                if (mCropImageView.getScale() == 1F) {
		                                	mCropImageView.center(true, true);
		                                }
		                                latch.countDown();
		                            }
		                        });
		                        try {
		                            latch.await();
		                        } catch (InterruptedException e) {
		                            throw new RuntimeException(e);
		                        }
		                        mRunFaceDetection.run();
		                    }
		                }, mHandler);
			}
		}
	}
	/**
	 * 保存剪切图片
	 * @return
	 */
	protected String saveCropImage(){
		String result = null;
		if(Helper.isNotNull(this.mHighlightView)){
			Rect r = mHighlightView.getCropRect();

	        int width = r.width();
	        int height = r.height();

	        try {
	            this.mCroppedBitmap = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        if(Helper.isNotNull(this.mCroppedBitmap)){
		        Canvas canvas = new Canvas(this.mCroppedBitmap);
	            Rect dstRect = new Rect(0, 0, width, height);
	            canvas.drawBitmap(this.mCropBitmap, r, dstRect, null);
	        }
	        result = OcrImageHelper.saveOcrAndGetImage(this.mCroppedBitmap);
            
		}
		return result;
	}
	/**
	 * 获取剪切后图片</br>
	 * （需先调用saveCropImage()）
	 * @return
	 */
	protected Bitmap getCroppedBitmap(){
		return this.mCroppedBitmap;
	}
	
	/**
	 * 设置CropImageView
	 * @return
	 */
	protected abstract CropImageView setCropImageView();
	
}
