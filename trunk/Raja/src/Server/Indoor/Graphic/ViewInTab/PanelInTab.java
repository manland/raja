package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Server.Indoor.Graphic.IEcouteurServerThread;
import Server.Indoor.Graphic.ServerThread;
import Server.Indoor.Graphic.ViewInTab.ViewOwl.ViewOwlComponent;

import com.hp.hpl.jena.rdf.model.Model;

public class PanelInTab extends JPanel implements ItemListener, IEcouteurServerThread {
	
	private ViewOwlComponent viewOwlComponent;
	private MenuPropriete menuPropriete;
	
	public PanelInTab()
	{
		setLayout(new BorderLayout());
		HeadComponent headComponent = new HeadComponent();
		headComponent.getQueryCombo().addItemListener(this);
		add(headComponent, BorderLayout.PAGE_START);
		viewOwlComponent = new ViewOwlComponent();
		add(viewOwlComponent, BorderLayout.CENTER);
		menuPropriete = new MenuPropriete();
		JPanel menuDroite = new JPanel();
		menuDroite.add(menuPropriete);
		add(menuDroite, BorderLayout.LINE_START);
	}

	private String lastRequete = "";
	private Model model;

	@Override
	public void itemStateChanged(ItemEvent evt) {
		JComboBox comboBox = (JComboBox)evt.getItemSelectable();
		final String requete = comboBox.getSelectedItem().toString();
		System.out.println(requete);
		if(!requete.equals(lastRequete))
		{
			lastRequete = requete;
			ServerThread server = new ServerThread(requete);
			server.addEcouteur(this);
			if(SwingUtilities.isEventDispatchThread())
			{
				SwingUtilities.invokeLater(server);
			}
			else
			{
				server.start();
			}
		}
	}

	@Override
	public void finish(ServerThread server) 
	{
		viewOwlComponent.dessine(server.getModel());
		viewOwlComponent.lignesDessous();
		menuPropriete.build(viewOwlComponent.getProprietes());
		menuPropriete.validate();
		menuPropriete.invalidate();
		menuPropriete.repaint();
		invalidate();
	}

}
