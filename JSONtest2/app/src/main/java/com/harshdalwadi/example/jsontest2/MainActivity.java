package com.harshdalwadi.example.jsontest2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView ll;
    ArrayList<Actors> arrayList;
    String response, news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<Actors>();

        new ActorsSync().execute();
    }

    public class ActorsSync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            URL u = null;
            try {
                u = new URL("http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors");
                HttpURLConnection h1 = (HttpURLConnection) u.openConnection();
                InputStream I = h1.getInputStream();
                int statusCode = h1.getResponseCode();
                if (statusCode == 200) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(I));
                    StringBuilder sb = new StringBuilder();
                    while ((news = bufferedReader.readLine()) != null) {
                        sb.append(news + "\n");
                    }
                    bufferedReader.close();
                    I.close();
                    h1.disconnect();

                    response = sb.toString().trim();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("actors");
                    Log.d("harsh", "doInBackground: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject tempobj = jsonArray.getJSONObject(i);

                        Actors actor = new Actors();

                        actor.setName(tempobj.getString("name"));
                        actor.setDescription(tempobj.getString("description"));
                        actor.setDob(tempobj.getString("dob"));
                        actor.setCountry(tempobj.getString("country"));
                        actor.setHeight(tempobj.getString("height"));
                        actor.setSpouse(tempobj.getString("spouse"));
                        actor.setChildren(tempobj.getString("children"));
                        actor.setImage(tempobj.getString("image"));

                        arrayList.add(actor);
                        Log.d("harsh", "doInBackground: " + i + arrayList.toString());
                    }

                    return true;

                } else {
                    Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                    return false;
                    //result = 0; //"Failed to fetch data!";
                }


            } catch (MalformedURLException e) {
                Log.d("harsh", "" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("harsh", "" + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("harsh", "" + e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean == false) {
                Toast.makeText(MainActivity.this, "Parsing data failed", Toast.LENGTH_LONG).show();
            } else {
                ActorsAdapter actorsAdapter = new ActorsAdapter(MainActivity.this, R.layout.row, arrayList);
                ll.setAdapter(actorsAdapter);
            }
        }

    }

}
