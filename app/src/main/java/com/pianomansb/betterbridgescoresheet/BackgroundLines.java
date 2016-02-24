package com.pianomansb.betterbridgescoresheet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import java.util.Vector;

/**
 * Created by pianomansb on 1/30/16.
 */
public class BackgroundLines extends View {
    private ShapeDrawable vertLine, horLine;
    private Vector<ShapeDrawable> gameLines = new Vector<>();
    private Vector<Integer> gameLinePositions = new Vector<>();

    public BackgroundLines(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        vertLine = new ShapeDrawable(new RectShape());
        horLine = new ShapeDrawable(new RectShape());
    }

    /*public void addGameLine(int position) {
        ShapeDrawable line = new ShapeDrawable(new RectShape());
        gameLines.add(line);
        gameLinePositions.add(position);
        invalidate();
    }*/

    @Override
    public void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        vertLine.setBounds((width/2)-2, 0, (width/2)+2, height);
        horLine.setBounds(0, (height/2) - 2, width, (height/2) + 2);
        vertLine.draw(canvas);
        horLine.draw(canvas);
        /*for( int i=0; i < gameLines.size(); ++i ){
            ShapeDrawable gameLine = gameLines.get(i);
            int position = gameLinePositions.get(i);
            gameLine.setBounds(0, position-2, width, position+2);
            gameLine.draw(canvas);
        }*/
    }
}
