package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class HeadComponent extends JPanel 
{
	private JComboBox vueCombo;
	private JComboBox queryCombo;
	
	public HeadComponent()
	{
		super();
		build();
	}

	private void build()
	{
		setLayout(new FlowLayout());
		String[] vueStrings = { "Vue modèle", "Vue tableau", "Vue chemin" };
		vueCombo = new JComboBox(vueStrings);
		add(vueCombo);
		JButton refreshBtn = new JButton("Rafraichir");
		add(refreshBtn);
		String[] queryStrings = { "Entrez une requête...", "SELECT ?a ?b ?c WHERE {?a ?b ?c}", "SELECT ?a WHERE {?a rdf:type virus:VIRUS}" };
		queryCombo = new JComboBox(queryStrings);
		queryCombo.setPreferredSize(new Dimension(700, 30));
		queryCombo.setEditable(true);
		add(queryCombo);
	}
	
	public String getRequete()
	{
		return queryCombo.getSelectedItem().toString();
	}
	
	public JComboBox getQueryCombo()
	{
		return queryCombo;
	}
	
	public JComboBox getVueCombo()
	{
		return vueCombo;
	}
}
