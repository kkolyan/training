package net.kkolyan.trainingdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import net.kkolyan.spring.altimpl.container.Container;
import net.kkolyan.trainingdroid.dev.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainActivity extends Activity {
    private Container application;
    private Preferences preferences = new Preferences();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        
        preferences.load();

//        showServerPrompt();
        navigate(preferences.getLocation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic, menu);
        return true;
    }

    private void startServer() {
        if (application != null) {
            return;
        }

        try {
            Properties filter = new Properties();

            InputStream filterProps = getClass().getClassLoader().getResourceAsStream("filter.properties");
            if (filterProps == null) {
                throw new FileNotFoundException("classpath:filter.properties");
            }
            filter.load(filterProps);
            String dataDirectory = filter.getProperty("dataDirectory");

            extractTo(getClass().getResource("library.xml"), dataDirectory);

            application =  new Container("weedyweb-mini.xml", "app.xml", "filter.properties");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void extractTo(URL resource, String directory) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        new File(directory).mkdirs();
        
        String fileName = resource.toString();
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }

        InputStream in = resource.openStream();
        try {
            OutputStream out = new FileOutputStream(new File(directory, fileName));

            try {
                byte[] bytes = new byte[1024];
                for (int n = in.read(bytes); n >= 0; n = in.read(bytes)) {
                        out.write(bytes, 0, n);
                    }
                out.flush();
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void stopServer() {
        if (application == null) {
            return;
        }
        try {
            application.shutdown();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString(), e);
        }
        application = null;
    }

    private void reloadPage() {
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.clearCache(true);
        webView.reload();
    }

    private void navigate(String address) {
        if (address == null) {
            throw new NullPointerException("address");
        }
        try {
            boolean local = new URL(address).getHost().equals("localhost");
            if (local) {
                startServer();
            } else {
                stopServer();
            }

            WebView webView = (WebView) findViewById(R.id.webView);
            webView.loadUrl(address);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void showAbout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("About");
        Scanner scanner = new Scanner(getClassLoader().getResourceAsStream("version"));
        try {
            alert.setMessage(scanner.nextLine());
        } finally {
            scanner.close();
        }
        alert.show();
    }

    private void showServerPrompt() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Select Server");

        // Set an EditText view to get user input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        alert.setView(layout);

        final EditText input = new EditText(this);
        layout.addView(input);

        Spinner spinner = new Spinner(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, preferences.getServers());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input.setText(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        layout.addView(spinner);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!preferences.getServers().contains(input.getText().toString())) {
                    preferences.getServers().add(input.getText().toString());
                    preferences.save();
                }

                navigate(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_server:
                showServerPrompt();
                return true;
            case R.id.clear_cache:
                reloadPage();
                return true;
            case R.id.keep_screen_on:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                return true;
            case R.id.keep_screen_off:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                return true;
            case R.id.open_stat:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:8080/stat")));
                return true;
            case R.id.update_app:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getUpdateUrl())));
                return true;
            case R.id.about:
                showAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        WebView webView = (WebView) findViewById(R.id.webView);
        preferences.setLocation(webView.getUrl());
        preferences.save();

        super.onDestroy();
        stopServer();
    }
}
