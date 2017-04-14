package server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Random;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HTTPServer {
	
	public static void start()
	{
		try
		{
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        final File image1 = new File("/home/wenhaoc/Pictures/small.jpg");
        final File image2 = new File("/home/wenhaoc/Pictures/large.jpg");
        server.createContext("/test", new HttpHandler() {

			@Override
			public void handle(HttpExchange exc) throws IOException
			{
				
	            
	            File image = new Random().nextBoolean()? image1 : image2;
	            exc.sendResponseHeaders(200, image.length());
	            OutputStream os = exc.getResponseBody();
	            os.write(Files.readAllBytes(image.toPath()));
	            os.close();
			}
        	
        });
        server.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
