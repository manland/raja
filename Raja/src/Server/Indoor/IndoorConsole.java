package Server.Indoor;

import java.util.Scanner;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class IndoorConsole implements IInDoor {

	@Override
	public String read() {
		String line = "";
		Scanner in = new Scanner(System.in);
		line = in.nextLine();

		if(line.equals("q"))
		{
			in.close(); 
			line = "";
		}
		return line;
	}

	@Override
	public void write(ResultSet rs, Query q) {
		ResultSetFormatter.out(System.out, rs, q);
	}
}
