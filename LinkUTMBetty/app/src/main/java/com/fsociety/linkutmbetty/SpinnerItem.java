package com.fsociety.linkutmbetty;

import android.widget.Spinner;

/**
 * Created by axl50 on 29/03/2017.
 */

public class SpinnerItem {
    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    private String name;
    private int iconId;
    public SpinnerItem(String Name, int IconId){
        name = Name;
        iconId = IconId;
    }
}
