>bug: scrollview嵌套listview后，当scrollview滑动会优先处理，导致listview里的内容不可滑动。debug:如代码所示。关键点:重写listview 传入父容器，在listview被按下的时候将父容器的requestDisallowInterceptTouchEvent为true 父容器将不再处理滑动。当手指抬起或取消时设为flase。将恢复父容器的滑动。

```JAVA
package cn.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerListView extends ListView {

    ScrollView parentScrollView;  //父容器

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }
                //传入父容器
    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    private int maxHeight;

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        if (maxHeight > -1) {

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(getChildAt(0));
    }

//重写该方法 在按下的时候让父容器不处理滑动事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                setParentScrollAble(false);
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }

}
```
------------------
##第二种
``` JAVA
package yf.o2o.store.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 如果不设置parentscrollview,当前的listview会铺满整个全部展开
 * 如果设置了parentscrollview,当前的listview将优先把事件在当前的touch中拦截,并使父 容器不可滑动
 * ,当滑动处超出listview,也就是焦点落在外部父容器上时将恢复焦点,因为在aciton.up恢复了父容器的事件处理
 *
 *
 * @author liushenen
 *
 */
public class InnerListView extends ListView {

	ScrollView parentScrollView; // 父容器
	boolean topOrBottom = false;
	int mLastY = 9999;
	private int TIME = 100;
	int mlastscrollY = Integer.MAX_VALUE;// 底部距离
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			topOrBottom = true;
		}
	};

	// private Boolean isbottom = false; // 是否是到底部了

	public ScrollView getParentScrollView() {
		return parentScrollView;
	}

	// 传入父容器
	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

	private int maxHeight;

	public int getMaxHeight() {
		return maxHeight;

	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (parentScrollView != null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

	// 重写该方法 在按下的时候让父容器不处理滑动事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 将父scrollview的滚动事件拦截
			mLastY = (int) ev.getY();
			mlastscrollY = Integer.MAX_VALUE;// 底部距离
			setParentScrollAble(false);
			topOrBottom = false;
			return super.onInterceptTouchEvent(ev);

		case MotionEvent.ACTION_UP:
			setParentScrollAble(true);
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_CANCEL:

			break;

		}

		return super.onInterceptTouchEvent(ev);
	}

	public int getmScrollY() {
		View c = getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = getFirstVisiblePosition();
		int top = c.getTop();
		return firstVisiblePosition * c.getHeight() - top;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		View child = getChildAt(0);
		boolean onTouchEvent = super.onTouchEvent(ev);

		if (parentScrollView != null) {
			if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				int scrollY = getmScrollY();
				final int y = (int) ev.getY();
				if (y > mLastY) {

					// Log.e("xys", " 手指向下滑动scrollY==" + scrollY);// 到底部了
					if (scrollY <= 0) {
						Log.e("xys", "到头了触发2222");
						setParentScrollAble(true); // 如果向下滑动到头，就把滚动交给父Scrollview
													// Log.e("xys", "滑动到头了");

					}
				} else {
					// Log.e("xys", " 手指向上滑动scrollY==" + scrollY);
					// 到底部了
					if (scrollY == mlastscrollY) {

						if (getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {

							setParentScrollAble(true); // 如果向上滑动到头，就把滚动交给父Scrollview
														// //Log.e("xys",
														// "到底了触发111");
						}
					}
					handler.postDelayed(runnable, TIME); // 每隔1s执行
				}
				mLastY = y;
				if (topOrBottom) {
					topOrBottom = false;
					mlastscrollY = scrollY;
				}

			}
		}

		return onTouchEvent;
	}

	/**
	 * @param flag
	 */
	private void setParentScrollAble(boolean flag) {
		if (parentScrollView != null) {
			parentScrollView.requestDisallowInterceptTouchEvent(!flag);
		}

	}
}

```