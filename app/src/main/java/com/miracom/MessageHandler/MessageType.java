/*******************************************************************************
' MessageType.java
'
' Copyright (c) 2009 by Miracom,Inc.
' All rights reserved.
'
' Generated by DevTool XMLGen 1.0
'
' Created at 2009/06/02 10:26:14
'
' Author : Miracom. R&D.
' Description : DevTool Xml Generator Version 1.0
'******************************************************************************/

package com.miracom.MessageHandler;

import com.miracom.oneoone.transceiverx.StreamTransformer;

public class MessageType
{
    public static void serialize_Message(StreamTransformer former, String target) throws Exception
    {
        former.writeMsgString(target);
    }

    public static String transform_Message(StreamTransformer former) throws Exception
    {
    	return former.readMsgString();
    }
}
