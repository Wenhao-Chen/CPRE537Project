package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Settings;

public class Adb {

	String deviceID;
	private enum Mode {usb, tcp};
	Mode mode;
	
	public Adb(String id)
	{
		this.deviceID = id;
		mode = Mode.usb;
	}

		
	public ArrayList<String> readLogcat()
	{
		Process p = exec("logcat");
		return P.read(p.getInputStream(), 1);
	}
	
	public void toTCPMode()
	{
		if (mode != Mode.tcp)
		{
			String ip = getIPAddress();
			sendCmd("tcpip 5555");
			Process p = sendCmd("connect " + ip, false);
			P.print(P.read(p.getInputStream()));
			mode = Mode.tcp;
		}
	}
	
	public String getIPAddress()
	{
		ArrayList<String> output = P.read(sendCmd("shell ip route", false).getInputStream());
		Pattern p = Pattern.compile("src \\d{2,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		for (String line : output)
		{
			Matcher m = p.matcher(line);
			if (m.find())
				return m.group(0).substring(4);
		}
		return "null";
	}
	
	public Process sendCmd(String cmd)
	{
		return sendCmd(cmd, true);
	}
	
	public Process sendCmd(String cmd, boolean printCMD)
	{
		Process p = P.exec(Settings.AdbPath + " -s " + deviceID + " " + cmd, printCMD);
		if (!cmd.startsWith("logcat"))
			try
			{
				p.waitFor();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			} 
		return p;
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
		Process p = P.exec(Settings.AdbPath+ " " + cmd, printCMD);
		if (!cmd.startsWith("logcat"))
			try
			{
				p.waitFor();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			} 
		return p;
	}
	

	
}
