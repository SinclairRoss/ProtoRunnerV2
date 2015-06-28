package com.raggamuffin.protorunnerv2.data;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLTable
{
	protected String m_Name;
	private ArrayList<SQLTableColumn> m_Columns;
	
	public SQLTable(String name) 
	{
		m_Name = name;
		m_Columns = new ArrayList<SQLTableColumn>();
		
		AddColumn(new SQLTableColumn("id", DataType.Integer, ExtraInformation.PrimaryKey));
	}

	public void AddColumn(SQLTableColumn column)
	{
		m_Columns.add(column);
	}
	
	public String GetName()
	{
		return m_Name;
	}
	
	public ArrayList<SQLTableColumn> GetColumns()
	{
		return m_Columns;
	}
	
	public void Insert(SQLiteDatabase db, TableRow row)
	{
		String sqlQuery = "insert into " + m_Name + "(";

		for(SQLTableColumn column : m_Columns)
		{
			if(column.GetName() == "id")
				continue;
			
			sqlQuery += column.GetName();
			
			if(m_Columns.indexOf(column) != m_Columns.size() - 1)
				sqlQuery += ", ";
			else		
				sqlQuery += ") ";
		}
		
		sqlQuery += "values(" + row.GenerateValuesString() + ");";
		
		db.execSQL(sqlQuery);
	}
	
	public void PreserveIDInsert(SQLiteDatabase db, TableRow row)
	{
		String sqlQuery = "insert into " + m_Name + "(id,";

		for(SQLTableColumn column : m_Columns)
		{
			if(column.GetName() == "id")
				continue;
			
			sqlQuery += column.GetName();
			
			if(m_Columns.indexOf(column) != m_Columns.size() - 1)
				sqlQuery += ", ";
			else		
				sqlQuery += ") ";
		}
		
		sqlQuery += "values(" + row.GetId() +", " + row.GenerateValuesString() + ");";
		
		db.execSQL(sqlQuery);
	}
	
	public int Count(SQLiteDatabase db)
	{
		String sqlQuery = "select count(*) as length from " + m_Name + ";";
		Cursor cursor = db.rawQuery(sqlQuery, new String[] {});
		
		if(cursor.moveToFirst())
		{
			return cursor.getInt(cursor.getColumnIndex("length"));
		}
		
		return 0;
	}
	
	public void ClearTable(SQLiteDatabase db)
	{
		String sqlQuery = "delete from " + m_Name + ";";
		db.execSQL(sqlQuery);
	}
	
	public abstract ArrayList<TableRow> Read(SQLiteDatabase db);
}