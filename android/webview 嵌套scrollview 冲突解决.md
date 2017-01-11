``` JAVA
package yf.o2o.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ScrollView;

public class InnerWebView extends WebView {

    /**
     */
    public ScrollView parentScrollView;//父容器ScrollView
    int currentY;

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    public InnerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parentScrollView == null) {
            return super.onInterceptTouchEvent(ev);
        } else {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 将父scrollview的滚动事件拦截
                currentY = (int) ev.getY();
                setParentScrollAble(false);
                return super.onInterceptTouchEvent(ev);
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            }
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parentScrollView != null) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int height = getContentHeight();
                float webViewContentHeight = getContentHeight() * getScale();
                //WebView的现高度
                float webViewCurrentHeight = (getHeight() + getScrollY());
//                System.out.println("height=" + height);
                int scrollY = getScrollY();
//                System.out.println("scrollY" + scrollY);
                int y = (int) ev.getY();

                // 手指向下滑动
                if (currentY < y) {
                    if (scrollY <= 0) {
                        // 如果向下滑动到头，就把滚动交给父Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }
                } else if (currentY > y) {
                    if ((webViewContentHeight - webViewCurrentHeight) == 0) {
                        // 如果向上滑动到头，就把滚动交给父Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }

                }
                currentY = y;
            }
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }

}
```