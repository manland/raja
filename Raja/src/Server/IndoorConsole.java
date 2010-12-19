package Server;

import java.util.Scanner;

class IndoorConsole implements IInDoor {

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
	public void write(String result) {
		System.out.println(result);
	}
}
