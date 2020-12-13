package com.example.tabataapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.tabataapplication.Adapters.EditDataAdapter;
import com.example.tabataapplication.DatabaseHelper.DatabaseAdapter;
import com.example.tabataapplication.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.tabataapplication.Models.Phase;
import com.example.tabataapplication.databinding.ActivityEditBinding;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    ActivityEditBinding binding;
    boolean isRotate = false;
    List<Phase> list = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    DatabaseAdapter databaseAdapter;

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.addSets) {
//
//        }
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        final long idSeq = intent.getLongExtra("idSeq", 1);
        databaseAdapter = new DatabaseAdapter(EditActivity.this);
        databaseAdapter.open();
        list = databaseAdapter.getPhasesOfSequence(idSeq);

        layoutManager = new LinearLayoutManager(this);
        binding.editList.setLayoutManager(layoutManager);
        final EditDataAdapter editDataAdapter = new EditDataAdapter(this, list);
        binding.editList.setAdapter(editDataAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(editDataAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.editList);

        binding.fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(v);
            }
        });
        binding.fabPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdapter.insertPhase(new Phase((int) idSeq, Action.PREPARATION, 5, "", 1));
                finish();
                startActivity(getIntent());
                animation(v);
            }
        });
        binding.fabWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdapter.insertPhase(new Phase((int) idSeq, Action.WORK, 10, "", 1));
                animation(v);
                finish();
                startActivity(getIntent());
            }
        });
        binding.fabRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdapter.insertPhase(new Phase((int) idSeq, Action.RELAX, 3, "", 1));
                animation(v);
                finish();
                startActivity(getIntent());
            }
        });
        binding.fabRelaxBetweenSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdapter.insertPhase(new Phase((int) idSeq, Action.RELAX_BETWEEN_SETS, 2, "", 1));
                animation(v);
                finish();
                startActivity(getIntent());
            }
        });

        ViewAnimation.init(binding.fabPrep);
        ViewAnimation.init(binding.fabWork);
        ViewAnimation.init(binding.fabRelax);
        ViewAnimation.init(binding.fabRelaxBetweenSets);
    }

    private void animation(View v) {
        isRotate = ViewAnimation.rotateFab(v, !isRotate);
        if (isRotate) {
            showIn();
        } else {
            showOut();
        }
    }

    private void showIn() {
        ViewAnimation.showIn(binding.fabPrep);
        ViewAnimation.showIn(binding.fabWork);
        ViewAnimation.showIn(binding.fabRelax);
        ViewAnimation.showIn(binding.fabRelaxBetweenSets);
    }

    private void showOut() {
        ViewAnimation.showOut(binding.fabPrep);
        ViewAnimation.showOut(binding.fabWork);
        ViewAnimation.showOut(binding.fabRelax);
        ViewAnimation.showOut(binding.fabRelaxBetweenSets);
    }
}