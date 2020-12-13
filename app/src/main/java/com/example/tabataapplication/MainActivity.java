package com.example.tabataapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tabataapplication.Adapters.SeqDataAdapter;
import com.example.tabataapplication.DatabaseHelper.DatabaseAdapter;
import com.example.tabataapplication.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.tabataapplication.Models.Sequence;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Sequence> sequenceList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button btnSeqAdd;
    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.seqList);
        btnSeqAdd = findViewById(R.id.btnSeqAdd);

        databaseAdapter = new DatabaseAdapter(MainActivity.this);
        databaseAdapter.open();
        sequenceList = databaseAdapter.getSequences();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SeqDataAdapter seqDataAdapter = new SeqDataAdapter(this, sequenceList);
        recyclerView.setAdapter(seqDataAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(seqDataAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        btnSeqAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                String basicSequenceTitle = "Sequence " + String.valueOf(databaseAdapter.getCountSequence() + 1);
                long idSeq = databaseAdapter.insertSequence(new Sequence(basicSequenceTitle, Color.WHITE));
                intent.putExtra("idSeq", idSeq);
                databaseAdapter.close();
                finish();
                startActivity(intent);
            }
        });
    }
}
