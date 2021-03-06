package com.lxyx.helllib;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by habbyge on 2019/03/24.
 */
public interface IHellOnActivityListener {
    void startActivity(Object srcActivity, String srcActivityName, Intent targetIntent);

    void finish(Activity srcActivity, String srcActivityName);

    boolean moveTaskToBack(Activity srcActivity, String srcActivityName, boolean nonRoot);

    void onCreate(Activity activity);

    void onNewIntent(Activity activity, Intent intent);

    void onResume(Activity activity);

    void onPostResume(Activity activity);

    void onPause(Activity activity);

    void onStop(Activity activity);

    void onDestroy(Activity activity);
}
