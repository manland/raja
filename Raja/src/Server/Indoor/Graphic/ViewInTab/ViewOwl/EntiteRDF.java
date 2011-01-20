package Server.Indoor.Graphic.ViewInTab.ViewOwl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class EntiteRDF extends JComponent implements MouseListener, MouseMotionListener {
	
	private String nom;
	private Color fond = Color.white;
	private Color texte = Color.lightGray;
	private Color bordure = Color.lightGray;
	private Vector<ProprieteRDF> proprietes;
	private boolean selectionne;
	private JLayeredPane pere;
	private int ancien_z_order;
	
	public EntiteRDF(JLayeredPane pere, String nom, Color fond)
	{
		this.pere = pere;
		this.nom = nom;
		this.fond = fond;
		Dimension dimension = new Dimension(100, 50);
		setSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
		proprietes = new Vector<ProprieteRDF>();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addPropriete(ProprieteRDF p)
	{
		proprietes.add(p);
	}
	
	public Vector<ProprieteRDF> getProprietes()
	{
		return proprietes;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(fond);
		g2d.fillOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.setPaint(bordure);
		g2d.drawOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.setPaint(texte);
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
	
	public boolean isSelectionne() {
		return selectionne;
	}

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
		if(selectionne)
		{
			bordure = Color.black;
			texte = Color.black;
		}
		else
		{
			bordure = Color.lightGray;
			texte = Color.lightGray;
		}
		repaint();
	}
	
	public void selectionMoiEtTousMesFils(boolean selectionne)
	{
		setSelectionne(selectionne);
		for(ProprieteRDF propriete : proprietes)
		{
			propriete.selectionMoiEtTousMesFils(selectionne);
		}
	}
	
	public void setXPlusEnfants(int x)
	{
		setX(x);
		for(ProprieteRDF propriete : proprietes)
		{
			propriete.setX(x);
		}
	}
	
	public void setYPlusEnfants(int y)
	{
		setY(y);
		for(ProprieteRDF propriete : proprietes)
		{
			propriete.setY(y);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if(event.getClickCount()>=2) {
			selectionMoiEtTousMesFils(!isSelectionne());
		}
		else if(event.getClickCount() == 1 && event.getButton() == 1) {
			setSelectionne(!isSelectionne());
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		ancien_z_order = pere.getComponentZOrder(this);
		pere.setComponentZOrder(this, 0);
		pere.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
//		if(e.getY() > 0 || panel_onglet.getBoutonsNoeud() == null) {
			pere.setComponentZOrder(this, ancien_z_order);
//			panel_onglet.enleverBoutonsNoeud();
//		}
//		else if(e.getY() > 0 || panel_onglet.getBoutonsNoeud() != null) {
//			if(panel_onglet.getBoutonsNoeud().getNoeud() != this) {
//				panel_onglet.setComponentZOrder(this, ancien_z_order);
//				panel_onglet.enleverBoutonsNoeud();
//			}
//		}
	}

	private int decalage_x;
	private int decalage_y;
	private int type_bouton_souris;
	@Override
	public void mousePressed(MouseEvent event) {
		decalage_x = event.getX();
		decalage_y = event.getY();
		type_bouton_souris = event.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		int deplacement_x = getLocationOnScreen().x - evt.getXOnScreen();
		int deplacement_y = getLocationOnScreen().y - evt.getYOnScreen();
		if(type_bouton_souris == 1) {//click droit
			setX(getX() - deplacement_x - decalage_x);
			setY(getY() - deplacement_y - decalage_y);
		}
		else if(type_bouton_souris == 3) {//click gauche
//			if(modele.getX() - (deplacement_x + decalage_x) >= 0) {
				setXPlusEnfants(getX() - deplacement_x - decalage_x);
//			}
//			if(modele.getY() - (deplacement_y + decalage_y) >= 0) {
				setYPlusEnfants(getY() - deplacement_y - decalage_y);
//			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
	}

}
