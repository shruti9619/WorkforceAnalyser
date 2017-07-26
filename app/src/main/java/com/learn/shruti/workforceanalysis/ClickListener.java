package com.learn.shruti.workforceanalysis;

import android.view.View;

/**
 * Created by Shruti on 26/07/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
