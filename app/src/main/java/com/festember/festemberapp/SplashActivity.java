
package com.festember.festemberapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.festember.festemberapp.R;
public class SplashActivity extends Activity {
    int screen_width;
    int screen_height;
    boolean loading=true;
    TextView bm=null;
    SharedPreferences prefs;
    String o="#BreakTheMonotony";

    Handler myHandler;
    Read_write_file fileOps;
    Runnable r=new Runnable() {
        @Override
        public void run() {

            Log.e("state5",String.valueOf(Utilities.status));
            Intent i;
            if(Utilities.status!=0) {
                i= new Intent(SplashActivity.this, Hscreen.class);

            }

            else
            {
                i= new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(i);
            finish();

        }
    };


    public void state2(final LinearLayout l2)
    {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                l2.setBackgroundResource(R.drawable.logo2);
                state3(l2);
            }
        }, 500);
    }
    public void state3(final LinearLayout l2)
    {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                l2.setBackgroundResource(R.drawable.logoin);

                state4();

            }
        }, 400);
    }


    public void Startmonotomy(final int i) {

        //Bitmaphandle.loadBitmap(this.getResources().getIdentifier("f"+String.valueOf(i+1), "drawable",this.getPackageName()),f[i]);

        //String s="";
        //int t=0;
        //while(++t<17-(i%17))s+=" ";
        bm.setText(o.substring((16-(i%17)),17));
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (i<33||loading) Startmonotomy((i + 1));
                else {
                    bm.setText(o);
                    state5();
                }
            }
        }, 300-15*(i%16));

    }

    public void state4()
    {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout r=(RelativeLayout)findViewById(R.id.loadingPanel);
                r.setVisibility(View.VISIBLE);
                Startmonotomy(0);

            }
        }, 250);
    }
    public void state5()
    {
        myHandler.postDelayed(r, 700);
    }


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screen_height =Utilities.screen_height= displaymetrics.heightPixels;
        screen_width =Utilities.screen_width= displaymetrics.widthPixels;


        myHandler = new Handler();
        bm=(TextView)findViewById(R.id.bmtext);
        fileOps = new Read_write_file(this);
        Typeface f = Typeface.createFromAsset(bm.getContext().getAssets(),
                "fonts/gnu.ttf");
        bm.setTypeface(f);
        Utilities.desc_check = getSharedPreferences("desc", 0);
        Utilities.check_desc = Utilities.desc_check.getInt("check", 0);
        Utilities.upcoming_check = Utilities.desc_check.getInt("upcoming_parse",0);



        new Utilstask().execute();

        StartAnimations();
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveVouchers();
            }
        }).start();

    }

    private void saveVouchers()
    {
        Bitmap bitmap1 = new SaveImage("festembervoucher1", null).loadFromCacheFile();
        Bitmap bitmap2 = new SaveImage("festembervoucher2", null).loadFromCacheFile();
        Bitmap bmp;
        if(bitmap1==null)
        {
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.festembervoucher1);
            SaveImage save = new SaveImage("festembervoucher1", bmp);
            save.saveToCacheFile(bmp);
            QRCodeWelcomePage.addImageToGallery(save.getCacheFilename(), SplashActivity.this);
            System.out.println("Voucher 1 saved.");
        }
        if(bitmap2==null)
        {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.festembervoucher2);
            SaveImage save = new SaveImage("festembervoucher2", bmp);
            save.saveToCacheFile(bmp);
            QRCodeWelcomePage.addImageToGallery(save.getCacheFilename(), SplashActivity.this);
            System.out.println("Voucher 2 saved.");
        }

    }


    private void updateUtils() {

        prefs = getSharedPreferences("LogInPrefs", Context.MODE_PRIVATE);
        Utilities.status = prefs.getInt("Logged_in",0);
        Utilities.f_id = prefs.getString("f_id", "");
        Utilities.f_pass=prefs.getString("f_pass","");
        Utilities.f_email=prefs.getString("f_email","");

        if(Utilities.status!=0)
        Profile.setProfile(Integer.valueOf(Utilities.f_id),prefs.getString("f_name",""),Utilities.f_email,prefs.getString("f_fullname",""));

    }

    class Utilstask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Apicalls().execute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Sharedprefs-content-Utils

            updateUtils();
            //SharedPrefs-Profile Class

            //JSON FILE-EventListdesc-Event objects


            return null;
        }
    }
    class Apicalls extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading=false;
            //bm.setText(o);
            //state5();

        }

        @Override
        protected Void doInBackground(Void... voids) {


            //Events-api

            //Events-description-once

            //

            //if(Utilities.check_desc==0)
            {
                System.out.println("calling parse");
                callDescParse(Utilities.url_eventsdesc,"description.txt");
            }

            //if(Utilities.upcoming_check == 0)
            {
                System.out.println("calling upcoming");
                callDescParse(Utilities.url_events,"upcoming.txt");
            }


            return null;
        }
    }

    @Override
    public void onBackPressed() {
        myHandler.removeCallbacks(r);

        super.onBackPressed();
    }

    private void StartAnimations() {
        final ImageView ivl = (ImageView) findViewById(R.id.logol);
        final ImageView ivr = (ImageView) findViewById(R.id.logor);
        final LinearLayout l=(LinearLayout)findViewById(R.id.logo);
        final LinearLayout l2=(LinearLayout)findViewById(R.id.logoin);

        //Animation anim = AnimationUtils.loadAnimation(this, R.anim.translater);
        //Animation animr = AnimationUtils.loadAnimation(this, R.anim.translate);
        //anim.reset();

        final Animation animation = new TranslateAnimation((screen_width)/2-screen_width/10,0,0, 0);
        animation.setDuration(1500);
        animation.setInterpolator(new BounceInterpolator());

        final Animation animationl = new TranslateAnimation( -((screen_width)/2-screen_width/10),0,0, 0);
        animationl.setDuration(1500);
        animationl.setInterpolator(new BounceInterpolator());
        final Animation anim = new ScaleAnimation(
                0.0125f, 1f, // Start and end values for the X axis scaling
                1f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF,0f); // Pivot point of Y scaling
        anim.setDuration(1500);
        anim.setInterpolator(new BounceInterpolator());
        //anim.setFillAfter(true); // Needed to keep the result of the animation


       anim.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) {

           }

           @Override
           public void onAnimationEnd(Animation animation) {


               myHandler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       l2.setBackgroundResource(R.drawable.logo1);
                       state2(l2);
                   }
               }, 200);


           }

           @Override
           public void onAnimationRepeat(Animation animation) {

           }
       });



        //animr.reset();

        ivl.clearAnimation();
        ivr.clearAnimation();


        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivl.setVisibility(View.VISIBLE);
                ivr.setVisibility(View.VISIBLE);
                l.setVisibility(View.VISIBLE);
                ivl.startAnimation(animation);
                ivr.startAnimation(animationl);
                l.startAnimation(anim);
            }
        }, 700);



    }
    public void callDescParse(String url, final String filename){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling
                        System.out.println("resopnse saving");
                        fileOps.writeToFile(response, filename);
                        Utilities.check_desc=1;
                        Utilities.upcoming_check=1;
                        SharedPreferences.Editor editor = Utilities.desc_check.edit();
                        editor.putInt("check",1);
                        editor.putInt("upcoming_parse",1);
                        editor.apply();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                Log.e("file","Something went wrong!");
                error.printStackTrace();

            }
        });
        // Add the request to the queue
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}