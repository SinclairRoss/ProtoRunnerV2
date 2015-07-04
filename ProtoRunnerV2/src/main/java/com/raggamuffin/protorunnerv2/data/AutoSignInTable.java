package com.raggamuffin.protorunnerv2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AutoSignInTable extends SQLTable
{
    public AutoSignInTable(SQLiteDatabase db)
    {
        super("autoSignInTable", db);

        AddColumn(new SQLTableColumn("autoSignIn", DataType.Boolean, Constraints.None));
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
                boolean autoSignIn = cursor.getInt(cursor.getColumnIndex("autoSignIn")) > 0;

                elements.add(new AutoSignInRow(id, autoSignIn));

                if (!cursor.moveToNext())
                    break;
            }
        }

        return elements;
    }
}
