package com.example.voting;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.database.Poll;
import com.example.voting.database.VotingDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final List<Poll> data = new ArrayList<Poll>();
    private VotingDatabaseHandler db;
    private final PollAdapter adapter = new PollAdapter();
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new VotingDatabaseHandler(getApplicationContext());
        Window window = getWindow();
        Drawable background = AppCompatResources.getDrawable(MainActivity.this, R.drawable.gradient_bg);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.white));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        ProgressBar progress = findViewById(R.id.progressBar1);
        Objects.requireNonNull(getSupportActionBar()).hide();
        rv = findViewById(R.id.polls_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        rv.setAdapter(adapter);
        registerForContextMenu(rv);
        progress.setVisibility(View.GONE);
        adapter.setOnItemClickListener(new PollAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Poll model) {
                // after clicking on item of recycler view
                // we are opening a new activity and passing
                // a data to our activity.
                progress.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            Intent intent = new Intent(MainActivity.this, VoteActivity.class);
                            intent.putExtra("edit_state", "editable");
                            intent.putExtra("id", model.getId());
                            startActivity(intent);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.GONE);
                                }
                            });
                            Log.i("TAG", "onItemClick: id -> " + model.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openVoteActivityIntent = new Intent(getApplicationContext(), VoteActivity.class);
                openVoteActivityIntent.putExtra("edit_state", "not_editable");
                startActivity(openVoteActivityIntent);
            }
        });

        Button searchBtn = (Button) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        search();
    }

    private void search() {
        EditText searchTxtView = (EditText) findViewById(R.id.search_edit_text);
        String searchTxt = searchTxtView.getText().toString();

        data.clear();
        // get data from db
        Cursor cursor;

        if (searchTxt.isEmpty()) {
            cursor = db.getPolls();
        } else {
            cursor = db.searchPolls(searchTxt);
        }
        if (cursor != null) {
            do {
                Log.i("TAG", "search: " + cursor.getString(1));
                data.add(new Poll(cursor.getString(1), cursor.getString(2), cursor.getInt(0)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.closeDB();


        adapter.notifyDataSetChanged();
        adapter.submitList(data);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_menu) {

            int position = -1;
            position = adapter.getPosition();
            try {
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage(), e);
                return super.onContextItemSelected(item);
            }

            db.deletePoll(data.get(position).getId());
            search();
        }
        return true;
    }

    private void hideSystemUI() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
            WindowInsetsControllerCompat windowInsetsController =
                    WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.systemGestures());
        } catch (Exception e) {
            Log.i(TAG, "hideSystemUI: exception -> $e");
        }
    }
}