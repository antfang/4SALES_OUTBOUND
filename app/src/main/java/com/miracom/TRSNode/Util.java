/**
 * Utilities for TRSNode
 */
package com.miracom.TRSNode;

/**
 * @author aiden
 *
 */
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Util {
	public static String trimEnd(String s)
	{
		int i;
		
		for(i = s.length() - 1; i >= 0; i--)
		{
			if(s.charAt(i) != ' ' && s.charAt(i) != '\u0020')
				break;
		}
		
		if(i == 0)
			return "";

		return s.substring(0, i + 1);
	}

	public static String convertEscape(String str){
		final StringBuffer result = new StringBuffer();
	    final StringCharacterIterator iterator = new StringCharacterIterator(str);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	    	if (character == '<') {
	    		result.append("&lt;");
    		}
	    	else if (character == '>') {
	    		result.append("&gt;");
	    	}
	    	else if (character == '\"') {
	    		result.append("&quot;");
	    	}
	    	else if (character == '\'') {
	    		result.append("&#039;");
	    	}
	    	else if (character == '&') {
	    		result.append("&amp;");
	    	}
	    	else {
	    		//the char is not a special one
	    		//add it to the result as is
	    		result.append(character);
	    	}
	    	character = iterator.next();
    	}
	    return result.toString();
	}

	void initNode(TRSBaseNode node)
	{
		int i;
		int j;
		TRSNode cnode;
		TRSNode litem;
		TRSList nlist;
		
		if(node.nodeType == NodeType.ELEMENT)
		{
			for(i = 0; i < node.getMembers().size(); i++)
			{
				cnode = (TRSNode)node.getMembers().get(i);
				initNodeSub(cnode);
			}
			
			for(i = 0; i < node.getList().size(); i++)
			{
				nlist = (TRSList)node.getList().get(i);
				
				for(j = 0; j < nlist.getItems().size(); j++)
				{
					litem = (TRSNode)nlist.getItems().get(j);
					initNodeSub(litem);
				}
			}
		}
		
		node.initNodeExceptName();
	}
	
	void initNodeSub(TRSBaseNode node)
	{
		int i;
		int j;
		TRSNode cnode;
		TRSNode litem;
		TRSList nlist;

		if(node.nodeType == NodeType.ELEMENT)
		{
			for(i = 0; i < node.getMembers().size(); i++)
			{
				cnode = (TRSNode)node.getMembers().get(i);
				initNodeSub(cnode);
			}
			
			for(i = 0; i < node.getList().size(); i++)
			{
				nlist = (TRSList)node.getList().get(i);
				
				for(j = 0; j < nlist.getItems().size(); j++)
				{
					litem = (TRSNode)nlist.getItems().get(j);
					initNodeSub(litem);
				}
			}
		}
		
		node.initNode();
		node = null;
	}
}
