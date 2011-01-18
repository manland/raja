package Server.Indoor.Graphic.ViewInTab.ViewOwl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class EntiteRDF extends JComponent {
	
	private String nom;
	private Color fond = Color.white;
	private Color texte = Color.black;
	private Color bordure = Color.black;
	
	public EntiteRDF(String nom, int x, int y)
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
//		if(modele.getLargeur() < g2d.getFontMetrics().stringWidth(modele.getFichier().getNom())) {
//			modele.setLargeur(g2d.getFontMetrics().stringWidth(modele.getFichier().getNom()));
//		}
		g2d.setPaint(fond);
		g2d.fillOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.setPaint(bordure);
		g2d.drawOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.setPaint(texte);
//		g2d.setFont(g2d.getFont().deriveFont((float)getHeight()-10));
		String a_dessiner = nom;
		if(a_dessiner.contains("#")) {
			a_dessiner = a_dessiner.split("#")[1];
		}
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

}
