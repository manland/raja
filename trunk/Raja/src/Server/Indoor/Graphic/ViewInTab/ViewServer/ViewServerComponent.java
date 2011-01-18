package Server.Indoor.Graphic.ViewInTab.ViewServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JLayeredPane;

import Query.Pair;
import Server.IVisiteur;
import Server.Server;
import Server.Adapter.CompositeAdapter;
import Server.Adapter.IAdapter;
import Server.Adapter.TerminalAdapter;
import Server.Translator.ITranslator;
import Server.Translator.Translator;

public class ViewServerComponent extends JLayeredPane implements IVisiteur
{
	int x = 0;
	int y = 0;
	int largeur = 800;
	int gap = 0;
	int width_f = 100;
	protected Color aller = Color.red;
	protected Color retour = Color.green;
	
	public ViewServerComponent()
	{
		super();
		build();
	}
	
	private void build()
	{
		setLayout(null);
		setBackground(new Color(255,255,255));
		setVisible(true);
		setOpaque(true);
		Dimension dimension = new Dimension(800, 600);
		setPreferredSize(dimension);
		setSize(dimension);
		setMinimumSize(dimension);
	}

	
	public void dessine()
	{
		x = 0;
		y = 50;
		positions = new HashMap<IAdapter, Pair<Integer, Integer>>();
		positionsTranslator = new HashMap<ITranslator, Pair<Integer,Integer>>();
		
		Server.getInstance().acceptVisitor(this);
		
		Dimension dimension = new Dimension(x+800, y+600);
		setPreferredSize(dimension);
		setSize(dimension);
		setMinimumSize(dimension);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(aller);
		int x_tab [] = {27, 13, 40};
		int y_tab [] = {70, 60, 60};
		g2d.fillPolygon(x_tab, y_tab, 3);
		g2d.fillRect(20, 10, 15, 50);
		g2d.setPaint(retour);
		int x_tab2 [] = {87, 72, 102};
		int y_tab2 [] = {0, 10, 10};
		g2d.fillPolygon(x_tab2, y_tab2, 3);
		g2d.fillRect(80, 10, 15, 50);
		
		g2d.setPaint(Color.black);
		g2d.drawString("aller", 10, 85);
		g2d.drawString("retour", 70, 85);
	}

	@Override
	public void visitServer(Server server) 
	{
		x = (int)largeur/2;
		VueServer s = new VueServer("Server", x, y);
		add(s);
		server.addListener(s);
		y += 100;
	}
	
	private HashMap<IAdapter, Pair<Integer, Integer>> positions;
	private HashMap<ITranslator, Pair<Integer, Integer>> positionsTranslator;
	
	@Override
	public void visitBeforeCompositeAdapter(CompositeAdapter compositeAdapter) 
	{
		String [] l = compositeAdapter.getFile().split("/");
		String nom = l[l.length-1];
		if(positions.containsKey(compositeAdapter))
		{
			Pair<Integer, Integer> p = positions.get(compositeAdapter);
			VueCompositeAdapter v = new VueCompositeAdapter(nom, p.getFirst()+60, p.getSecond());
			add(v);
			compositeAdapter.addListener(v);
			for(int i=0; i<compositeAdapter.getSubAdapters().size(); i++)
			{
				Pair<Integer, Integer> pp = new Pair<Integer, Integer>((p.getFirst() + (i*110)), p.getSecond()+100);
				positions.put(compositeAdapter.getSubAdapters().get(i), pp);
			}
//			int gap = (p.getFirst()*2)+width_f;
//			for(int i=0; i<compositeAdapter.getSubAdapters().size(); i++)
//			{
//				int x = ((gap - width_f)/2)+(i*gap);
//				Pair<Integer, Integer> pp = new Pair<Integer, Integer>(x, p.getSecond()+100);
//				positions.put(compositeAdapter.getSubAdapters().get(i), pp);
//			}
		}
		else
		{
			VueCompositeAdapter v = new VueCompositeAdapter(nom, x, y);
			add(v);
			compositeAdapter.addListener(v);
			int gap = largeur/compositeAdapter.getSubAdapters().size();
			for(int i=0; i<compositeAdapter.getSubAdapters().size(); i++)
			{
				int x = ((gap - width_f)/2)+(i*gap);
				Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y+100);
				positions.put(compositeAdapter.getSubAdapters().get(i), p);
			}
		}
	}
	
	@Override
	public void visitAfterCompositeAdapter(CompositeAdapter compositeAdapter) 
	{
	}

	@Override
	public void visitTerminalAdapter(TerminalAdapter terminalAdapter) 
	{
		String [] l = terminalAdapter.getFile().split("/");
		String nom = l[l.length-1];
		if(positions.containsKey(terminalAdapter))
		{
			Pair<Integer, Integer> p = positions.get(terminalAdapter);
			VueTerminalAdapter t = new VueTerminalAdapter(nom, p.getFirst(), p.getSecond());
			add(t);
			terminalAdapter.addListener(t);
			positionsTranslator.put(terminalAdapter.getTranslator(), p);
		}
	}

	@Override
	public void visitTranslator(Translator translator) 
	{
		if(positionsTranslator.containsKey(translator))
		{
			Pair<Integer, Integer> p = positionsTranslator.get(translator);
			VueTranslator v = new VueTranslator(translator.getDataBase().getDatabaseName(), p.getFirst(), p.getSecond()+100);
			add(v);
			translator.addListener(v);
		}
	}

}
