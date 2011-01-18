package Server.Indoor.Graphic.ViewInTab.ViewServer;

import java.awt.Color;

import Server.Translator.IListenerTranslator;
import Server.Translator.ITranslator;

public class VueTranslator extends Vue implements IListenerTranslator {

	public VueTranslator(String nom, int x, int y) {
		super(nom, x, y);
		fond = Color.white;
		texte = Color.black;
		bordure = Color.black;
	}

	@Override
	public void goIn(ITranslator adapter) 
	{
		isGoIn = true;
		repaint();
	}

	@Override
	public void goOut(ITranslator adapter) 
	{
		isGoOut = true;
		repaint();
	}

}
