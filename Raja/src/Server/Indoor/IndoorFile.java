package Server.Indoor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class IndoorFile implements IInDoor 
{
	private BufferedReader brIn;
	private OutputStream ops;
	
	public IndoorFile(String inFile, String outFile)
	{
		InputStream ips = null;
		ops = null;
		try 
		{
			ips = new FileInputStream(inFile);
			ops = new FileOutputStream(outFile);
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println(e.getMessage());
		} 
		InputStreamReader ipsr = new InputStreamReader(ips);
		brIn = new BufferedReader(ipsr);
	}
	
	@Override
	public String read() 
	{
		String line="";
		try 
		{
			line = brIn.readLine();
			if(line == null)
			{
				line = "";
				brIn.close(); 
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return line;
	}

	@Override
	public void write(ResultSet rs, Query q) 
	{
		ResultSetFormatter.out(ops, rs, q);
	}

}
