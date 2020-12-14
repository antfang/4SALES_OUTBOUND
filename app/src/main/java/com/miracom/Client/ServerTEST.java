package com.miracom.Client;

import java.lang.Thread;
import com.miracom.TRSNode.*;
import com.miracom.MessageHandler.*;

public class ServerTEST
{
	private static int i_running_count;

	public static void main(String[] args)
	{
		ServerTEST st = new ServerTEST();

		if(st.InitMsgHandler() == false)
			return ;

		i_running_count = 0;
		while(true)
		{
			if(i_running_count > (10 * 60 * 1))
			{
				break;
			}

			try
			{
				Thread.sleep(100);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			i_running_count++;
		}

		st.TermMsgHandler();
	}

	public boolean InitMsgHandler()
	{
		String url = "10.10.17.25:10101";
//		String url = "127.0.0.1:10101";
		if(MPMH.getInstance().init("TEST_SERVER", 2, url, 10104) == false)
		{
			System.out.println("Middleware Initialize. - " + MPMH.getStatusMessage());
			return false;
		}
		MPMH.registerDispatcher("MESPLUS", new MessageTuner());

		MPMH.registerDispatcher("JAVA", new TunerImpl());
		if(MPMH.tune("/MPV1/MESServer", DeliveryMode.Unicast) == false)
		{
			System.out.println("Middleware Tune. - " + MPMH.getStatusMessage());
			return false;
		}

		return true;
	}

	public boolean TermMsgHandler()
	{
        try
        {
        	MPMH.untune("/MPT2/MESServerTest", DeliveryMode.Unicast);
            MPMH.unregisterDispatcher("JAVA");

        	MPMH.unregisterDispatcher("MESPLUS");
            MPMH.getInstance().term();
		}
        catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		return true;
	}

}

class TunerImpl implements ServiceDispatcher
{
	public boolean dispatch(String s_service_name, TRSNode in_node, TRSNode out_node)
	{
        if (s_service_name.equals("Java_Test_Service"))
        	Java_Test_Service(in_node, out_node);
        else
            return false;

        return true;
	}

	public void Java_Test_Service(TRSNode in_node, TRSNode out_node)
	{
		try
		{
			System.out.println("LOT_ID : " + in_node.getString("LOT_ID"));
			System.out.println("MAT_ID : " + in_node.getString("MAT_ID"));
			System.out.println("MAT_VER : " + in_node.getInt("MAT_VER"));
			System.out.println("QTY_1 : " + in_node.getDouble("QTY_1"));
			System.out.println("HOLD_FLAG : " + in_node.getChar("HOLD_FLAG"));

			/* Business Logic */

			out_node.addString("NAME", "JAVA");
			out_node.addInt("AGE", 28);
			out_node.addChar("SEX", 'F');
			out_node.addString("DESC", "JAVA");

			out_node.setStatusValue('0');
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
