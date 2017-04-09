package core;

import java.io.IOException;
import java.util.ArrayList;

import main.Settings;

public class Adb {

	String deviceID;
	private enum Mode {usb, tcp};
	
	public Adb(String id)
	{
		this.deviceID = id;
	}
	
	public static void check()
	{
		try
		{
			Runtime.getRuntime().exec(Settings.AdbPath + " devices");
		}
		catch (IOException e)
		{
			if (e.getMessage().contains("cannot find the file specified"))
			{
				System.err.println(e.getMessage());
				System.err.println("Please check the settings in 'main.Settings.java'");
				System.exit(1);
			}
			else
				e.printStackTrace();
		}
		
		ArrayList<String> devices = getDevices();
		if (devices.size() == 0)
		{
			System.err.println("No android devices currently connected to adb server");
		}
		else
		{
			P.print("Adb connected devices: ");
			P.print(devices);
		}
	}
		
	public ArrayList<String> readLogcat()
	{
		Process p = exec("logcat");
		return P.read(p.getInputStream(), 1);
	}
	

	public static ArrayList<String> getDevices()
	{
		ArrayList<String> deviceInfo = P.read(exec("devices", false).getInputStream());
		ArrayList<String> devices = new ArrayList<String>();
		for (String device : deviceInfo)
		{
			if (device.startsWith("List of devices attached") || !device.contains("device"))
				continue;
			String id = device.split("\t")[0];
			devices.add(id);
		}
		return devices;
	}
	
	
	public static Process exec(String cmd)
	{
		return exec(cmd, true);
	}
	
	public static Process exec(String cmd, boolean printCMD)
	{
		return P.exec(Settings.AdbPath+ " " + cmd, printCMD);
	}

	
}
