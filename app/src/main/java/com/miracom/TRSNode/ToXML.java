/**
 * Convert TRSNode to XML string.
 */
package com.miracom.TRSNode;

/**
 * @author aiden of com.miracom
 *
 */
public class ToXML {

	public String toXMLString(TRSNode node)
	{
		int i;
		int j;
		TRSNode cnode;
		TRSList nlist;
		TRSNode litem;
		
		boolean b_exist_attribute;
		StringBuffer sb = new StringBuffer();
		
        sb.append(toXMLStringMember(node));

        b_exist_attribute = false;
        if (sb.length() > 0)
        {
            b_exist_attribute = true;
        }

        if (node.nodeType == NodeType.ELEMENT)
        {
            if (b_exist_attribute == true)
            {
                sb.append("<" + TRSDefine.XML_TAG_LIST + " " + TRSDefine.XML_TAG_NAME + "=\"" + node.getName() + "\">");
            }

            for(i = 0; i < node.getMembers().size(); i++)
            {
            	cnode = (TRSNode)node.getMembers().get(i);
                sb.append(toXMLStringSub(cnode));
            }

            for(i = 0; i < node.getList().size(); i++)
            {
            	nlist = (TRSList)node.getList().get(i);
            	
                for(j = 0; j < nlist.getItems().size(); j++)
                {
                	litem = (TRSNode)nlist.getItems().get(j);
                    sb.append(toXMLStringSub(litem));
                }
            }

            if (b_exist_attribute == true)
            {
                sb.append("</" + TRSDefine.XML_TAG_LIST + ">");
            }
        }

        return sb.toString();
    }
	
    private String toXMLStringSub(TRSNode node)
    {
		int i;
		int j;
		TRSNode cnode;
		TRSList nlist;
		TRSNode litem;

		StringBuffer sb = new StringBuffer();

        sb.append(toXMLStringMember(node));

        if(node.nodeType == NodeType.ELEMENT)
        {
            sb.append("<" + TRSDefine.XML_TAG_LIST + " " + TRSDefine.XML_TAG_NAME + "=\"" + node.getName() + "\">");

            for(i = 0; i < node.getMembers().size(); i++)
            {
            	cnode = (TRSNode)node.getMembers().get(i);
                sb.append(toXMLStringSub(cnode));
            }

            for(i = 0; i < node.getList().size(); i++)
            {
            	nlist = (TRSList)node.getList().get(i);
            	
                for(j = 0; j < nlist.getItems().size(); j++)
                {
                	litem = (TRSNode)nlist.getItems().get(j);
                    sb.append(toXMLStringSub(litem));
                }
            }

            sb.append("</" + TRSDefine.XML_TAG_LIST + ">");
        }

        return sb.toString();
    }

	private String toXMLStringMember(TRSNode node)
	{
		StringBuffer sb = new StringBuffer();
		
		if(node.nodeType == NodeType.ATTRIBUTE ||
		   (node.nodeType == NodeType.ELEMENT && node.value != null && node.value.trim() != ""))
		{
			sb.append("<" + TRSDefine.XML_TAG_DATA + " " + TRSDefine.XML_TAG_NAME + "=\"" + node.getName() + "\" " + TRSDefine.XML_TAG_TYPE + "=\"");

			if(node.dataType == DataType.INT)
				sb.append(TRSDefine.XML_TYPE_TAG_INT);
			else if(node.dataType == DataType.FLOAT)
				sb.append(TRSDefine.XML_TYPE_TAG_FLOAT);
			else if(node.dataType == DataType.DOUBLE)
				sb.append(TRSDefine.XML_TYPE_TAG_DOUBLE);
			else if(node.dataType == DataType.CHAR)
				sb.append(TRSDefine.XML_TYPE_TAG_CHAR);
			else if(node.dataType == DataType.STRING)
				sb.append(TRSDefine.XML_TYPE_TAG_STRING);

            if (node.dataType == DataType.STRING)
            {
                if (node.value == null)
                {
                    sb.append("\" " + TRSDefine.XML_TAG_SIZE + "=\"0\"");
                }
                else
                {
                    sb.append("\" " + TRSDefine.XML_TAG_SIZE + "=\"" + node.value.length() + "\"");
                }
            }
            else
            {
                sb.append("\"");
            }

            if (node.value != null)
            {
                sb.append(">" + node.value + "</" + TRSDefine.XML_TAG_DATA + ">");
            }
            else
            {
                sb.append("/>");
            }
        }

        return sb.toString();
	}

}
