package f15.delta.com.fdoodle;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ScheduleActivity extends ActionBarActivity {

    public static Typeface f4 = null;
    private Scheduleadapter mscheduleadapter;
    ViewPager mviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.ColorSchedule)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Schedule </font>"));
        TextView t1 = (TextView) findViewById(R.id.textView1);
        f4 = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/gnu.ttf");
        t1.setTypeface(f4);
        mscheduleadapter = new Scheduleadapter(getSupportFragmentManager());
        mviewpager = (ViewPager) findViewById(R.id.pager);
        mviewpager.setAdapter(mscheduleadapter);

    }




    public class Scheduleadapter extends FragmentStatePagerAdapter {
        public Scheduleadapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {

            // TODO Auto-generated method stub
            Bundle args = new Bundle();
            // Our object is just an integer :-P

            Fragment fragment = new Scheduleobject();
            args.putInt(Scheduleobject.ARG_OBJECT, arg0 + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return super.getPageTitle(position);
        }


    }

    public static class Scheduleobject extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View rootview = inflater.inflate(R.layout.fragment_left, container, false);
            final Bundle args = getArguments();
            if (args.getInt(ARG_OBJECT) == 1) {
                ((ImageView) rootview.findViewById(R.id.schedimage)).setImageResource(R.drawable.d0_comp);
                ((TextView) rootview.findViewById(R.id.daytxt)).setText("Days 0&1");
                ((TextView) rootview.findViewById(R.id.daytxt)).setTypeface(f4);
            }
            if (args.getInt(ARG_OBJECT) == 2) {
                ((ImageView) rootview.findViewById(R.id.schedimage)).setImageResource(R.drawable.d1_comp);
                ((TextView) rootview.findViewById(R.id.daytxt)).setText("Day 2");
                ((TextView) rootview.findViewById(R.id.daytxt)).setTypeface(f4);
            }
            if (args.getInt(ARG_OBJECT) == 3) {

                ((ImageView) rootview.findViewById(R.id.schedimage)).setImageResource(R.drawable.d2_comp);
                ((TextView) rootview.findViewById(R.id.daytxt)).setText("Day 3");
                ((TextView) rootview.findViewById(R.id.daytxt)).setTypeface(f4);
            }

            ((ImageView) rootview.findViewById(R.id.schedimage)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), PopupActivity.class);
                    intent.putExtra("num", args.getInt(ARG_OBJECT));
                    startActivity(intent);
                }
            });

            return rootview;


        }


    }


}
