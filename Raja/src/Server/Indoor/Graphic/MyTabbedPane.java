package Server.Indoor.Graphic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import Exception.DataBaseNotAccessibleException;
import Server.Server;
import Server.Indoor.IndoorFile;
import Server.Indoor.Graphic.ViewInTab.PanelInTab;

public class MyTabbedPane extends JTabbedPane
{

	public MyTabbedPane()
	{
		super();
		try 
		{
			Server.getInstance().init("bin/config.xml", null);
		} 
		catch (DataBaseNotAccessibleException e) 
		{
			JOptionPane.showMessageDialog(this,
				    e.getMessage(),
				    e.getClass().getSimpleName(),
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		build();
	}

	private void build()
	{
		firstTab();
		addClosableTab("Recherche");
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
				addClosableTab("Recherche");
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
