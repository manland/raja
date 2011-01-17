package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Server.Indoor.Graphic.ViewInTab.ViewOwl.ProprieteRDF;

public class MenuPropriete extends JPanel implements ItemListener 
{
	private HashMap<String, Vector<ProprieteRDF>> proprietes;

	public MenuPropriete()
	{
		super(new GridLayout(0, 1));
	}

	public void build(HashMap<String, Vector<ProprieteRDF>> proprietes)
	{
		this.proprietes = proprietes;
		removeAll();
		JCheckBox check;
		for(Vector<ProprieteRDF> vectorProprietes : proprietes.values())
		{
			ProprieteRDF propriete = vectorProprietes.get(0);
			String nom = propriete.getNom();
			if(nom.contains("#")) {
				nom = nom.split("#")[1];
			}
			check = new JCheckBox(nom);
			check.setActionCommand(propriete.getNom());
			check.setSelected(true);
			check.addItemListener(this);
			add(check);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		JCheckBox source = (JCheckBox)e.getItemSelectable();
		Vector<ProprieteRDF> vectorProprietes = null;
		if(proprietes.containsKey(source.getActionCommand()))
		{
			vectorProprietes = proprietes.get(source.getActionCommand());
		}
		if(vectorProprietes != null && source.isSelected())
		{
			for(ProprieteRDF propriete : vectorProprietes)
			{
				propriete.setVisible(true);
			}
		}
		else if(vectorProprietes != null)
		{
			for(ProprieteRDF propriete : vectorProprietes)
			{
				propriete.setVisible(false);
			}
		}
	}

}
