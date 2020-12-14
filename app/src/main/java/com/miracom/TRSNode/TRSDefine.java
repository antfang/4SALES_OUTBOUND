/**
 * Define public constants
 */
package com.miracom.TRSNode;

/**
 * @author aiden of com.miracom
 *
 */
public final class TRSDefine {
	/**
	 * Message header information
	 */
	public static final String XML_TAG_MSG = "MESSAGE";
	public static final String XML_TAG_VERSION = "Version";
	public static final String XML_MSG_VERSION = "1.0";
	public static final String XML_TAG_MSG_NAME = "Name";
	
	/**
	 * Data message
	 */
	public static final String XML_TAG_BODY = "B";
	public static final String XML_TAG_DATA = "D";
	public static final String XML_TAG_LIST = "L";
	public static final String XML_TAG_NAME = "N";
	public static final String XML_TAG_TYPE = "T";
	public static final String XML_TAG_SIZE = "S";
	
	/**
	 * Data type
	 */
	public static final String XML_TYPE_TAG_STRING = "S";
	public static final String XML_TYPE_TAG_INT = "I4";
	public static final String XML_TYPE_TAG_FLOAT = "F4";
	public static final String XML_TYPE_TAG_DOUBLE = "F8";
	public static final String XML_TYPE_TAG_CHAR = "C";
	
	/**
	 * Default out node name
	 */
	public static final String OUT_STATUSVALUE = "STATUSVALUE";
	public static final String OUT_MSGCODE = "MSGCODE";
	public static final String OUT_MSG = "MSG";
	public static final String OUT_MSGCATE = "MSGCATE";
	public static final String OUT_FIELDMSG = "FIELDMSG";
	public static final String OUT_DBERRMSG = "DBERRMSG";
	
	/**
	 * Default in node name
	 */
	public static final String IN_PASSPORT = "PASSPORT";
	public static final String IN_LANGUAGE = "LANGUAGE";
	public static final String IN_FACTORY = "FACTORY";
	public static final String IN_USERID = "USERID";
	public static final String IN_PASSWORD = "PASSWORD";
	public static final String IN_PROCSTEP = "PROCSTEP";
}
