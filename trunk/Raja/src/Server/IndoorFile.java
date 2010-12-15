package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class IndoorFile implements IInDoor {

	private String outFile;
	
	private BufferedReader brIn;
	
	public IndoorFile(String inFile, String outFile)
	{
		this.outFile = outFile;
		InputStream ips = null;
		try {
			ips = new FileInputStream(inFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader ipsr = new InputStreamReader(ips);
		brIn = new BufferedReader(ipsr);
	}
	
	@Override
	public String read() {
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
	public void write(String result) {
		try {
			FileWriter fw = new FileWriter(outFile);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter fichierSortie = new PrintWriter(bw); 
			fichierSortie.println (result+"\n"); 
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

}
