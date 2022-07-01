package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //debug tool
    private static final String TAG = MainActivity.class.getSimpleName();

    EditText searchET;
    EditText locET;
    TextView countTextView;
    Button jobsearchButton;

    String jobString;
    String joblocString;
    String jobcountString;

    //progress bar
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    RecyclerView jobRecyclerView;
    List<JobItem_foratyourservice> JobItemList;
    Adapter_forAYSpage adapter;

    private Handler textHandler = new Handler();
    private differentThread dt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchET = findViewById(R.id.editTextJobSearch);
        locET = findViewById(R.id.editTextJobLocation);
        countTextView = findViewById(R.id.textView_job_count);
        jobsearchButton = findViewById(R.id.job_search_button);

        progressDialog = getDialogProgressBar().create();

        //recyclerview
        //Instantiate the arraylist
        JobItemList = new ArrayList<>();



        //Instantiate a adapter
        adapter = new Adapter_forAYSpage(JobItemList, this);
        jobRecyclerView = findViewById(R.id.jobRecyclerView);

        // Checks the orientation of the screen
        jobRecyclerView.setHasFixedSize(true);
        //This defines the way in which the RecyclerView is oriented
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //You can add the divider line between rows by using DividerItemDecoration provided by support library
        jobRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        jobRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Associates the adapter with the RecyclerView
        jobRecyclerView.setAdapter(adapter);
        jobcountString = "Job Result Count: "+String.valueOf(adapter.getItemCount());
        countTextView.setText(jobcountString);
        locET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    jobsearchButton.performClick();
                    return true;
                }
                return false;
            }
        });



        jobRecyclerView = findViewById(R.id.jobRecyclerView);
        jobRecyclerView.addOnItemTouchListener(new RecyclerTouchListener_forlinkcollector(getApplicationContext(), jobRecyclerView, new RecyclerTouchListener_forlinkcollector.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String websiteStr = JobItemList.get(position).getWebsite();
                if(!(websiteStr.startsWith("https://")||websiteStr.startsWith("http://"))){
                    websiteStr ="http://"+websiteStr;
                }


                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(websiteStr));
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }


        }));




        //jobsearch Button Callback function
        jobsearchButton = findViewById(R.id.job_search_button);
        jobsearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !searchET.getText().toString().isEmpty() &&!locET.getText().toString().isEmpty()){
                    jobString = searchET.getText().toString();
                    joblocString = locET.getText().toString();
                }
                else{
                    jobString = "";
                    joblocString= "";
                }

                if(dt1==null){
                    progressDialog.show();
                    dt1 = new differentThread();
                    dt1.start();
                    dt1 = null;
                }



            }
        });




    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if(jobString!=null||joblocString!=null||jobcountString!=null){
            searchET.setText(jobString);
            locET.setText(joblocString);
            countTextView.setText(jobcountString);
        }

        // Checks the orientation of the screen
        jobRecyclerView.setHasFixedSize(true);
        //This defines the way in which the RecyclerView is oriented
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //You can add the divider line between rows by using DividerItemDecoration provided by support library
        jobRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //Associates the adapter with the RecyclerView
        jobRecyclerView.setAdapter(adapter);


    }



    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("Loading job results...");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            progressBar.setPadding(0, 0, 0, 30);
            builder.setView(progressBar);
        }
        return builder;
    }

    //Class which extends the Thread class.
    private class differentThread extends Thread {
        boolean isFinish = false;
        @Override
        public void run() {


            // Create a neat value object to hold the URL
            try {
                URL url = new URL("https://jooble.org/api/986e724b-424c-4fec-ba9b-8eff8a7c8670");
                // Open a connection(?) on the URL(??) and cast the response(???)
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Now it's "open", we can set the request method, headers etc.
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String jsonInputString = "{ keywords: '"+jobString+"', location: '"+joblocString+"'}";
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jobject = new JSONObject(response.toString());
                    JSONArray jsa = new JSONArray(jobject.get("jobs").toString());
                    JobItemList.clear();
                    for(int i = 0; i < jsa.length(); i++)
                    {
                        JSONObject objects = jsa.getJSONObject(i);
                        //Iterate through the elements of the array i.
                        String title = objects.get("title").toString();
                        if(!title.isEmpty()){
                            Log.i(TAG, "Debug by Qia Lin: title"+title);
                            String link = objects.get("link").toString();
                            String company = objects.get("company").toString();
                            String location = objects.get("location").toString();
                            JobItemList.add(new JobItem_foratyourservice(title,company,location,link));

                        }


                    }


                }
                catch (IOException | JSONException e) {
                    Log.i(TAG, "Debug by Qia Lin: "+e);
                }






                textHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        jobcountString = "Job Result Count: "+String.valueOf(adapter.getItemCount())+"\nClick job to apply on website";
                        countTextView.setText(jobcountString);
                        progressDialog.dismiss();


                    }
                });





            } catch (MalformedURLException e) {
                Log.i(TAG, "Debug by Qia Lin: "+e);
            } catch (IOException e) {
                Log.i(TAG, "Debug by Qia Lin: "+e);
            }


        }

        public void setFinish(boolean finish) {
            isFinish = finish;
        }
        public boolean getFinish() {
            return isFinish;
        }

    }









}