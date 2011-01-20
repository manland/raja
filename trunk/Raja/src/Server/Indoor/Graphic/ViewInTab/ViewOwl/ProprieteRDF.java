package Server.Indoor.Graphic.ViewInTab.ViewOwl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class ProprieteRDF extends JComponent 
{
	
	private String nom;
	private EntiteRDF rdf1;
	private EntiteRDF rdf2;
	private Color color = Color.lightGray;
	private boolean selectionne;
	
	public ProprieteRDF(String nom, EntiteRDF rdf1, EntiteRDF rdf2)
	{
		this.nom = nom;
		this.rdf1 = rdf1;
		this.rdf2 = rdf2;
		calibre();
	}
	
	protected void paintComponent(Graphics g) {
		calibre();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(color);
		int poignet_x_milieu = rdf2.getX()+(rdf2.getWidth()/2);
		int rdf1_x_milieu = rdf1.getX() + (rdf1.getHeight()/2);
		int rdf1_y_bas = rdf1.getY() + rdf1.getHeight();
		if((rdf1_x_milieu<poignet_x_milieu && rdf1_y_bas<rdf2.getY()) ||
				(rdf1_x_milieu>poignet_x_milieu && rdf1_y_bas>rdf2.getY())) {
			//le lien va de gauche a droite
			g.drawLine(0, 0, getWidth(), getHeight());
		}
		else {
			//le lien va de droite à gauche
			g.drawLine(0, getHeight(), getWidth(), 0);
		}
		
	}
	
	private void calibre()
	{
		int poignet_x_milieu = rdf2.getX()+(rdf2.getWidth()/2);
		int rdf1_x_milieu = rdf1.getX() + (rdf1.getHeight()/2);
		int rdf1_y_bas = rdf1.getY() + rdf1.getHeight();
		Dimension dimension = new Dimension(Math.max(rdf1_x_milieu, poignet_x_milieu)-
				Math.min(rdf1_x_milieu, poignet_x_milieu), 
				Math.max(rdf1_y_bas, rdf2.getY())-
				Math.min(rdf1_y_bas, rdf2.getY()));
		if(dimension.getWidth() <= 0) {
			dimension.setSize(1, dimension.getHeight());
		}
		if(dimension.getHeight() <= 0) {
			dimension.setSize(dimension.getWidth(), 1);
		}
		
		setLocation(Math.min(rdf1_x_milieu, poignet_x_milieu), 
				Math.min(rdf1_y_bas, rdf2.getY()));
		
		setSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
	}

	public void setX(int x) 
	{
		setLocation(x, getY());
		if(rdf1.getX() != x)
		{
			rdf1.setX(x);
		}
		if(rdf2.getX() != x)
		{
			rdf2.setX(x);
		}
		repaint();
	}

	public void setY(int y) 
	{
		setLocation(getX(), y);
		if(rdf1.getY() != y)
		{
			rdf1.setY(y);
		}
		if(rdf2.getY() != y)
		{
			rdf2.setY(y);
		}
		repaint();
	}

	public String getNom() 
	{
		return nom;
	}
	
	public boolean isSelectionne() {
		return selectionne;
	}
	
	public void setColor(Color c)
	{
		color = c;
		repaint();
	}
	
	private Color beforeSelectionne;

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
		if(selectionne)
		{
			beforeSelectionne = color;
			color = Color.black;
		}
		else
		{
			color = beforeSelectionne;
		}
		repaint();
	}
	
	public void selectionMoiEtTousMesFils(boolean selectionne)
	{
		if(isVisible())
		{
			setSelectionne(selectionne);
			if(rdf1.isSelectionne() != selectionne)
			{
				rdf1.setSelectionne(selectionne);
			}
			if(rdf2.isSelectionne() != selectionne)
			{
				rdf2.setSelectionne(selectionne);
			}
		}
	}
	
}
