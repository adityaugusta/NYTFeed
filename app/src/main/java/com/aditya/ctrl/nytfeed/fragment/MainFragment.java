package com.aditya.ctrl.nytfeed.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aditya.ctrl.nytfeed.R;
import com.aditya.ctrl.nytfeed.adapter.AdapterView;
import com.aditya.ctrl.nytfeed.adapter.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends Fragment {

    private ProgressDialog pDialog;
    private CustomAdapter customAdapter;
    private AdapterView adapterView;
    private RecyclerView recyclerView;
    private int totalScrolled = 0;

    ArrayList<HashMap<String, String>> arraylist;

    // SHOW IN LISTVIEW
    public static String URL = "url";
    public static String TITLE = "title";
    public static String ABS = "abs";
    public static String SOURCE = "source";
    public static String PUB_DATE = "pubdate";
    public static String IMG = "imgurl";

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        pDialog = new ProgressDialog(getContext());

        fetchData();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        pDialog.setMessage("Fetching data...");
        pDialog.setCancelable(false);
        pDialog.show();
        FetchNews weatherTask = new FetchNews();
        weatherTask.execute();
    }
    private void updateData() {
        pDialog.setMessage("Refreshing...");
        pDialog.setCancelable(false);
        pDialog.show();
        FetchNews weatherTask = new FetchNews();
        weatherTask.execute();
    }

    public class FetchNews extends AsyncTask<String, Void, String[]> {

        private String[] getDataFromJson(String JsonStr) throws JSONException {

            final String NYT_RESULTS = "results";
            final String NYT_MEDIA = "media";
            final String NYT_METADATA = "media-metadata";
            final String NYT_URL = "url";
            final String NYT_TITLE = "title";
            final String NYT_ABSTRACT = "abstract";
            final String NYT_SOURCE = "source";
            final String NYT_PUBLISHED_DATE = "published_date";
            final String NYT_URLIMAGE = "url";

            arraylist = new ArrayList<>();

            JSONObject newsJson = new JSONObject(JsonStr);
            JSONArray newsArray = newsJson.getJSONArray(NYT_RESULTS);

            for (int i = 0; i < newsArray.length(); i++) {
                HashMap<String, String> map = new HashMap<>();
                String url;
                String title;
                String abstractX;
                String source;
                String pub_date;

                String img_url;

                JSONObject itemResults = newsArray.getJSONObject(i);
                JSONObject resultObject = itemResults.getJSONArray(NYT_MEDIA).getJSONObject(0);

                url = itemResults.getString(NYT_URL);
                title = itemResults.getString(NYT_TITLE);
                abstractX = itemResults.getString(NYT_ABSTRACT);
                source = itemResults.getString(NYT_SOURCE);
                pub_date = itemResults.getString(NYT_PUBLISHED_DATE);

                JSONObject itemMedia = resultObject.getJSONArray(NYT_METADATA).getJSONObject(0);
                img_url = itemMedia.getString(NYT_URLIMAGE);

                map.put("url", url);
                map.put("title", title);
                map.put("abs", abstractX);
                map.put("source", source);
                map.put("pubdate", pub_date);
                map.put("imgurl", img_url);

                arraylist.add(map);

            }
            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;

            try {
                String api = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/";
                String time = "1";
                String type = ".json?";
                String key = "api-key=0b3b1b87b9591eea4a2e68939361ab28%3A11%3A75047606";
                String data_url = api + time + type + key;

                URL url = new URL(data_url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) { return null; }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {  buffer.append(line + "\n"); }
                if (buffer.length() == 0) { return null; }
                JsonStr = buffer.toString();
            }

            catch (IOException e) { return null; }

            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try { reader.close(); }
                    catch (final IOException e) {}
                }
            }

            try {  return getDataFromJson(JsonStr); }
            catch (JSONException e) { e.printStackTrace(); }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            adapterView = new AdapterView(getActivity(), arraylist);
            recyclerView.setAdapter(adapterView);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

    }
}
