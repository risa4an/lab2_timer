package com.example.tabataapplication.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;

import androidx.core.content.ContextCompat;

import com.example.tabataapplication.Action;
import com.example.tabataapplication.Models.Phase;
import com.example.tabataapplication.Models.Sequence;
import com.example.tabataapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open() {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private Cursor getAllEntries(String tableName) {
        Cursor cursor;
        String[] phase_columns = new String[]{DatabaseHelper.PHASE_ID, DatabaseHelper.FK_SEQUENCE_ID,
                DatabaseHelper.ACTION_NAME, DatabaseHelper.TIME, DatabaseHelper.DESCRIPTION, DatabaseHelper.SETS_AMOUNT};
        String[] sequence_columns = new String[]{DatabaseHelper.SEQUENCE_ID, DatabaseHelper.TITLE, DatabaseHelper.COLOUR};
        switch (tableName) {
            case "phases":
                cursor = database.query(DatabaseHelper.PHASES_TABLE, phase_columns,
                        null, null, null, null, null);
                break;
            case "sequences":
                cursor = database.query(DatabaseHelper.SEQUENCE_TABLE, sequence_columns,
                        null, null, null, null, null);
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    public List<Sequence> getSequences() {
        ArrayList<Sequence> sequences = new ArrayList<>();
        Cursor cursor = getAllEntries("sequences");
        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SEQUENCE_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
                int colour = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLOUR));
                sequences.add(new Sequence(id, title, colour));
            }
            while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return sequences;
    }

    public List<Phase> getPhases() {
        ArrayList<Phase> phases = new ArrayList<>();
        Cursor cursor = getAllEntries("phases");
        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PHASE_ID));
                int id_sequence = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FK_SEQUENCE_ID));
                String actionName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_NAME));
                int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIME));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION));
                int setsAmount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SETS_AMOUNT));
                Drawable actionImage = chooseImage(actionName);
                phases.add(new Phase(id, id_sequence, Phase.stringToEnumValue(actionName), time, description, actionImage, setsAmount));
            }
            while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return phases;
    }

    public List<Phase> getPhasesOfSequence(long idSeq) {
        ArrayList<Phase> phases = new ArrayList<>();
        String[] columns = new String[]{DatabaseHelper.PHASE_ID, DatabaseHelper.FK_SEQUENCE_ID,
                DatabaseHelper.ACTION_NAME, DatabaseHelper.TIME, DatabaseHelper.DESCRIPTION, DatabaseHelper.SETS_AMOUNT};
        String selection = DatabaseHelper.FK_SEQUENCE_ID + " == ?";
        String[] selectionArgs = new String[]{String.valueOf(idSeq)};
        Cursor cursor = database.query(DatabaseHelper.PHASES_TABLE, columns, selection, selectionArgs, null, null, null);
        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PHASE_ID));
                int id_sequence = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FK_SEQUENCE_ID));
                String actionName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_NAME));
                int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIME));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION));
                int setsAmount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SETS_AMOUNT));
                Drawable actionImage = chooseImage(actionName);
                phases.add(new Phase(id, id_sequence, Phase.stringToEnumValue(actionName), time, description, actionImage, setsAmount));
            }
            while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return phases;
    }

    private Drawable chooseImage(String actionName) {
        Action action = Phase.stringToEnumValue(actionName);
        Drawable result;
        switch (action) {
            case PREPARATION:
                result = context.getResources().getDrawable(R.drawable.ic_preparation_color);
                break;
            case WORK:
                result = context.getResources().getDrawable(R.drawable.ic_work_color);
                break;
            case RELAX:
                result = context.getResources().getDrawable(R.drawable.ic_relax_color);
                break;
            case RELAX_BETWEEN_SETS:
                result = context.getResources().getDrawable(R.drawable.ic_relax_between_sets_color);
                break;
            default:
                result = null;
        }
        return result;
    }

    public long getCountSequence() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.SEQUENCE_TABLE);
    }

    public long getCountPhase() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.PHASES_TABLE);
    }

    public Sequence getSequence(int id) {
        Sequence sequence = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.SEQUENCE_TABLE, DatabaseHelper.SEQUENCE_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            int colour = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLOUR));
            sequence = new Sequence(id, title, colour);
        }
        cursor.close();
        return sequence;
    }

    public Phase getPhase(int id) {
        Phase phase = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.PHASES_TABLE, DatabaseHelper.PHASE_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            int id_sequence = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FK_SEQUENCE_ID));
            String actionName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_NAME));
            int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TIME));
            String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION));
            int setsAmount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SETS_AMOUNT));
            Drawable actionImage = chooseImage(actionName);
            phase = new Phase(id, id_sequence, Phase.stringToEnumValue(actionName), time, description, actionImage, setsAmount);
        }
        cursor.close();
        return phase;
    }

    public long insertSequence(Sequence sequence) {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TITLE, sequence.getTitle());
        cv.put(DatabaseHelper.COLOUR, sequence.getColour());

        return database.insert(DatabaseHelper.SEQUENCE_TABLE, null, cv);
    }

    public long insertPhase(Phase phase) {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.FK_SEQUENCE_ID, phase.getId_sequence());
        cv.put(DatabaseHelper.ACTION_NAME, phase.getActionName());
        cv.put(DatabaseHelper.TIME, phase.getTime());
        cv.put(DatabaseHelper.DESCRIPTION, phase.getDescription());
        cv.put(DatabaseHelper.SETS_AMOUNT, phase.getSetsAmount());

        return database.insert(DatabaseHelper.PHASES_TABLE, null, cv);
    }

    public long deleteSequence(long sequenceId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(sequenceId)};
        return database.delete(DatabaseHelper.SEQUENCE_TABLE, whereClause, whereArgs);
    }

    public long deletePhase(long phaseId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(phaseId)};
        return database.delete(DatabaseHelper.PHASES_TABLE, whereClause, whereArgs);
    }

    public long updateSequence(Sequence sequence) {

        String whereClause = DatabaseHelper.SEQUENCE_TABLE + "=" + String.valueOf(sequence.getId());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TITLE, sequence.getTitle());
        cv.put(DatabaseHelper.COLOUR, sequence.getColour());
        return database.update(DatabaseHelper.SEQUENCE_TABLE, cv, whereClause, null);
    }

    public long updatePhase(Phase phase) {

        String whereClause = DatabaseHelper.PHASES_TABLE + "=" + String.valueOf(phase.getId());
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.FK_SEQUENCE_ID, phase.getId_sequence());
        cv.put(DatabaseHelper.ACTION_NAME, phase.getActionName());
        cv.put(DatabaseHelper.TIME, phase.getTime());
        cv.put(DatabaseHelper.DESCRIPTION, phase.getDescription());
        cv.put(DatabaseHelper.SETS_AMOUNT, phase.getSetsAmount());

        return database.update(DatabaseHelper.PHASES_TABLE, cv, whereClause, null);
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.SEQUENCE_TABLE, null, null);
        database.delete(DatabaseHelper.PHASES_TABLE, null, null);
    }
}
