/**
 * Base node for TRSNode
 */
package com.miracom.TRSNode;

import java.util.Vector;

/**
 * @author aiden of com.miracom
 *
 */
public abstract class TRSBaseNode {
	protected int				nodeType;
	protected int	 			dataType;
	protected TRSBaseNode		parent;
	protected int 				length;
	protected String			name;
	protected String			value;

	protected Vector		 	members;
	protected Vector		 	list;

	public TRSBaseNode()
	{
		members = new Vector();
		list = new Vector();
	}

	public TRSBaseNode getParent()
	{
		return parent;
	}

	public int getNodeType()
	{
		return nodeType;
	}

	public int getDataType()
	{
		return dataType;
	}

	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}

	void setName(String name)
	{
		this.name = name;
	}

	public Vector getMembers()
	{
		return members;
	}

	Vector getList()
	{
		return list;
	}

	public void init()
	{
		Util util = new Util();
		util.initNode(this);
	}

	void initNode()
	{
		this.name = null;
		this.nodeType = NodeType.EMPTY;
		this.parent = null;
		this.value = null;
		this.dataType = 0;
		this.length = 0;

		this.members.clear();
		this.list.clear();
	}

	void initNodeExceptName()
	{
		this.nodeType = NodeType.EMPTY;
		this.value = "";
		this.dataType = DataType.STRING;
		this.length = 0;

		this.members.clear();
		this.list.clear();
	}

	public int memberCount()
	{
		return this.members.size();
	}

	public int listCount()
	{
		return this.list.size();
	}

	public Vector getList(String list_name)
	{
		int i;
		TRSList nlist;

		for(i = 0; i < this.list.size(); i++)
		{
			nlist = (TRSList)this.list.get(i);

			if(nlist.getName().equals(list_name) == true)
				return nlist.getItems();
		}

		return new Vector(0);
	}

	public Vector getList(int index)
	{
		if(index < this.list.size())
		{
			return ((TRSList)(this.list.get(index))).getItems();
		}
		return new Vector(0);
	}


	public TRSNode addMember(String name, Object value, int dType) throws Exception
	{
		int oneUnit;
		TRSNode node;

		name = name.trim();

		oneUnit = (TRSDefine.XML_TAG_DATA.length() * 2) + TRSDefine.XML_TAG_NAME.length() + TRSDefine.XML_TAG_TYPE.length() + 14;
		node = new TRSNode(name);

		if(value == null)
			value = "";

		node.value = Util.convertEscape(value.toString());
		node.dataType = dType;

		node.nodeType = NodeType.ATTRIBUTE;
		node.parent = this;

		node.length = node.name.length() + node.value.length();
		if(dType == DataType.STRING)
		{
			int value_length = node.value.length();
			node.length += TRSDefine.XML_TAG_SIZE.length() + 3 + Integer.toString(value_length).length() + 12; // <![CDATA[]]> is 12 byte
		}
		node.length += oneUnit;

		this.members.add(node);

		if(this.nodeType != NodeType.ELEMENT)
		{
			this.nodeType = NodeType.ELEMENT;
		}

		return node;
	}

	public TRSNode addNode(String name) throws Exception
	{
		int i;
		TRSNode node;
		TRSList nlist;

		name = name.trim();
		node = new TRSNode(name);

		node.parent = this;
		node.nodeType = NodeType.EMPTY;

		if(node.nodeType != NodeType.ELEMENT)
		{
			this.nodeType = NodeType.ELEMENT;
		}

		for(i = 0; i < this.list.size(); i++)
		{
			nlist = (TRSList)this.list.get(i);

			if(nlist.getName().equals(name) == true)
			{
				nlist.add(node);
				return node;
			}
		}

		this.list.add(new TRSList(name, node));
		return node;
	}

	public void addNode(TRSNode node) throws Exception
	{
		int i;
		TRSList nlist;

		node.parent = this;

		if(node.nodeType != NodeType.ELEMENT)
		{
			this.nodeType = NodeType.ELEMENT;
		}

		if(node.nodeType == NodeType.ATTRIBUTE)
		{
			this.members.add(node);
		}
		else
		{
			for(i = 0; i < this.list.size(); i++)
			{
				nlist = (TRSList)this.list.get(i);

				if(nlist.getName().equals(node.getName()) == true)
				{
					nlist.add(node);
					return;
				}
			}

			this.list.add(new TRSList(name, node));
		}
	}

	public TRSNode addString(String name, String value) throws Exception
	{
		return addMember(name, value, DataType.STRING);
	}

	public TRSNode addChar(String name, char value) throws Exception
	{
		String s = Character.toString(value);
		return addMember(name, s, DataType.CHAR);
	}

	public TRSNode addInt(String name, int value) throws Exception
	{
		String s = Integer.toString(value);
		return addMember(name, s, DataType.INT);
	}

	public TRSNode addFloat(String name, float value) throws Exception
	{
		String s = Float.toString(value);
		return addMember(name, s, DataType.FLOAT);
	}

	public TRSNode addDouble(String name, double value) throws Exception
	{
		String s = Double.toString(value);
		return addMember(name, s, DataType.DOUBLE);
	}

	public TRSNode setString(String name, String value) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
		{
			member = this.addString(name, value);
		}
		else
		{
			if(member.dataType != DataType.STRING)
			{
				throw new Exception("[" + name + "] member is not string type. This member is [" + DataType.toString(member.dataType) + "] type");
			}

			member.value = value;
		}

		return member;
	}

	public TRSNode setChar(String name, char value) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
		{
			member = this.addChar(name, value);
		}
		else
		{
			if(member.dataType != DataType.CHAR)
			{
				throw new Exception("[" + name + "] member is not character type. This member is [" + DataType.toString(member.dataType) + "] type");
			}

			member.value = String.valueOf(value);
		}

		return member;
	}

	public TRSNode setInt(String name, int value) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
		{
			member = this.addInt(name, value);
		}
		else
		{
			if(member.dataType != DataType.INT)
			{
				throw new Exception("[" + name + "] member is not integer type. This member is [" + DataType.toString(member.dataType) + "] type");
			}

			member.value = String.valueOf(value);
		}

		return member;
	}

	public TRSNode setFloat(String name, float value) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
		{
			member = this.addFloat(name, value);
		}
		else
		{
			if(member.dataType != DataType.FLOAT)
			{
				throw new Exception("[" + name + "] member is not float type. This member is [" + DataType.toString(member.dataType) + "] type");
			}

			member.value = String.valueOf(value);
		}

		return member;
	}

	public TRSNode setDouble(String name, double value) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
		{
			member = this.addDouble(name, value);
		}
		else
		{
			if(member.dataType != DataType.DOUBLE)
			{
				throw new Exception("[" + name + "] member is not double type. This member is [" + DataType.toString(member.dataType) + "] type");
			}

			member.value = String.valueOf(value);
		}

		return member;
	}

	public TRSNode getMember(String name) throws Exception
	{
		int i;
		TRSNode node;

		for(i = 0; i < this.members.size(); i++)
		{
			node = (TRSNode)this.members.get(i);

			if(node.getName().equals(name) == true)
				return node;
		}

		return null;
	}

	public TRSNode getMember(int index) throws Exception
	{
		if(index < this.members.size())
		{
			return (TRSNode)this.members.get(index);
		}

		return null;
	}

	public String getString(String name) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
			return "";

		if(member.value == null)
			return "";

		if(member.dataType != DataType.STRING)
		{
			throw new Exception("[" + name + "] member is not string type. This member is [" + DataType.toString(member.dataType) + "] type");
		}

		return member.value;
	}

	public char getChar(String name) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
			return ' ';

		if(member.value == null)
			return ' ';

		if(member.dataType != DataType.CHAR)
		{
			throw new Exception("[" + name + "] member is not character type. This member is [" + DataType.toString(member.dataType) + "] type");
		}

		return member.value.charAt(0);
	}

	public int getInt(String name) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
			return 0;

		if(member.value == null)
			return 0;

		if(member.dataType != DataType.INT)
		{
			throw new Exception("[" + name + "] member is not integer type. This member is [" + DataType.toString(member.dataType) + "] type");
		}

		return Integer.parseInt(member.value);
	}

	public float getFloat(String name) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
			return 0;

		if(member.value == null)
			return 0;

		if(member.dataType != DataType.FLOAT)
		{
			throw new Exception("[" + name + "] member is not float type. This member is [" + DataType.toString(member.dataType) + "] type");
		}

		return Float.parseFloat(member.value);
	}

	public double getDouble(String name) throws Exception
	{
		TRSNode member = this.getMember(name);

		if(member == null)
			return 0;

		if(member.value == null)
			return 0;

		if(member.dataType != DataType.DOUBLE)
		{
			throw new Exception("[" + name + "] member is not double type. This member is [" + DataType.toString(member.dataType) + "] type");
		}

		return Double.parseDouble(member.value);
	}
}
