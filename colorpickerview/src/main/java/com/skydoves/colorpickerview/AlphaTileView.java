/*
 * Designed and developed by 2017 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.colorpickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.skydoves.colorpickerview.sliders.AlphaTileDrawable;

/**
 * AlphaTileView visualizes ARGB color on the canvas using {@link AlphaTileDrawable}.
 */
@SuppressWarnings("unused")
public class AlphaTileView extends View {

    private Paint colorPaint;
    private Bitmap backgroundBitmap;
    private final AlphaTileDrawable.Builder builder = new AlphaTileDrawable.Builder();

    protected Paint borderPaint;
    protected int borderSize = 2;
    protected int borderColor = Color.BLACK;
    protected boolean drawBorder = false;

    public AlphaTileView(Context context) {
        super(context);
        onCreate();
    }

    public AlphaTileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreate();
        getAttrs(attrs);
    }

    public AlphaTileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
        getAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaTileView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
        getAttrs(attrs);
    }

    private void onCreate() {
        this.colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setBackgroundColor(Color.WHITE);

        this.borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(borderSize);
        this.borderPaint.setColor(borderColor);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AlphaTileView);
        try {
            if (a.hasValue(R.styleable.AlphaTileView_tileSize)) {
                builder.setTileSize(a.getInt(R.styleable.AlphaTileView_tileSize, builder.getTileSize()));
            }
            if (a.hasValue(R.styleable.AlphaTileView_tileOddColor)) {
                builder.setTileOddColor(
                        a.getInt(R.styleable.AlphaTileView_tileOddColor, builder.getTileOddColor()));
            }
            if (a.hasValue(R.styleable.AlphaTileView_tileEvenColor)) {
                builder.setTileEvenColor(
                        a.getInt(R.styleable.AlphaTileView_tileEvenColor, builder.getTileEvenColor()));
            }

            if (a.hasValue(R.styleable.AlphaTileView_borderColor)) {
                borderColor = a.getColor(R.styleable.AlphaTileView_borderColor, borderColor);
                this.borderPaint.setColor(borderColor);
            }
            if (a.hasValue(R.styleable.AlphaTileView_borderSize)) {
                borderSize = a.getDimensionPixelOffset(R.styleable.AlphaTileView_borderSize, borderSize);
                this.borderPaint.setStrokeWidth(borderSize);
            }

            drawBorder = a.getBoolean(R.styleable.AlphaTileView_drawBorder, drawBorder);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        AlphaTileDrawable drawable = builder.build();
        backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (backgroundBitmap != null && !backgroundBitmap.isRecycled()) {
            Canvas backgroundCanvas = new Canvas(backgroundBitmap);
            drawable.setBounds(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
            drawable.draw(backgroundCanvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), colorPaint);

        if (drawBorder) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();
            float strokeWidth = borderPaint.getStrokeWidth();
            canvas.drawRect(strokeWidth / 2, strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth / 2, borderPaint);
        }
    }

    public void setPaintColor(int color) {
        colorPaint.setColor(color);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        setPaintColor(color);
    }
}
