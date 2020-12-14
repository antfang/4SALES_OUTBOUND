package com.miracom.MessageHandler;

import com.miracom.TRSNode.TRSNode;

public interface ServiceDispatcher 
{
    public boolean dispatch(String s_service_name, TRSNode in_node, TRSNode out_node);
}
