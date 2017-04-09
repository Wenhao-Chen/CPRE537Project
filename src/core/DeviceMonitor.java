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
	public static ArrayList<String> currentDevices;
	
	public DeviceMonitor()
	{
		stalkers = new ArrayList<Stalker>();
		currentDevices = new ArrayList<String>();
	}


	@Override
	public void run()
	{
		while (true)
		{
			ArrayList<Stalker> newStalkers = new ArrayList<>();
			for (Stalker stalker : stalkers)
			{
				if (stalker.victimConnected)
				{
					newStalkers.add(stalker);
				}
			}
			
			stalkers = newStalkers;
			
			currentDevices = Adb.getDevices();
			for (String device : currentDevices)
			{
				if (!hasStalker(device))
				{
					P.print("New device connected: " + device + ". Sending new Stalker.");
					Stalker stalker = new Stalker(device);
					stalker.start();
					stalkers.add(stalker);
				}
			}
			
			wait(200);
		}
	}
	
	public void start()
	{
		new Thread(this).start();
	}
	
	private boolean hasStalker(String deviceID)
	{
		for (Stalker stalker : stalkers)
		{
			if (stalker.deviceID.equals(deviceID))
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
