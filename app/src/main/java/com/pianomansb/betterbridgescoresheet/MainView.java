package com.pianomansb.betterbridgescoresheet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pianomansb on 2/24/16.
 */
public class MainView extends View {

    ShapeDrawable horizontalLine = new ShapeDrawable(new RectShape());
    ShapeDrawable verticalLine = new ShapeDrawable(new RectShape());

    public MainView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        horizontalLine.setBounds(0, (h/2)-2, w, (h/2)+2);
        verticalLine.setBounds((w/2)-2, 0, (w/2)+2, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        horizontalLine.draw(canvas);
        verticalLine.draw(canvas);
    }
}
