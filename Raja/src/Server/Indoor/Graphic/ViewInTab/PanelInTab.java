package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Server.Indoor.Graphic.IEcouteurServerThread;
import Server.Indoor.Graphic.ServerThread;
import Server.Indoor.Graphic.ViewInTab.ViewOwl.ViewOwlComponent;
import Server.Indoor.Graphic.ViewInTab.ViewServer.ViewServerComponent;

import com.hp.hpl.jena.rdf.model.Model;

public class PanelInTab extends JPanel implements ItemListener, IEcouteurServerThread {
	
	private HeadComponent headComponent;
	private ViewOwlComponent viewOwlComponent;
	private MenuPropriete menuPropriete;
	private ViewServerComponent viewServerComponent;
	private JComponent selectComponent;
	
	public PanelInTab()
	{
		setLayout(new BorderLayout());
		headComponent = new HeadComponent();
		headComponent.getQueryCombo().addItemListener(this);
		headComponent.getVueCombo().addItemListener(this);
		add(headComponent, BorderLayout.PAGE_START);
		viewOwlComponent = new ViewOwlComponent();
		viewServerComponent = new ViewServerComponent();
		viewServerComponent.dessine();
		add(viewServerComponent, BorderLayout.CENTER);
		selectComponent = viewServerComponent;
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
		if(comboBox == headComponent.getQueryCombo())
		{
			String requete = comboBox.getSelectedItem().toString();
			if(!requete.equals("Entrez une requête...") && !requete.equals(lastRequete))
			{
				lastRequete = requete;
				ServerThread server = new ServerThread(requete, this);
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
		else if(comboBox == headComponent.getVueCombo())
		{
			String vue = comboBox.getSelectedItem().toString();
			remove(selectComponent);
			if(vue.equals("Vue modèle"))
			{
				add(viewOwlComponent, BorderLayout.CENTER);
				selectComponent = viewOwlComponent;
			}
			else if(vue.equals("Vue tableau"))
			{
				add(viewServerComponent, BorderLayout.CENTER);
				selectComponent = viewServerComponent;
			}
			else if(vue.equals("Vue chemin"))
			{
				add(viewServerComponent, BorderLayout.CENTER);
				selectComponent = viewServerComponent;
			}
			validate();
			repaint();
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
