package zhq.com.slidingmenusuper;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;


/**
 * Created by dance on 2017/11/21.
 * 如果我们要继承一个ViewGroup，并且自己不想实现onMeasure和onLayout，那么可以选择继承已有的
 * 一个布局，比如FrameLayout
 */

public class SlidingMenuSuper extends FrameLayout {
    public SlidingMenuSuper(Context context) {
        this(context, null);
    }

    public SlidingMenuSuper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    View main,menu;
    ViewDragHelper dragHelper;
    int maxLeft;//最大的左边
    public SlidingMenuSuper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dragHelper = ViewDragHelper.create(this, cb);

    }

    /**
     * 当填充完子View的时候执行，所以可以获取到自己的所有子View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menu = getChildAt(0);
        main = getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //让ViewDragHelper帮我们判断是否应该拦截
        boolean result = dragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //让ViewDragHelper帮我们处理触摸事件
        dragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        /**
         * 是否捕获当前子View的触摸事件
         * @param child     当前触摸到的子View
         * @param pointerId   触摸点的索引，就是多点触摸的时候，每个触摸点的id，
         * @return  true表示捕获，false不捕获
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==main || child==menu;
        }

        /**
         * 当一个View的触摸事件被捕获的时候执行
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            maxLeft = (int) (getMeasuredWidth() * 0.6f);
        }

        /**
         * 好像是获取child的水平拖拽范围的, 这个方法的作用其实是用来控制是否想强制水平滑动
         * 如果返回任意大于0的数，就可以强制水平滑动，反之就不能水平滑动
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /**
         * 控制，修改修正View水平方向的位置，在View位置改变之前执行
         * @param child 当前触摸的子View
         * @param left  表示ViewDragHelper计算好的left值，left=child.getLeft()+dx
         * @param dx  表示每次移动的水平距离，向右移动就是正，向左就是负值
         * @return  返回的值最终决定child的left
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //只限制main
            if(child==main){
                left = fixLeft(left);
            }

            return left;
        }

        /**
         * 控制View垂直方向的滑动
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        /**
         * 当一个View的位置改变之后执行，可以获取到移动的距离，从而实现一些伴随移动的效果
         * @param changedView   位置改变的View
         * @param left  改变后最新的left
         * @param top   改变后最新的top
         * @param dx    每次手指水平移动的距离
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //如果当前位置改变的是menu，那么我们手动让main也移动
            if(changedView==menu){
                //通过layout方法来固定menu的位置
                menu.layout(0, 0, menu.getMeasuredWidth(), menu.getMeasuredHeight());

                int newLeft = main.getLeft() + dx;
                newLeft = fixLeft(newLeft);
                main.layout(newLeft, 0, newLeft+main.getMeasuredWidth(), main.getMeasuredHeight());
            }

            //执行一系列的动画
            //1.计算main滑动的百分比进度
            float fraction = main.getLeft()*1f / maxLeft;
            //2.根据百分比执行动画
            execAnim(fraction);
        }

        /**
         * 抬起手指的时候执行
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(main.getLeft() > maxLeft/2){
                //open
//                scroller.startScroll();
                dragHelper.smoothSlideViewTo(main, maxLeft, 0);
                ViewCompat.postInvalidateOnAnimation(SlidingMenuSuper.this);
            }else {
                //close
                dragHelper.smoothSlideViewTo(main, 0, 0);
                ViewCompat.postInvalidateOnAnimation(SlidingMenuSuper.this);
            }
        }
    };

    FloatEvaluator floatEval = new FloatEvaluator();
    /**
     * 执行动画
     * @param fraction
     */
    private void execAnim(float fraction) {
        //1.main执行缩放
        main.setScaleY(floatEval.evaluate(fraction, 1f, 0.8f));//1->0.6
        main.setScaleX(floatEval.evaluate(fraction, 1f, 0.8f));//1->0.6

        //2.menu执行缩放
        menu.setScaleY(floatEval.evaluate(fraction, 0.2f, 1f));//1->0.6
        menu.setScaleX(floatEval.evaluate(fraction, 0.2f, 1f));//1->0.6
        //平移
        menu.setTranslationX(floatEval.evaluate(fraction, -menu.getMeasuredWidth()/2, 0));
     //menu.setTranslationY(floatEval.evaluate(fraction, -menu.getMeasuredWidth(), 0));

        //3d动画
       /* main.setRotationY(floatEval.evaluate(fraction, 0, 90));
        main.setTranslationX(floatEval.evaluate(fraction, 0, main.getMeasuredWidth()/2));
        menu.setRotationY(floatEval.evaluate(fraction, -90, 0));*/
    }

    Scroller scroller = null;

    @Override
    public void computeScroll() {
        super.computeScroll();
        //scroller的写法
//        if(scroller.computeScrollOffset()){
//            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
//        }
        //ViewDragHelper的写法
        if(dragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlidingMenuSuper.this);
        }
    }

    /**
     * 限制left变量的取值范围
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if(left>maxLeft){
            left = maxLeft;
        }else if(left<0){
            left = 0;
        }
        return left;
    }

    //原始的实现方法
//    float downX,downY;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                downY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //1.计算手指移动的距离
//                float deltaX = event.getX() - downX;
////                float deltaY = event.getY() - downY;
//                //2.让View移动
//                scrollTo(x,y);
//                scrollBy();
//                offsetLeftAndRight(-10);
//                offsetTopAndBottom(-10);
//                setTranslationX(10);
//                setTranslationY(10);
//                view.layout(l,t,r,b);
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return true;
//    }


}
