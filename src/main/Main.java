package main;

import core.Adb;
import core.DeviceMonitor;
import server.HTTPServer;


public class Main {

	public static void main(String[] args)
	{
		Adb.check();
		
		new DeviceMonitor().start();
		
		HTTPServer.start();
	}

}
