package com.raggamuffin.protorunnerv2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OfflineHighScoreTable extends SQLTable
{
    public OfflineHighScoreTable(SQLiteDatabase db)
    {
        super("offlineHighScoreTable", db);

        AddColumn(new SQLTableColumn("highScore", DataType.Integer, Constraints.None));
        CreateTable();
    }

    @Override
    public ArrayList<TableRow> Read()
    {
        String sqlQuery = "select * from " + m_Name + ";";
        Cursor cursor = m_Database.rawQuery(sqlQuery, new String[] {});

        ArrayList<TableRow> elements = new ArrayList<TableRow>();

        if(cursor.moveToFirst())
        {
            while (true)
            {
                int id      = cursor.getInt(cursor.getColumnIndex("id"));
                int score   = cursor.getInt(cursor.getColumnIndex("highScore"));

                elements.add(new OfflineHighScoreRow(id, score));

                if (!cursor.moveToNext())
                    break;
            }
        }

        return elements;
    }
}
