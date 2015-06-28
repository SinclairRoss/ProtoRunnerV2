package com.raggamuffin.protorunnerv2.data;

public class SQLTableColumn 
{
	private String m_Name;
	private DataType m_DataType;
	private ExtraInformation m_ExtraInformation;
	
	public SQLTableColumn(String name, DataType dataType, ExtraInformation extra)
	{
		m_Name = name;
		m_DataType = dataType;
		m_ExtraInformation = extra;
	}
	
	public String GetName()
	{
		return m_Name;
	}
	
	public DataType GetDataType()
	{
		return m_DataType;
	}
	
	public ExtraInformation GetExtraInformation()
	{
		return m_ExtraInformation;
	}
}
