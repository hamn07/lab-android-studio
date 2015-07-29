package tw.org.iiiedu.taichung.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private ViewPager pager;
    private LayoutInflater inflater; //current ui's floating layout
    private ArrayList<View> views;
    private TextView tv;
    private ViewFlipper flipper;
    private View f1,f2,f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        pager = (ViewPager) findViewById(R.id.pager);
        initViewPager();
    }

    private void initViewPager() {

        inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1,null));
        views.add(inflater.inflate(R.layout.page2,null));
        views.add(inflater.inflate(R.layout.page3,null));
        views.add(inflater.inflate(R.layout.page4,null));
        views.add(inflater.inflate(R.layout.page5,null));

        pager.setOnPageChangeListener(new PageListener());
        pager.setAdapter(new MyPageAdapter());
        pager.setCurrentItem(0);

        flipper = (ViewFlipper) views.get(0).findViewById(R.id.flipper);
        f1 = views.get(0).findViewById(R.id.f1);
        f2 = views.get(0).findViewById(R.id.f2);
        f3 = views.get(0).findViewById(R.id.f3);

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
            }
        });
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            tv.setText("page:"+position);
        }
    }

    private class AnimationListener implements Animation.AnimationListener {

        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        @Override
        public void onAnimationEnd(Animation animation) {

        }

        /**
         * <p>Notifies the repetition of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class MyPageAdapter extends PagerAdapter {

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * Determines whether a page View is associated with a specific key object
         * as returned by {@link #instantiateItem(View, int)}. This method is
         * required for a PagerAdapter to function properly.
         *
         * @param view   Page View to check for association with <code>object</code>
         * @param object Object to check for association with <code>view</code>
         * @return true if <code>view</code> is associated with the key object <code>object</code>
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(View container, int position) {

            ((ViewPager)container).addView(views.get(position));

            return views.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(views.get(position));
        }
    }

}
