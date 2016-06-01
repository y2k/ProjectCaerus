package com.android.internal.policy.impl;

import android.content.Context;
import android.util.Log;
import com.android.internal.policy.IPolicy;

public class Policy implements IPolicy {
    private static final String TAG = "PhonePolicy";
    private static final String[] preload_classes = new String[]{"com.android.internal.policy.impl.PhoneLayoutInflater", "com.android.internal.policy.impl.PhoneWindow", "com.android.internal.policy.impl.PhoneWindow$1", "com.android.internal.policy.impl.PhoneWindow$ContextMenuCallback", "com.android.internal.policy.impl.PhoneWindow$DecorView", "com.android.internal.policy.impl.PhoneWindow$PanelFeatureState", "com.android.internal.policy.impl.PhoneWindow$PanelFeatureState$SavedState"};

    public Policy() {
    }

    public PhoneWindow makeNewWindow(Context context) {
        return new PhoneWindow(context);
    }

    public PhoneLayoutInflater makeNewLayoutInflater(Context context) {
        return new PhoneLayoutInflater(context);
    }

    public PhoneWindowManager makeNewWindowManager() {
        return new PhoneWindowManager();
    }

    static {
//        String[] arr$ = preload_classes;
//        int len$ = arr$.length;
//
//        for(int i$ = 0; i$ < len$; ++i$) {
//            String s = arr$[i$];
//
//            try {
//                Class.forName(s);
//            } catch (ClassNotFoundException var5) {
//                Log.e("PhonePolicy", "Could not preload class for phone policy: " + s);
//            }
//        }
    }
}
