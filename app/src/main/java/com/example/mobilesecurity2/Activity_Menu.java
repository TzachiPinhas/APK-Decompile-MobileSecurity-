package com.example.mobilesecurity2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Activity_Menu extends AppCompatActivity {
    private MaterialButton menu_BTN_start;
    /* access modifiers changed from: private */
    public TextInputEditText menu_EDT_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_menu);
        findViews();
        initViews();
    }

    private void initViews() {
        this.menu_BTN_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Activity_Menu.this.makeServerCall();
            }
        });
    }

    private void findViews() {
        this.menu_BTN_start = (MaterialButton) findViewById(R.id.menu_BTN_start);
        this.menu_EDT_id = (TextInputEditText) findViewById(R.id.menu_EDT_id);
    }

    /* access modifiers changed from: private */
    public void makeServerCall() {
        new Thread() {
            public void run() {
                String data = Activity_Menu.getJSON(Activity_Menu.this.getString(R.string.url));
                Log.d("pttt", data);
                if (data != null) {
                    Activity_Menu activity_Menu = Activity_Menu.this;
                    activity_Menu.startGame(activity_Menu.menu_EDT_id.getText().toString(), data);
                }
            }
        }.start();
    }


    public void startGame(String id, String data) {
        // Check if the ID is at least 8 characters long
        if (id == null || id.length() < 8) {
            Log.e("Error", "ID must be at least 8 characters long.");
            return;
        }
        int index;
        try {
            index = Integer.parseInt(String.valueOf(id.charAt(7)));
        } catch (NumberFormatException e) {
            return;
        }

        String[] dataArray = data.split(",");
        if (index >= dataArray.length) {
            Log.e("Error", "Invalid index from ID's 8th character.");
            return;
        }

        String state = dataArray[index];
        Intent intent = new Intent(getBaseContext(), Activity_Game.class);
        intent.putExtra(Activity_Game.EXTRA_ID, id);
        intent.putExtra(Activity_Game.EXTRA_STATE, state);
        startActivity(intent);
    }


    public static String getJSON(String url) {
        String data = "";
        HttpsURLConnection con = null;
        try {
            HttpsURLConnection con2 = (HttpsURLConnection) new URL(url).openConnection();
            con2.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = br.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            br.close();
            data = sb.toString();
            if (con2 != null) {
                try {
                    con2.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (MalformedURLException ex2) {
            ex2.printStackTrace();
            if (con != null) {
                con.disconnect();
            }
        } catch (IOException ex3) {
            ex3.printStackTrace();
            if (con != null) {
                con.disconnect();
            }
        } catch (Throwable th) {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex4) {
                    ex4.printStackTrace();
                }
            }
            throw th;
        }
        return data;
    }
}
