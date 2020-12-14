package com.miracom.MessageHandler;

public class MPMH 
{
    private static com.miracom.MessageHandler.MessageHandler msg_handler = null;

    public static com.miracom.MessageHandler.MessageHandler getInstance()
    {
        if (null == msg_handler)
            msg_handler = new com.miracom.MessageHandler.MessageHandler();
        return msg_handler;
    }

    public static String getStatusMessage()
    {
        return getInstance().getMessage();
    }

    public static void setStatusMessage(String s_msg)
    {
    	getInstance().setMessage(s_msg);
    }

    public static void registerDispatcher(String module, Object tuner)
    {
    	getInstance().registerDispatcher(module, tuner);
    }

    public static boolean tune(String channel, int deliveryType)
    {
        return getInstance().tune(channel, deliveryType);
    }
    public static void unregisterDispatcher(String module)
    {
    	getInstance().unregisterDispatcher(module);
    }
    public static boolean untune(String channel, int deliveryType)
    {
        return getInstance().untune(channel, deliveryType);
    }
}
