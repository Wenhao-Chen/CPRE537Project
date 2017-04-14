package http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import core.DeviceMonitor;
import core.P;

public class EvilHandler implements HttpHandler{

	DeviceMonitor m;
	EvilHandler(DeviceMonitor monitor)
	{
		m = monitor;
	}
	
	
	@Override
	public void handle(HttpExchange exc) throws IOException
	{
		byte[] req = new byte[exc.getRequestBody().available()];
		
		exc.getRequestBody().read(req, 0, req.length);
		String s = req.toString();
		
		System.out.println("request body="+s);
		System.out.println("request header=" + exc.getRequestHeaders().get("code"));
		System.out.println("request method=" + exc.getRequestMethod());
		
		s = exc.getRequestHeaders().get("code").get(0);
		if (s.equals("0"))
		{
			ArrayList<String> processes = m.stalkers.get(0).device.runningProcesses;
			P.print(processes);
			sendMessages(exc, processes);
		}
		else if (s.equals("1"))
		{
			sendMessages(exc, "fuck");
		}
		else
		{
			sendMessages(exc, "Do not recognize request code " + s);
		}
        //exc.sendResponseHeaders(200, image.length());
        //OutputStream os = exc.getResponseBody();
        //os.write(Files.readAllBytes(image.toPath()));
        //os.close();
	}

	void sendMessages(HttpExchange exc, ArrayList<String> msgs)
	{
		String msg = "";
		for (String s : msgs)
			msg += s+"\n";
		sendMessages(exc, msg);
	}
	
	void sendMessages(HttpExchange exc, String msg)
	{
		try
		{
			exc.sendResponseHeaders(200, msg.length());
			OutputStream out = exc.getResponseBody();
			out.write(msg.getBytes());
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
