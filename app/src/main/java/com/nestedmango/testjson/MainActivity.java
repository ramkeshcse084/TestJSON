package com.nestedmango.testjson;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<CityPlace> cityList;
   // ListView listview;
    public ImageView imageview;
    public TextView tvName;

    public TextView tvStatus;
    public TextView tvCreateDone;
    public TextView tvId;
    CityPlace actor = new CityPlace();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview=(ImageView)findViewById(R.id.ivImage);
        tvName=(TextView)findViewById(R.id.tvName);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        tvCreateDone=(TextView)findViewById(R.id.tvCreateDone);
        tvId=(TextView)findViewById(R.id.tvId);

        cityList = new ArrayList<CityPlace>();
        new CityAsyntask().execute("http://api.modinagarmycity.com/api/Category");






    }




    public class CityAsyntask extends AsyncTask<String, String, List<CityPlace>> {



        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Plz wait we are loading data!!");
            progressDialog.show();
        }

        @Override
        protected List<CityPlace> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            URL url = null;
            try {
                url = new URL((String) params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }
                String data = buffer.toString();
                // JSONObject jsono = new JSONObject(data);
                JSONArray jarray = new JSONArray(data);

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject object = jarray.getJSONObject(i);


                    actor.setName(object.getString("NAME"));
                    actor.setCretedone(object.getString("CREATEDON"));
                    actor.setId(object.getString("ID"));
                    actor.setStatus(object.getString("STATUS"));
                    actor.setImage(object.getString("IMAGEURL"));

                    cityList.add(actor);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(List<CityPlace> result) {
            super.onPostExecute(result);

          String name=actor.getName();
            tvName.setText(name);
           String createdone=actor.getCretedone();
            tvCreateDone.setText(createdone);
            String id=actor.getId();
            tvId.setText(id);
            String status=actor.getStatus();
            tvStatus.setText(status);
            String img=actor.getImage();
            imageview.setImageResource(R.drawable.flower);
            new DownloadImageTask(imageview).execute(img);

            progressDialog.cancel();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }



}
