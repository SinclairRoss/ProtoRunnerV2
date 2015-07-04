package com.raggamuffin.protorunnerv2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class TutorialOfferedTable extends SQLTable
{
    public TutorialOfferedTable(SQLiteDatabase db)
    {
        super("tutorialOfferedTable", db);

        AddColumn(new SQLTableColumn("tutorialOffered", DataType.Boolean, Constraints.None));
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
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                boolean hasPlayed = cursor.getInt(cursor.getColumnIndex("tutorialOffered")) > 0;

                elements.add(new TutorialOfferedRow(id, hasPlayed));

                if (!cursor.moveToNext())
                    break;
            }
        }

        return elements;
    }
}
