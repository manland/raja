package Server.Indoor.Graphic.ViewInTab.ViewServer;

import java.awt.Color;

import Server.Adapter.IAdapter;
import Server.Adapter.IListenerAdapter;

public class VueCompositeAdapter extends Vue implements IListenerAdapter 
{

	public VueCompositeAdapter(String nom, int x, int y) 
	{
		super(nom, x, y);
		fond = Color.white;
		texte = Color.black;
		bordure = Color.black;
	}

	@Override
	public void goIn(IAdapter adapter) 
	{
		isGoIn = true;
		repaint();
	}

	@Override
	public void goOut(IAdapter adapter) 
	{
		isGoOut = true;
		repaint();
	}

}
