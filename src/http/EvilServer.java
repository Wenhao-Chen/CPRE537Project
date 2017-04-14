package http;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import core.DeviceMonitor;

public class EvilServer {
	
	DeviceMonitor m;
	public EvilServer(DeviceMonitor monitor)
	{
		m = monitor;
	}
	
	public void start()
	{
		try
		{
	        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	        server.createContext("/test", new EvilHandler(m));
	        server.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
