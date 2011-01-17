package Server.Indoor.Graphic;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.LightweightPeer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import Query.SelectQuery;
import Server.Server;
import Server.Indoor.IndoorFile;
import Server.Indoor.Graphic.ViewInTab.PanelInTab;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

public class MyTabbedPane extends JTabbedPane
{

	public MyTabbedPane()
	{
		super();
		build();
		Server.getInstance().init("bin/config.xml", new IndoorFile("bin/tests.txt","bin/out.txt"));
	}

	private void build()
	{
		firstTab();
		addClosableTab("test");
	}

	private JPanel buildContentPane()
	{
		return new PanelInTab();
	}

	private void firstTab() 
	{
		addTab(null, new JPanel());
		JButton addTabButton = new JButton("+");
		ActionListener al = new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				addClosableTab("test2");
			}
		};
		addTabButton.addActionListener(al);
		JPanel pnl = new JPanel();
		pnl.setMaximumSize(new Dimension(5, 5));
		pnl.setOpaque(false);
		pnl.add(addTabButton);
		setTabComponentAt(getTabCount() - 1, pnl);
		setSelectedIndex(getTabCount() - 1);
	}

	private void addClosableTab(String nom) 
	{
		JPanel p = buildContentPane();
		JScrollPane scroll = new JScrollPane(p);
		addTab(null, scroll);
		JLabel lbl = new JLabel(nom);
		JButton closeTabButton = new JButton("x");
		closeTabButton.setActionCommand("" + getTabCount());
		ActionListener al = new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				JButton btn = (JButton) ae.getSource();
				String s1 = btn.getActionCommand();
				for (int i = 1; i < getTabCount(); i++) 
				{
					JPanel pnl = (JPanel) getTabComponentAt(i);
					btn = (JButton) pnl.getComponent(1);
					String s2 = btn.getActionCommand();
					if (s1.equals(s2)) 
					{
						removeTabAt(i);
						break;
					}
				}
			}
		};
		closeTabButton.addActionListener(al);
		JPanel pnl = new JPanel();
		pnl.setMaximumSize(new Dimension(5, 5));
		pnl.setOpaque(false);
		pnl.add(lbl);
		pnl.add(closeTabButton);
		setTabComponentAt(getTabCount() - 1, pnl);
		setSelectedIndex(getTabCount() - 1);
	}
}
