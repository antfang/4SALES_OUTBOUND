/**
 * Define TRSNode data type
 */
package com.miracom.TRSNode;

/**
 * @author aiden of com.miracom
 *
 */
//public enum DataType {
//	STRING,
//	INT,
//	DOUBLE,
//	FLOAT,
//	CHAR
//}

public final class DataType {
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int DOUBLE = 2;
	public static final int FLOAT = 3;
	public static final int CHAR = 4;
	
	public static String toString(int type)
	{
		String s_type = "";
		
		switch(type)
		{
		case STRING:
			s_type = "String";
			break;
		case INT:
			s_type = "Int";
			break;
		case DOUBLE:
			s_type = "Double";
			break;
		case FLOAT:
			s_type = "Float";
			break;
		case CHAR:
			s_type = "Char";
			break;
		}
		
		return s_type;
	}
}
