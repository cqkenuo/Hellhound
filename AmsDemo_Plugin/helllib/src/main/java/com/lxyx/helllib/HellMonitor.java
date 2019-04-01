package com.lxyx.helllib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by habbyge 2019/3/6.
 */
public final class HellMonitor {
    private static final String TAG = "HellMonitor";

    private static HellMonitor sInstance;

    public static HellMonitor getInstance() {
        if (sInstance == null) {
            synchronized (HellMonitor.class) {
                if (sInstance == null) {
                    sInstance = new HellMonitor();
                }
            }
        }
        return sInstance;
    }

    private HellMonitor() {
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ View操作相关 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void callClickListenerBefore(int clickType, View view) {
        mListener.onClickBefore(clickType, view);
    }

    public void callClickListenerAfter(int clickType, View view) {
        mListener.onClickAfter(clickType, view);
    }

    private final IHellOnClickListener mListener = new IHellOnClickListener() {
        @Override
        public void onClickBefore(int clickType, View view) {
            if (view == null) {
                return;
            }

            StringBuilder showTextSb = new StringBuilder("点击之前，注入：");

            String viewName = view.getClass().getName();
            System.out.println(TAG + ", view = " + viewName);
            showTextSb.append(viewName);

            System.out.println(TAG + ", clickType = " + clickType);
            showTextSb.append("|").append(clickType);

            view.setBackgroundResource(android.R.color.holo_green_dark);

            System.out.println("HABBYGE-MALI, onClickBefore: " + showTextSb.toString());

            // 这里从当前View开始，向上遍历，获取View树
            String viewId = getViewId(view);
            if (viewId == null || viewId.isEmpty()) {
                return;
            }
            System.out.println("habbyge-mali, onClickBefore, viewId = " + viewId);
        }

        @Override
        public void onClickAfter(int clickType, View view) {
            if (view == null) {
                return;
            }

            StringBuilder showTextSb = new StringBuilder("点击之后，注入：");

            String viewName = view.getClass().getName();
            System.out.println(TAG + ", view = " + viewName);
            showTextSb.append(viewName);

            System.out.println(TAG + ", clickType = " + clickType);
            showTextSb.append("|").append(clickType);

            Toast toast = Toast.makeText(view.getContext(), showTextSb.toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

//            view.setBackgroundResource(android.R.color.holo_blue_bright);

            System.out.println("HABBYGE-MALI, onClickAfter: " + showTextSb.toString());
        }
    };

    public void callbackItemClickBefore(AdapterView<?> parent, View view, int position, long id) {
        itemListener.onItemClickBefore(parent, view, position, id);
    }
    public void callbackItemClickAfter(AdapterView<?> parent, View view, int position, long id) {
        itemListener.onItemClickAfter(parent, view, position, id);
    }

    private final IHellOnItemClickListener itemListener = new IHellOnItemClickListener() {
        @Override
        public void onItemClickBefore(AdapterView<?> parent, View view, int position, long id) {
            // 这里从当前View开始，向上遍历，获取View树
            String viewId = getViewId(view);
            if (viewId == null || viewId.isEmpty()) {
                return;
            }
            System.out.println("HABBYGE-MALI, onItemClickBefore, itemListener: " + viewId);
        }

        @Override
        public void onItemClickAfter(AdapterView<?> parent, View view, int position, long id) {
            // 这里从当前View开始，向上遍历，获取View树
            String viewId = getViewId(view);
            if (viewId == null || viewId.isEmpty()) {
                return;
            }
            System.out.println("HABBYGE-MALI, onItemClickAfter, itemListener: " + viewId);
        }
    };

    /**
     * @return 返回的数据格式是：
     *      所属于的Activity:viewPaths(以|分割，每个控件包括控件类名和在其父类的位置，
     *      格式是: viewPath[n]):resourceid
     */
    @SuppressLint("PrivateApi")
    private static String getViewId(View view) {
        if (view == null) {
            return null;
        }

        Class<?> viewRootClass;
        Class<?> decorViewClass;
        try {
            viewRootClass = Class.forName("android.view.ViewRootImpl");
            decorViewClass = Class.forName("com.android.internal.policy.DecorView");
        } catch (Exception e) {
            return null;
        }
        String viewId = view.getClass().getSimpleName();
        ViewParent viewParent;
        ViewGroup parent;
        int viewIndex;
        while ((viewParent = view.getParent()) != null
                && !viewRootClass.isInstance(viewParent)
                && !decorViewClass.isInstance(viewParent)) {

            parent = (ViewGroup) viewParent;
            viewIndex = parent.indexOfChild(view);
            // noinspection StringConcatenationInLoop
            viewId += "|" + parent.getClass().getSimpleName() + "[" + viewIndex + "]";

            view = parent; // next
        }
        return viewId;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Activity相关 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @param eventType 这里太懒，事件编号分别对应：create-0,resume-1,pause-2,stop-3,destroy-4
     */
    public void callActivityListener(Activity activity, int eventType) {
        switch (eventType) {
        case HellConstant.ACTIVITY_EVENT_OnCreate:
            mActivityListener.onCreate(activity);
            break;
        case HellConstant.ACTIVITY_EVENT_OnResume:
            mActivityListener.onResume(activity);
            break;
        case HellConstant.ACTIVITY_EVENT_OnPause:
            mActivityListener.onPause(activity);
            break;
        case HellConstant.ACTIVITY_EVENT_OnStop:
            mActivityListener.onStop(activity);
            break;
        case HellConstant.ACTIVITY_EVENT_OnDestroy:
            mActivityListener.onDestroy(activity);
            break;
        }
    }

    public void callbackActivityOnNewIntentListener(Activity activity, Intent intent) {
        mActivityListener.onNewIntent(activity, intent);
    }

    public static void callbackStartActivity(Object srcActivity, Intent intent) {
        System.out.println("HABBYGE-MALI, callbackStartActivity()");
    }

    public void callbackStartActivity(Object srcActivity, String srcActivityName, Intent intent) {
        mActivityListener.startActivity(srcActivity, srcActivityName, intent);
    }

    public void callbackFinish(Activity srcActivity, String srcActivityName) {
        mActivityListener.finish(srcActivity, srcActivityName);
    }

    public void callbackMoveTaskToBack(Activity srcActivity, String srcActivityName, boolean nonRoot) {
        mActivityListener.moveTaskToBack(srcActivity, srcActivityName, nonRoot);
    }

    // 返回调用者对象、目标对象，这样就可以把用户页面行为串起来了。
    private final IHellOnActivityListener mActivityListener = new IHellOnActivityListener() {
        @Override
        public void startActivity(Object srcActivity, String srcActivityName, Intent targetIntent) {
            System.out.println("HABBYGE-MALI, mActivityListener, startActivity: "
                    + srcActivity.getClass().getName() + " | " + srcActivityName);

            // 调用者可能是Activity或Context，所以这里县写成Object类型，根据需要自己转换
            // 从 targetIntent 中可以获取跳转参数，包括：跳转目标Activity，此时就可以获取跳转源和跳转目标
        }

        @Override
        public void finish(Activity srcActivity, String srcActivityName) {
            System.out.println("HABBYGE-MALI, mActivityListener, finish: "
                    + srcActivity.getClass().getName() + " | " + srcActivityName);
        }

        @Override
        public boolean moveTaskToBack(Activity srcActivity, String srcActivityName, boolean nonRoot) {
            System.out.println("HABBYGE-MALI, mActivityListener, moveTaskToBack: " +
                    srcActivity.getClass().getName() + " | " + srcActivityName + " | " + nonRoot);
            return false;
        }

        @Override
        public void onCreate(Activity activity) {
            System.out.println("HABBYGE-MALI, Activity, onCreate: " + activity.getClass().getName());
        }

        @Override
        public void onNewIntent(Activity activity, Intent intent) {
            System.out.println("HABBYGE-MALI, Activity, onNewIntent: " + activity.getClass().getName());
            // 可以从Intent中取出想要的参数
        }

        @Override
        public void onResume(Activity activity) {
            System.out.println("HABBYGE-MALI, Activity, onResume: " + activity.getClass().getName());
        }

        @Override
        public void onPause(Activity activity) {
            System.out.println("HABBYGE-MALI, Activity, onPause: " + activity.getClass().getName());
        }

        @Override
        public void onStop(Activity activity) {
            System.out.println("HABBYGE-MALI, Activity, onStop: " + activity.getClass().getName());
        }

        @Override
        public void onDestroy(Activity activity) {
            System.out.println("HABBYGE-MALI, Activity, onDestroy: " + activity.getClass().getName()) ;
        }
    };


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Fragment相关 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    void callbackFragment(Fragment fragment, int eventType, Bundle savedInstanceState) {
        System.out.println("HABBYGE-MALI, callbackFragment: " +
                fragment.getClass().getName() + " | " + eventType);
    }

    void callbackFragment(Fragment fragment, int eventType) {
        System.out.println("HABBYGE-MALI, callbackFragment: " +
                fragment.getClass().getName() + " | " + eventType);
    }
}
