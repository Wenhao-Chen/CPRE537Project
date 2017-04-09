package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class P {

	
	public static Process exec(String cmd)
	{
		return exec(cmd, true);
	}
	
	public static Process exec(String cmd, boolean printCMD)
	{
		try
		{
			if (printCMD)
				print(cmd);
			return Runtime.getRuntime().exec(cmd);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void print(String s)
	{
		System.out.println(s);
	}
	
	public static void print(ArrayList<String> ss)
	{
		for (String s : ss)
		{
			System.out.println(s);
		}
	}
	
	public static ArrayList<String> read(InputStream stream)
	{
		ArrayList<String> result = new ArrayList<String>();
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = in.readLine())!=null)
			{
				result.add(line);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static ArrayList<String> read(InputStream stream, int timeOut)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));

		while (true)
		{
			Future<String> future = executor.submit(new P().new ReadWithTimeout(in));
			try
			{
				result.add(future.get(timeOut, TimeUnit.SECONDS));
			}
			catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
			catch (TimeoutException e)
			{
				future.cancel(true);
				break;
			}
		}
		
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		executor.shutdownNow();
		
		return result;
	}
	
	public class ReadWithTimeout implements Callable<String> {
		BufferedReader in;
		public ReadWithTimeout(BufferedReader reader)
		{
			in = reader;
		}
		@Override
		public String call() throws Exception
		{
			return in.readLine();
		}
	}
}
