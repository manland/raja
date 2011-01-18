package Server.Indoor.Graphic.ViewInTab.ViewServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Vue extends JComponent {
	
	protected String nom;
	protected Color fond = Color.white;
	protected Color texte = Color.black;
	protected Color bordure = Color.black;
	protected Color aller = Color.red;
	protected Color retour = Color.green;
	protected boolean isGoIn = false;
	protected boolean isGoOut = false;
	
	public Vue(String nom, int x, int y)
	{
		this.nom = nom;
		Dimension dimension = new Dimension(100, 50);
		setSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
		setX(x);
		setY(y);
		setLocation(x, y);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(fond);
		g2d.fillRect(0, 0, getWidth()-1, getHeight()-1);
		if(isGoIn)
		{
			g2d.setPaint(aller);
			g2d.fillRect(0, 0, (getWidth()-1)/2, getHeight()-1);
		}
		if(isGoOut)
		{
			g2d.setPaint(retour);
			g2d.fillRect((getWidth())/2, 0, (getWidth()-1)/2, getHeight()-1);
		}
		g2d.setPaint(bordure);
		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		g2d.setPaint(texte);
		String a_dessiner = nom;
		if(a_dessiner.length()>8)
		{
			a_dessiner = a_dessiner.substring(0, 8) + "...";
		}
		g2d.drawString(a_dessiner, (getWidth()-(g2d.getFontMetrics().stringWidth(a_dessiner)))/2, (getHeight()/2)+(g2d.getFontMetrics().getHeight()/8));
	}
	
	public void setX(int x) {
		setLocation(x, getY());
	}

	public void setY(int y) {
		setLocation(getX(), y);
	}
	
	public void clean()
	{
		isGoIn = false;
		isGoOut = false;
		repaint();
	}

}
