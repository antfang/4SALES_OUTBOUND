/**
 * Dynamic transfer data node for XML
 */
package com.miracom.TRSNode;

/**
 * @author aiden of com.miracom
 *
 */
public class TRSNode extends TRSBaseNode {
	
	
	public TRSNode(String name)
	{
		super();
		this.name = name;
	}

	/* Get method for in_node */
	public String getPassport() throws Exception
	{
		return this.getString(TRSDefine.IN_PASSPORT);
	}

	public char getLanguage() throws Exception
	{
		return this.getChar(TRSDefine.IN_LANGUAGE);
	}

	public String getFactory() throws Exception
	{
		return this.getString(TRSDefine.IN_FACTORY);
	}

	public String getUserID() throws Exception
	{
		return this.getString(TRSDefine.IN_USERID);
	}

	public String getPassword() throws Exception
	{
		return this.getString(TRSDefine.IN_PASSWORD);
	}

	public char getProcStep() throws Exception
	{
		return this.getChar(TRSDefine.IN_PROCSTEP);
	}

	/* Set method for in_node */
	public void setPassport(String value) throws Exception
	{
		this.setString(TRSDefine.IN_PASSPORT, value);
	}
	
	public void setLanguage(char value) throws Exception
	{
		this.setChar(TRSDefine.IN_LANGUAGE, value);
	}

	public void setFactory(String value) throws Exception
	{
		this.setString(TRSDefine.IN_FACTORY, value);
	}
	
	public void setUserID(String value) throws Exception
	{
		this.setString(TRSDefine.IN_USERID, value);
	}
	
	public void setPassword(String value) throws Exception
	{
		this.setString(TRSDefine.IN_PASSWORD, value);
	}
	
	public void setProcStep(char value) throws Exception
	{
		this.setChar(TRSDefine.IN_PROCSTEP, value);
	}

	/* Get method for out_node */
	public char getStatusValue() throws Exception
	{
		return this.getChar(TRSDefine.OUT_STATUSVALUE);
	}

	public String getMsgCode() throws Exception
	{
		return this.getString(TRSDefine.OUT_MSGCODE);
	}

	public String getMsg() throws Exception
	{
		return this.getString(TRSDefine.OUT_MSG);
	}

	public char getMsgCate() throws Exception
	{
		return this.getChar(TRSDefine.OUT_MSGCATE);
	}

	public String getDBErrMsg() throws Exception
	{
		return this.getString(TRSDefine.OUT_DBERRMSG);
	}
	
	/* Set method for out_node */
	public void setStatusValue(char value) throws Exception
	{
		this.setChar(TRSDefine.OUT_STATUSVALUE, value);
	}

	public void setMsgCode(String value) throws Exception
	{
		this.setString(TRSDefine.OUT_MSGCODE, value);
	}

	public void setMsg(String value) throws Exception
	{
		this.setString(TRSDefine.OUT_MSG, value);
	}

	public void setMsgCate(char value) throws Exception
	{
		this.setChar(TRSDefine.OUT_MSGCATE, value);
	}

	public void setDBErrMsg(String value) throws Exception
	{
		this.setString(TRSDefine.OUT_DBERRMSG, value);
	}

	/* toXMLString */
	public String toXMLString()
	{
		ToXML toXml;
		String s_xml;
		
		if(this.parent != null) return "";
		
		toXml = new ToXML();
		
		s_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		s_xml += "<" + TRSDefine.XML_TAG_MSG + " " + TRSDefine.XML_TAG_VERSION + "=\"1.0\" " + TRSDefine.XML_TAG_MSG_NAME + "=\"" + this.getName() + "\">";
		s_xml += "<" + TRSDefine.XML_TAG_BODY + ">";
		s_xml += toXml.toXMLString(this);
		s_xml += "</" + TRSDefine.XML_TAG_BODY + ">";
		s_xml += "</" + TRSDefine.XML_TAG_MSG + ">";
		
		return s_xml;
	}
	
	/* parse */
	public boolean parse(String xmlString) throws Exception
	{
		XMLParse xmlParse;
		
		if(xmlString.trim() == "") return false;
		
		xmlParse = new XMLParse();
		xmlParse.parse(this, xmlString);
		
		return true;
	}
	
	public int getXmlLength()
	{
		int totalLength = 0;
		
		totalLength += this.getNodeXmlLength();
		totalLength += (TRSDefine.XML_TAG_MSG.length() * 2);
		totalLength += TRSDefine.XML_TAG_VERSION.length() + TRSDefine.XML_TAG_MSG_NAME.length() + 3 + 13 + 21;
		
		return totalLength;
	}
	
	private int getNodeXmlLength()
	{
		int i;
		int j;
		TRSNode member;
		TRSNode litem;
		TRSList nlist;
		
		int oneListUnit = (TRSDefine.XML_TAG_LIST.length() * 2) + TRSDefine.XML_TAG_NAME.length() + 9;
		int totalLength = this.length;
		
		for(i = 0; i < this.members.size(); i ++)
		{
			member = (TRSNode)this.members.get(i);
			
			if(member.nodeType == NodeType.ATTRIBUTE)
			{
				totalLength += member.length;
			}
			else if(member.nodeType == NodeType.ELEMENT)
			{
				totalLength += member.getNodeXmlLength();
				totalLength += oneListUnit;
			}
		}
		
		for(i = 0; i < this.list.size(); i ++)
		{
			nlist = (TRSList)this.list.get(i);
			
			for(j = 0; j < nlist.getItems().size(); j++)
			{
				litem = (TRSNode)nlist.getItems().get(j);
				
				totalLength += litem.getNodeXmlLength();
				totalLength += oneListUnit;
			}
		}
		
		return totalLength;
	}
	
}
