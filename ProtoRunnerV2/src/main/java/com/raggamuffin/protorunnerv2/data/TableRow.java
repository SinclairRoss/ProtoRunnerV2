package com.raggamuffin.protorunnerv2.data;

public abstract class TableRow 
{
	private int m_Id;
	
	public TableRow(int id)
	{
		m_Id = id;
	}
	
	public int GetId()
	{
		return m_Id;
	}
	
	public abstract String GenerateValuesString();
}
