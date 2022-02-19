package com.example.sport.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sport.R;
import com.example.sport.record.PathRecord;

public class sport_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_main);
        Button button_1 = findViewById(R.id.run_outRoom);
        Button button_2 = findViewById(R.id.run_room);

        ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getResultCode() == 1) {
                    PathRecord pathRecord = (PathRecord) result.getData().getParcelableExtra("ahh");

                    Log.d("TAG", "onActivityResult: " + pathRecord.getDistance());

                } else if (result.getData() != null && result.getResultCode() == 2) {
                    String jj = result.getData().getStringExtra("JJ");
                    Log.d("TAG", "onActivityResult: " + jj);
                }
            }
        });
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sport_MainActivity.this, sport_Activity_OutRoom.class);
                intentActivityResultLauncher.launch(intent);
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sport_MainActivity.this, sport_Activity_Room.class);
                intentActivityResultLauncher.launch(intent);
            }
        });

    }
}