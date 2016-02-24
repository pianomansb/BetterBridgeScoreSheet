package com.pianomansb.betterbridgescoresheet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pianomansb on 2/11/16.
 */
public class GameLine extends TextView {
    /**
     * state denotes left-facing arrow
     */
    public static final int WE_ARROW =0;
    /**
     * state denotes right-facing arrow
     */
    public static final int THEY_ARROW =1;
    /**
     * state denotes simple line (back end of arrow)
     */
    public static final int GAME_LINE =2;

    private int state;
    private ShapeDrawable horLine = new ShapeDrawable(new RectShape());
    private ShapeDrawable vertLine = new ShapeDrawable(new RectShape());

    /**
     *
     * @param context passed to superclass
     * @param state Denotes the type of graphic to show.
     *              Choose GameLine.WE_ARROW for a left-facing arrow used in the
     *              'we' column. Choose GameLine.THEY_ARROW for a right-facing
     *              arrow used in the 'they' column. Choose GameLine.GAME_LINE
     *              for a simple line used as the back side of the arrow in the
     *              opposite column of the declarer
     */
    public GameLine(Context context, int state) {
        super(context);
        this.state = state;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        params.gravity = Gravity.TOP;
        setLayoutParams(params);
        setText("Hi there");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        View parent = (View) getParent();
        int width = parent.getWidth();
        int left = parent.getLeft();
        int top = this.getTop();
        horLine.setBounds(left, top, left + width, top+2);
        horLine.draw(canvas);
        super.onDraw(canvas);
        /*vertLine.setBounds(left, top, left+2, top+10);
        vertLine.draw(canvas);*/
    }
}
