package core;

import java.util.ArrayList;

public class DeviceInfo {

	public String usbID;
	public String tcpID;
	public boolean usbConnected, tcpConnected;
	
	public ArrayList<String> runningProcesses;
	
	
	public DeviceInfo(String id)
	{
		usbID = id;
		tcpID = "";
		usbConnected = true;
		tcpConnected = false;
		runningProcesses = new ArrayList<String>();
	}
	
	
	public String getID()
	{
		if (usbConnected)
			return usbID;
		else if (tcpConnected)
			return tcpID;
		else
			return "NULL";
	}
	
	public boolean is(String s)
	{
		return (s.equals(usbID) || s.equals(tcpID));
	}
	
}
