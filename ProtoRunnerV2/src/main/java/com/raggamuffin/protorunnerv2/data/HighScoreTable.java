package com.raggamuffin.protorunnerv2.data;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HighScoreTable extends SQLTable
{
	public static final int MAX_HIGH_SCORES = 6;
	
	public HighScoreTable() 
	{
		super("highScoreTable");
		
		AddColumn(new SQLTableColumn("name",  DataType.String,  ExtraInformation.None));
		AddColumn(new SQLTableColumn("score", DataType.Integer, ExtraInformation.None));
		AddColumn(new SQLTableColumn("play_time", DataType.Float, ExtraInformation.None));
		AddColumn(new SQLTableColumn("reboots", DataType.Integer, ExtraInformation.None));
		AddColumn(new SQLTableColumn("shots_fired", DataType.Integer, ExtraInformation.None));
		AddColumn(new SQLTableColumn("shots_landed", DataType.Integer, ExtraInformation.None));
		AddColumn(new SQLTableColumn("wingman_a_life", DataType.Float, ExtraInformation.None));
		AddColumn(new SQLTableColumn("wingman_b_life", DataType.Float, ExtraInformation.None));
	}

	@Override
	public ArrayList<TableRow> Read(SQLiteDatabase db) 
	{
		String sqlQuery = "select * from " + m_Name + " order by score desc limit " + MAX_HIGH_SCORES;
		Cursor cursor = db.rawQuery(sqlQuery, new String[] {});
		
		ArrayList<TableRow> elements = new ArrayList<TableRow>();
		
		if(cursor.moveToFirst())
		{
			while(true)
			{
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int score = cursor.getInt(cursor.getColumnIndex("score"));
				float playTime = cursor.getInt(cursor.getColumnIndex("play_time"));
				int reboots = cursor.getInt(cursor.getColumnIndex("reboots"));
				int shotsFired = cursor.getInt(cursor.getColumnIndex("shots_fired"));
				int shotsLanded = cursor.getInt(cursor.getColumnIndex("shots_landed"));
				float aLife = cursor.getInt(cursor.getColumnIndex("wingman_a_life"));
				float bLife = cursor.getInt(cursor.getColumnIndex("wingman_b_life"));
				
				elements.add(new HighScoreRow(id, name, score, playTime, reboots, shotsFired, shotsLanded, aLife, bLife));
				
				if(!cursor.moveToNext())
					break;
			}
		}
		
		return elements;
	}
	
	public void CleanTable(SQLiteDatabase db)
	{
		ArrayList<TableRow> rows = Read(db);
		
		this.ClearTable(db);
		
		for(TableRow row : rows)
		{
			PreserveIDInsert(db, row);
		}
	}
}
