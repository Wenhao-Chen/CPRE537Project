package main;

import core.Adb;
import core.DeviceMonitor;


public class Main {

	public static void main(String[] args)
	{
		Adb.check();
		
		new DeviceMonitor().start();
		
		
	}

}
