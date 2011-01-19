package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Server.Server;

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
		String[] vueStrings = { "Vue chemin", "Vue modèle", "Vue tableau" };
		vueCombo = new JComboBox(vueStrings);
		add(vueCombo);
		JButton refreshBtn = new JButton("Rafraichir");
		add(refreshBtn);
		queryCombo = new JComboBox(parseForQuery("bin/tests.txt"));
		queryCombo.setPreferredSize(new Dimension(700, 30));
		queryCombo.setEditable(true);
		add(queryCombo);
	}
	
	private Vector<String> parseForQuery(String file)
	{
		Vector<String> res = new Vector<String>();
		res.add("Entrez une requête...");
		res.add("SELECT ?a ?b ?c WHERE {?a ?b ?c}");
		res.add("schéma global");
		InputStream ips = null;
		try 
		{
			ips = new FileInputStream(file);
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println(e.getMessage());
		}
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader brIn = new BufferedReader(ipsr);
		String line="";
		try 
		{
			while(line != null)
			{
				line = brIn.readLine();
				if(line != null)
				{
					if(!line.startsWith("#"))
					{
						res.add(line);
					}
				}
			}
			line = "";
			brIn.close(); 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return res;
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