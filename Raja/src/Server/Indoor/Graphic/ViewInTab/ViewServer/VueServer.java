package Server.Indoor.Graphic.ViewInTab.ViewServer;

import java.awt.Color;

import javax.swing.JComponent;

import Query.IQuery;
import Server.IListenerServer;
import Server.Server;

public class VueServer extends Vue implements IListenerServer {
	
	public VueServer(String nom, int x, int y)
	{
		super(nom, x, y);
		fond = Color.white;
		texte = Color.black;
		bordure = Color.black;
	}

	@Override
	public void call(Server server, IQuery query) {
		isGoIn = true;
		repaint();
	}

	@Override
	public void finish(Server server, IQuery query) {
		isGoOut = true;
		repaint();
	}

	@Override
	public void initialization(Server server) {
		// TODO Auto-generated method stub
		
	}
}
