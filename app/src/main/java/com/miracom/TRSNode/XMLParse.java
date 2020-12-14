/**
 * Parse XML string and save information to TRSNode
 */
package com.miracom.TRSNode;

/**
 * @author aiden of com.miracom
 *
 */

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParse extends DefaultHandler
{
	private TRSBaseNode 	node;
	private TRSBaseNode		last_node;
	private String 			s_member_name;
	private String			s_data_type;
	
	private TRSBaseNode		prev_node;
	private String 			s_prev_member_name;

	public void parse(TRSNode node, String xmlString) throws Exception
	{
		SAXParserFactory factory;
		SAXParser saxParser;
		InputStream is;
		
		this.node = node;

		is = new ByteArrayInputStream(xmlString.getBytes());
		
		factory = SAXParserFactory.newInstance();
		saxParser = factory.newSAXParser();
		saxParser.parse(is, this);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException
	{
		if(qName.equals(TRSDefine.XML_TAG_MSG) == true)
		{
			if(attrs.getValue(TRSDefine.XML_TAG_VERSION).equals(TRSDefine.XML_MSG_VERSION) == false)
			{	
			}
			
			this.node.setName(attrs.getValue(TRSDefine.XML_TAG_MSG_NAME));
		}
		else
		{
			if(qName.equals(TRSDefine.XML_TAG_DATA) == true)
			{
				s_member_name = attrs.getValue(TRSDefine.XML_TAG_NAME);
				s_data_type = attrs.getValue(TRSDefine.XML_TAG_TYPE);
			}
			if(qName.equals(TRSDefine.XML_TAG_LIST) == true)
			{
				if(s_member_name.equals(attrs.getValue(TRSDefine.XML_TAG_NAME)) == true)
				{
					this.node = this.last_node;
				}
				else
				{
					s_member_name = attrs.getValue(TRSDefine.XML_TAG_NAME);
					try
					{
						this.node = this.node.addNode(s_member_name);
					} catch(Exception e)
					{
						throw new SAXException(e.getMessage());
					}
				}
			}			
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if(qName.equals(TRSDefine.XML_TAG_LIST) == true)
		{
			this.node = this.node.parent;
		}
	}
	
	public void characters(char[] buf, int start, int length) throws SAXException
	{
		String 	s_value = String.valueOf(buf, start, length);
		
		try
		{
			if(s_data_type.equals(TRSDefine.XML_TYPE_TAG_INT) == true)
				this.last_node = this.node.addMember(s_member_name, s_value, DataType.INT);
			else if(s_data_type.equals(TRSDefine.XML_TYPE_TAG_FLOAT) == true)
				this.last_node = this.node.addMember(s_member_name, s_value, DataType.FLOAT);
			else if(s_data_type.equals(TRSDefine.XML_TYPE_TAG_DOUBLE) == true)
				this.last_node = this.node.addMember(s_member_name, s_value, DataType.DOUBLE);
			else if(s_data_type.equals(TRSDefine.XML_TYPE_TAG_CHAR) == true)
				this.last_node = this.node.addMember(s_member_name, s_value, DataType.CHAR);
			else if(s_data_type.equals(TRSDefine.XML_TYPE_TAG_STRING) == true)
			{
				if(this.prev_node == this.node && this.s_prev_member_name.equals(s_member_name) == true)
				{
					String s_node_value = this.node.getString(s_member_name);
					s_node_value += s_value;
					this.last_node = this.node.setString(s_member_name, s_node_value);
				}
				else
				{
					this.last_node = this.node.addString(s_member_name, s_value);
				}
				this.prev_node = this.node;
				this.s_prev_member_name = s_member_name;
			}
		
		}catch(Exception e)
		{
			throw new SAXException(e.getMessage());
		}
	}
	
}
