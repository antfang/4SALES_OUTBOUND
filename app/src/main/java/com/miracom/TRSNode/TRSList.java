/**
 * List class for TRSNode
 */
package com.miracom.TRSNode;

import java.util.Vector;

/**
 * @author aiden of com.miracom
 *
 */
public class TRSList {
	private String name;
	private Vector items;
	
	public TRSList()
	{
		name = "";
		items = null;
	}
	
	public TRSList(String name)
	{
		this.name = name;
		items = null;
	}
	
	public TRSList(String name, TRSNode node)
	{
		this.name = name;
		add(node); 
	}
	
	public String getName()
	{
		return name;
	}
	
	public Vector getItems()
	{
		return items;
	}
	
	public void add(TRSNode node)
	{
		if(items == null)
		{
			items = new Vector();
		}
		
		items.add(node);
	}
}
