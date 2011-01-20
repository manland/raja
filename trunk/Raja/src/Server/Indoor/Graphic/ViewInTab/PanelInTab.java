package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Server.Indoor.Graphic.IEcouteurServerThread;
import Server.Indoor.Graphic.ServerThread;
import Server.Indoor.Graphic.ViewInTab.ViewOwl.ViewOwlComponent;
import Server.Indoor.Graphic.ViewInTab.ViewServer.ViewServerComponent;
import Server.Indoor.Graphic.ViewInTab.ViewTab.ViewTabComponent;

import com.hp.hpl.jena.rdf.model.Model;

public class PanelInTab extends JPanel implements ItemListener, IEcouteurServerThread {
	
	private HeadComponent headComponent;
	private ViewOwlComponent viewOwlComponent;
	private MenuPropriete menuPropriete;
	private ViewServerComponent viewServerComponent;
	private JComponent selectComponent;
	private ViewTabComponent viewTabComponent;
	
	public PanelInTab()
	{
		setLayout(new BorderLayout());
		headComponent = new HeadComponent();
		headComponent.getQueryCombo().addItemListener(this);
		headComponent.getVueCombo().addItemListener(this);
		headComponent.getBtnRefresh().addItemListener(this);
		add(headComponent, BorderLayout.PAGE_START);
		viewOwlComponent = new ViewOwlComponent();
		viewTabComponent = new ViewTabComponent();
		viewServerComponent = new ViewServerComponent();
		viewServerComponent.dessine();
		add(viewServerComponent, BorderLayout.CENTER);
		selectComponent = viewServerComponent;
		menuPropriete = new MenuPropriete();
	}

	private String lastRequete = "";
	private Model model;

	private void callServeur()
	{
		ServerThread server = new ServerThread(lastRequete, this);
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
	
	@Override
	public void itemStateChanged(ItemEvent evt) {
		try
		{
			JComboBox comboBox = (JComboBox)evt.getItemSelectable();
			if(comboBox == headComponent.getQueryCombo())
			{
				String requete = comboBox.getSelectedItem().toString();
				if(!requete.equals("Entrez une requête...") && !requete.equals(lastRequete))
				{
					headComponent.setInfos("Calcul en cours...");
					lastRequete = requete;
					callServeur();
				}
			}
			else if(comboBox == headComponent.getVueCombo())
			{
				String vue = comboBox.getSelectedItem().toString();
				remove(selectComponent);
				if(selectComponent == viewOwlComponent)
				{
					remove(menuPropriete);
				}
				if(vue.equals("Vue modèle"))
				{
					add(viewOwlComponent, BorderLayout.CENTER);
					add(menuPropriete, BorderLayout.LINE_START);
					selectComponent = viewOwlComponent;
				}
				else if(vue.equals("Vue tableau"))
				{
					add(viewTabComponent, BorderLayout.CENTER);
					selectComponent = viewTabComponent;
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
		catch(ClassCastException e)
		{
			JButton btnRefresh = (JButton)evt.getItemSelectable();
			System.out.println("classCastE");
			if(btnRefresh == headComponent.getBtnRefresh())
			{
				System.out.println("refresh");
				callServeur();
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
		menuPropriete.repaint();
		viewTabComponent.build(server.getModel(), lastRequete);
		validate();
		headComponent.setInfos(server.getTime()+" msc");
	}

}
