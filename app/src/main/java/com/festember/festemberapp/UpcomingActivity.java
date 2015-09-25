package com.festember.festemberapp;




import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import java.util.Map;
import java.util.TimeZone;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.festember.festemberapp.R;

public class UpcomingActivity extends AppCompatActivity {
    Typeface p;


    String[] Number;
    String[][] present;
    String[][] tempeve;
    int[][] prtime;
    int[][] temptime;
    int[] id;
    String[] evstarttime;
    String[] evendtime;
    String[] evdate;
    String[] evlastupdate;


    int no,t,ch=-1,catech=-1;  //Updated for dynamic spinnerview
    int timelimit=1;

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    String url = "https://api.festember.com/events/list";

    Event[] Eve;

    int date=26;
    String noentrytest="No ongoing/upcoming events.\nWhy not visit the FOODSTALLS instead?";
    private Read_write_file fileOps;

    //new data for dynamic spinnerviews
    String[] venues={"barn"};
    String[] cates={"dramatics"};
    int co1=1,co2=1;
    Spinner spinner ;
    Spinner spinnertime;
    Spinner spinnercate;
    ArrayAdapter<String> spadapter;
    ArrayAdapter<String> caadapter;

    //Updated for dynamic spinnerview
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        /*savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
        savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);*/



        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("spinner1", spinner.getSelectedItemPosition());
        savedInstanceState.putInt("spinner2", spinnertime.getSelectedItemPosition());
        savedInstanceState.putInt("spinner3", spinnercate.getSelectedItemPosition());
    }

    //Updated for dynamic spinnerview
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);



        if (savedInstanceState != null) {
            ch = savedInstanceState.getInt("spinner1") - 1;
            timelimit = savedInstanceState.getInt("spinner2") + 1;
            catech = savedInstanceState.getInt("spinner3") - 1;
        }





    }



    protected void sort(String[][] tevents,  int[][] ttime,  int o){



        Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DATE);
        Calendar timenow=new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
        timenow.set(Calendar.DATE,date);

        String[] temp;
        int[] tem;
        int chcheck=0,catechcheck=0;

        prtime=new int[o][6];
        String[][] store=new String[o][3];

        for(int i=0;i<o;i++){

            for(int j=i;j<o-1-i;j++){
                Calendar time1=new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
                Calendar time2=new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
                time1.set(timenow.get(Calendar.YEAR), 8, ttime[j][0], ttime[j][1], ttime[j][2]);
                time2.set(timenow.get(Calendar.YEAR), 8, ttime[j + 1][0], ttime[j + 1][1], ttime[j + 1][2]);


                if(time1.after(time2)){
                    temp=tevents[j];
                    tevents[j]=tevents[j+1];
                    tevents[j+1]=temp;

                    tem=ttime[j];
                    ttime[j]=ttime[j+1];
                    ttime[j+1]=tem;


                }
            }
        }

        Calendar time3=new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
        time3.set(Calendar.YEAR,timenow.get(Calendar.YEAR));
        time3.set(Calendar.MONTH,timenow.get(Calendar.MONTH));
        time3.set(Calendar.DATE,timenow.get(Calendar.DATE));
        time3.set(Calendar.HOUR_OF_DAY,timenow.get(Calendar.HOUR_OF_DAY)+timelimit);
        time3.set(Calendar.MINUTE,timenow.get(Calendar.MINUTE));

        if(time3.get(Calendar.HOUR_OF_DAY)>23){
            time3.set(Calendar.HOUR_OF_DAY,((timenow.get(Calendar.HOUR_OF_DAY))-24));
            time3.set(Calendar.DATE,(timenow.get(Calendar.DATE)+1));
        }


        t=0;

        for(int k=0;k<o;k++) {

            Calendar time4 = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
            time4.set(timenow.get(Calendar.YEAR), 8, ttime[k][0], ttime[k][1], ttime[k][2]);

            Calendar time5 = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
            time5.set(timenow.get(Calendar.YEAR), 8, ttime[k][3], ttime[k][4], ttime[k][5]);

            if ((time4.before(time3)) && timenow.before(time5)) {


                catechcheck=0;
                chcheck=0;


                //Updated for dynamic spinnerview

                if(catech==-1){
                    catechcheck=1;
                }


                else if((tevents[k][2].equalsIgnoreCase(cates[catech]))){
                    catechcheck=1;
                }


                if(ch==-1){
                    chcheck=1;
                }

                else if((tevents[k][1].equalsIgnoreCase(venues[ch]))){
                    chcheck=1;
                }



                if(chcheck==1&&catechcheck==1) {
                    store[t] = tevents[k];
                    prtime[t] = ttime[k];
                    t++;
                }
            }
        }



        present=new String[t][3];
        Number = new String [t];

        for (int q = 0; q < t; q++) {

            present[q] = store[q];


        }






    }


    public void netparse(String response)
    {
        try {



            JSONObject dataJsonObj = new JSONObject(response);

            JSONArray dataJsonArr = new JSONArray();

            dataJsonArr = dataJsonObj.getJSONArray("data");
            no=dataJsonArr.length();
            tempeve=new String[no][3];
            temptime=new int[no][6];
            id=new int[no];
            Eve=new Event [no];
            evstarttime=new String[no];
            evendtime=new String[no];
            evdate=new String[no];
            evlastupdate=new String[no];
            //Updated for dynamic spinnerview
            String[] temp1=new String[no];
            String[] temp2=new String[no];
            co1=0;
            co2=0;



            for (int i = 0; i < no; i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);





                tempeve[i][0] = (c.getString("event_name"));
                evstarttime[i] = c.getString("event_start_time");
                evendtime[i] = c.getString("event_end_time");
                tempeve[i][1] = c.getString("event_venue");
                tempeve[i][2] =c.getString("event_cluster");
                evdate[i] = c.getString("event_date");
                id[i] =  c.getInt("event_id");
                evlastupdate[i]=c.getString("event_last_update_time");

                temptime[i][0] = (Integer.parseInt(evdate[i].substring(8, 10)));
                temptime[i][1] = (Integer.parseInt(evstarttime[i].substring(0, 2)));
                temptime[i][2] = (Integer.parseInt(evstarttime[i].substring(3, 5)));
                temptime[i][4] = (Integer.parseInt(evendtime[i].substring(0, 2))) ;
                temptime[i][5] = (Integer.parseInt(evendtime[i].substring(3, 5)));
                if (temptime[i][1] < temptime[i][4]) {
                    temptime[i][3] = temptime[i][0] ;
                }
                else if(temptime[i][2]<temptime[i][5]){
                    temptime[i][3] = temptime[i][0] ;
                }
                else{
                    temptime[i][3] = temptime[i][0]+1 ;
                }

                //Updated for dynamic spinnerview

                if(i==0){
                    temp1[0]=tempeve[0][1];
                    temp2[0]=tempeve[0][2];
                    co1=1;
                    co2=1;
                }

                int ca=0;
                for(int j=0;j<co1;j++) {
                    if ((tempeve[i][1].equalsIgnoreCase(temp1[j]))) {
                        ca++;
                        break;
                    }

                }
                if (ca == 0) {
                    temp1[co1] = tempeve[i][1];
                    co1++;
                }

                ca=0;
                for(int j=0;j<co2;j++) {
                    if ((tempeve[i][2].equalsIgnoreCase(temp2[j]))) {
                        ca++;
                        break;
                    }

                }
                if (ca == 0) {
                    temp2[co2] = tempeve[i][2];
                    co2++;
                }







            }

            //Updated for dynamic spinnerview

            venues=new String[co1];
            cates=new String[co2];
            for(int j=0;j<co1;j++){
                venues[j]=temp1[j];
            }
            for(int j=0;j<co2;j++){
                cates[j]=temp2[j];
            }








        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void parseevents(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        netparse(response);
                        pDialog.dismiss();


                        //Updated for dynamic spinnerview


                        handlerecycle();


                        optsel();



                        optsel();//Dont delete the extra ones, its on purpose

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(UpcomingActivity.this, "Could not update schedule. Please try again.", Toast.LENGTH_LONG).show();
                        String response = fileOps.readFromFile("upcoming.txt");
                        netparse(response);
                        optsel();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(postRequest);

    }


    public void optsel(){


        //Updated for dynamic spinnerview
        if(tempeve!=null) {


            sort(tempeve, temptime, no);
        }
        RecycleList adapter = new
                RecycleList(UpcomingActivity.this, present,prtime,t,Number);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclelist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UpcomingActivity.this));
        mRecyclerView.setAdapter(adapter);
        TextView texx=(TextView) findViewById(R.id.noevent);
        if(t==0){
            texx.setText(noentrytest);
            texx.setTypeface(p);

        }
        else{
            texx.setText(null);
        }

    }


    //Updated for dynamic spinnerview

    public void handlerecycle(){
        String[] items=new String[co1+1];;//[] { "All", "Barn", "SAC","LHC","EEE" };
        String[] tempa =new String[co1+1];
        items[0]="All";
        tempa[0]="All";
        int c=1;
        if(venues!=null) {

            for (int i = 0; i < co1; i++) {
                items[i + 1] = propergram(venues[i]);
            }
        }
        spinner = (Spinner) findViewById(R.id.recyclespinner);

        spadapter = new ArrayAdapter<String>(
                this, R.layout.spinnerstyle, items);



        spinner.setAdapter(spadapter);
        spinner.setSelection(ch + 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ch = position - 1;

                //parseevents();
                optsel();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


                optsel();

            }


        });





        spinnertime = (Spinner) findViewById(R.id.recyclespinnertime);
        String[] itemstime = new String[] { "1 hour", "2 hours"};
        ArrayAdapter<String> tiadapter = new ArrayAdapter<String>(
                this, R.layout.spinnerstyle, itemstime);


        spinnertime.setAdapter(tiadapter);
        spinnertime.setSelection(timelimit - 1);
        spinnertime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch (position) {

                    case 0:
                        timelimit = 1;
                        break;

                    case 1:
                        timelimit = 2;
                        break;
                }


                //parseevents();
                optsel();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


                optsel();

            }
        });




        spinnercate = (Spinner) findViewById(R.id.recyclespinnercate);
        String[] itemscate = new String[co2+1]; //{ "All","Dance", "Music", "Shruthilaya","Dramatics","Cinematics","Fashionitas","Gaming","Arts","Hindi Lits","English Lits","Tamil Lits"};
        itemscate[0]="All";
        if(cates!=null) {
            for (int i = 0; i < co2; i++) {
                itemscate[i + 1] = propergram(cates[i]);
            }
        }

        caadapter = new ArrayAdapter<String>(
                this, R.layout.spinnerstyle, itemscate);


        spinnercate.setAdapter(caadapter);
        spinnercate.setSelection(catech+1);
        spinnercate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                catech=position-1;

                //parseevents();
                optsel();
                //Toast.makeText(getApplicationContext(), tempeve[0][2], Toast.LENGTH_SHORT).show();





            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


                optsel();

            }
        });

    }



    //Updated for dynamic spinnerview

    public String propergram(String word){


        // word.toLowerCase();
        String[] tempstr =word.split("_");
        String tempstr2;

        for(int i=0;i<tempstr.length;i++){
            if((tempstr[i].charAt(0))>='A'&&(tempstr[i].charAt(0))<='Z') {
                tempstr2 = (String.valueOf(tempstr[i].charAt(0)));
            }
            else {
                tempstr2 = (String.valueOf(tempstr[i].charAt(0))).toUpperCase();
            }
            tempstr[i] = tempstr2.concat(tempstr[i].substring(1));
            if (i != 0) {
                word=word.concat(" ").concat(tempstr[i]);
            } else {
                word = tempstr[i];
            }

        }
        return word;


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        setTitle("Upcoming Events");
        Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DATE);
        //Toast.makeText(this, String.valueOf(date),Toast.LENGTH_SHORT).show();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.ColorUpcoming));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fileOps = new Read_write_file(this);
        p= Typeface.createFromAsset(getAssets(),"fonts/gnu.ttf");
        Button b=(Button)findViewById(R.id.btn_notif);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(UpcomingActivity.this,Notify.class);
                UpcomingActivity.this.startActivity(i);


            }
        });

        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);*/

        parseevents();

        for(int i=0;i<no;i++) {

            Eve[i].name = tempeve[i][0];
            Eve[i].start_time= Time.valueOf(evstarttime[i]);
            Eve[i].end_time=Time.valueOf(evendtime[i]);

            Eve[i].venue = tempeve[i][1];
            Eve[i].cluster = tempeve[i][2];
            Eve[i].date=Date.valueOf(evdate[i]);
            Eve[i].id = id[i];
            Eve[i].last_updated=Time.valueOf(evlastupdate[i]);
            Eve[i].desc=" ";
        }


        //Deleted for dynamic spinnerview





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming, menu);
        return true;


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            parseevents();
            optsel();
            return true;
        }
        if (id == R.id.action_notif) {
            Intent i=new Intent(UpcomingActivity.this,Notify.class);
            this.startActivity(i);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}