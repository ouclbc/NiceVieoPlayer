package com.xiao.nicevideoplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.VideoView;

public class ResizableVideoView extends VideoView {

    // X and Y position will offset 3.5
    private float mOffset = 3.5f;
    // width and height will shrink 8px
    private int mShrink = 8;

	public ResizableVideoView(Context context) {
		super(context);
	}

	public ResizableVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int mVideoWidth = 0;
	private int mVideoHeight = 0;

	public void changeVideoSize(int width, int height) {
		mVideoWidth = width;
		mVideoHeight = height;

		// not sure whether it is useful or not but safe to do so
		getHolder().setFixedSize(width, height);
		
		forceLayout();
		invalidate(); // very important, so that onMeasure will be triggered
		requestLayout();
	}
	
    public void changeVideoSize(float xPosition, float yPosition, int width, int height) {
        setX(xPosition/* + mOffset*/);
        setY(yPosition/* + mOffset*/);
        changeVideoSize(width/*-mShrink*/, height/*-mShrink*/);
    }

	@Override
	public void onMeasure(int specwidth, int specheight) {
	    //super.onMeasure(specwidth,specheight);
		setMeasuredDimension(mVideoWidth, mVideoHeight);
	}

	@Override
	public void onDraw(Canvas c) {
		super.onDraw(c);
	}
}
