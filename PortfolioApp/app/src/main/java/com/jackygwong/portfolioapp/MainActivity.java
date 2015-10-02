package com.jackygwong.portfolioapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.popular_movies_button)
    Button popular_movies_button;
    @Bind(R.id.scores_button)
    Button scores_button;
    @Bind(R.id.library_button)
    Button library_button;
    @Bind(R.id.build_it_bigger_button)
    Button build_it_bigger_button;
    @Bind(R.id.xyz_reader_button)
    Button xyz_button;
    @Bind(R.id.capstone_button)
    Button capstone_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.popular_movies_button, R.id.scores_button, R.id.library_button, R.id.build_it_bigger_button, R.id.xyz_reader_button, R.id.capstone_button})
    public void button_click(Button button) {
        int buttonId = button.getId();
        switch (buttonId) {
            case R.id.popular_movies_button:
                Toast.makeText(getApplicationContext(), "This button will launch my popular movies app!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.scores_button:
                Toast.makeText(getApplicationContext(), "This button will launch my scores app!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.library_button:
                Toast.makeText(getApplicationContext(), "This button will launch my library app!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.build_it_bigger_button:
                Toast.makeText(getApplicationContext(), "This button will launch my build it bigger app!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.xyz_reader_button:
                Toast.makeText(getApplicationContext(), "This button will launch my xyz reader app!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.capstone_button:
                Toast.makeText(getApplicationContext(), "This button will launch my capstone app!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
