package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.data.DataType;
import com.raggamuffin.protorunnerv2.data.ExtraInformation;
import com.raggamuffin.protorunnerv2.data.HighScoreRow;
import com.raggamuffin.protorunnerv2.data.HighScoreTable;
import com.raggamuffin.protorunnerv2.data.SQLTable;
import com.raggamuffin.protorunnerv2.data.SQLTableColumn;
import com.raggamuffin.protorunnerv2.data.TableRow;
import com.raggamuffin.protorunnerv2.data.TableType;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager 
{
	private final String DATABASE_NAME = "protodb";
	private GameLogic m_Game;
	
	SQLiteDatabase m_Database;
	
	private HighScoreTable m_HighScoreTable;
	
	public DatabaseManager(GameLogic game)
	{
		m_Game = game;

		m_Database = m_Game.GetContext().openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		
		m_HighScoreTable = new HighScoreTable();
		AddTable(m_HighScoreTable);

		m_HighScoreTable.CleanTable(m_Database);
	}
	
	public ArrayList<TableRow> GetTableData(TableType table)
	{
		switch(table)
		{
			case Highscore:
				return m_HighScoreTable.Read(m_Database);
		}
		
		return new ArrayList<TableRow>();	// Return an empty array list.
	}
	
	public void AddToTable(TableType table, TableRow row)
	{
		switch(table)
		{
			case Highscore:
				m_HighScoreTable.Insert(m_Database, row);
				break;	
		}
	}
	
	private void AddTable(SQLTable table)
	{
		String sqlQuery = "create table if not exists " + table.GetName() + " (";
		
		ArrayList<SQLTableColumn> columns = table.GetColumns();
		
		for(SQLTableColumn column : columns)
		{
			sqlQuery += column.GetName() + " " + GetDataTypeString(column.GetDataType()) + " " + GetExtraInfoString(column.GetExtraInformation()) + " ";
			
			if(columns.indexOf(column) != columns.size() - 1)
				sqlQuery += ",";
			else
				sqlQuery += ");";	
		}

		m_Database.execSQL(sqlQuery);
	}
	
	private String GetDataTypeString(DataType type)
	{
		switch(type)
		{
			case Integer:
				return "integer";
				
			case String:
				return "varchar";
			
			case Float:
				return "float";
			
			case Boolean:
				return "boolean";
		}
		
		return "";
	}
	
	private String GetExtraInfoString(ExtraInformation info)
	{
		switch(info)
		{
			case None:
				return "";
				
			case PrimaryKey:
				return "primary key autoincrement";
				
			case AutoIncrement:
				return "autoincrement";
				
			case NotNull:
				return "not null";
		}
		
		return "";
	}
}
