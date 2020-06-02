package com.waibao.qualityCertification.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewUtils {
    public static View getView(Activity activity, View view, ArrayList<String> dataList, int position) {
        // Convert the view as a TextView widget
        TextView tv = (TextView) view;

        // set the TextView text color (GridView item color)
        tv.setTextColor(Color.DKGRAY);

        // Set the layout parameters for TextView widget
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        );
        tv.setLayoutParams(lp);

        // Get the TextView LayoutParams
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();

        // Set the width of TextView widget (item of GridView)
        params.width = getPixelsFromDPs(activity, 120);

        // Set the TextView height (GridView item/row equal height)
        params.height = getPixelsFromDPs(activity, 50);

        // Set the TextView layout parameters
        tv.setLayoutParams(params);

        // Display TextView text in center position
        tv.setGravity(Gravity.CENTER);

        // Set the TextView text font family and text size
        tv.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

        // Set the TextView text (GridView item text)
        tv.setText(dataList.get(position));

        // Return the TextView widget as GridView item
        return tv;
    }

    // Method for converting DP value to pixels
    public static int getPixelsFromDPs(Activity activity, int dps) {
        Resources r = activity.getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
}