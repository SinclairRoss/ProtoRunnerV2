package com.raggamuffin.protorunnerv2.data;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLTable
{
	protected String m_Name;
    protected SQLiteDatabase m_Database;
	private ArrayList<SQLTableColumn> m_Columns;
	
	public SQLTable(String name, SQLiteDatabase db)
	{
		m_Name = name;
        m_Database = db;

		m_Columns = new ArrayList<SQLTableColumn>();
		
		AddColumn(new SQLTableColumn("id", DataType.Integer, Constraints.PrimaryKey));
    }

    protected void CreateTable()
    {
        String sqlQuery = "create table if not exists " + m_Name + " (";

        ArrayList<SQLTableColumn> columns = GetColumns();

        for(SQLTableColumn column : columns)
        {
            sqlQuery += column.GetName() + " " + GetDataTypeString(column.GetDataType()) + " " + GetConstraintString(column.GetConstraint()) + " ";

            if(columns.indexOf(column) != columns.size() - 1)
                sqlQuery += ",";
            else
                sqlQuery += ");";
        }

        m_Database.execSQL(sqlQuery);
    }

	public void AddColumn(SQLTableColumn column)
	{
		m_Columns.add(column);
	}

	public void Insert(TableRow row)
	{
		String sqlQuery = "insert into " + m_Name + "(";

		for(SQLTableColumn column : m_Columns)
		{
			if(column.GetName().equals("id"))
				continue;
			
			sqlQuery += column.GetName();
			
			if(m_Columns.indexOf(column) != m_Columns.size() - 1)
				sqlQuery += ", ";
			else		
				sqlQuery += ") ";
		}
		
		sqlQuery += "values(" + row.GenerateValuesString() + ");";

        m_Database.execSQL(sqlQuery);
	}

    public abstract ArrayList<TableRow> Read();
	
	public void PreserveIDInsert(TableRow row)
	{
		String sqlQuery = "insert into " + m_Name + "(id,";

		for(SQLTableColumn column : m_Columns)
		{
			if(column.GetName().equals("id"))
				continue;
			
			sqlQuery += column.GetName();
			
			if(m_Columns.indexOf(column) != m_Columns.size() - 1)
				sqlQuery += ", ";
			else		
				sqlQuery += ") ";
		}
		
		sqlQuery += "values(" + row.GetId() +", " + row.GenerateValuesString() + ");";

        m_Database.execSQL(sqlQuery);
	}
	
	public int Count()
	{
		String sqlQuery = "select count(*) as length from " + m_Name + ";";
		Cursor cursor = m_Database.rawQuery(sqlQuery, new String[] {});
		
		if(cursor.moveToFirst())
		{
			return cursor.getInt(cursor.getColumnIndex("length"));
		}
		
		return 0;
	}
	
	public void ClearTable()
	{
		String sqlQuery = "delete from " + m_Name + ";";
        m_Database.execSQL(sqlQuery);
	}

    public String GetName()
    {
        return m_Name;
    }

    public ArrayList<SQLTableColumn> GetColumns()
    {
        return m_Columns;
    }

    protected String GetDataTypeString(DataType type)
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

    protected String GetConstraintString(Constraints info)
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