package com.raggamuffin.protorunnerv2.data;

public class SQLTableColumn 
{
	private String m_Name;
	private DataType m_DataType;
	private Constraints m_Constraint;
	
	public SQLTableColumn(String name, DataType dataType, Constraints extra)
	{
		m_Name = name;
		m_DataType = dataType;
        m_Constraint = extra;
	}
	
	public String GetName()
	{
		return m_Name;
	}
	
	public DataType GetDataType()
	{
		return m_DataType;
	}
	
	public Constraints GetConstraint()
	{
		return m_Constraint;
	}
}
