package Server.Indoor.Graphic.ViewInTab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class PanelInTab extends JPanel implements ItemListener, IEcouteurServerThread, ActionListener {

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
		headComponent.getBtnRefresh().setEnabled(false);//pour pas clicker avant d'avoir choisis une première requete
		headComponent.getBtnRefresh().addActionListener(this);
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

	private void callServeur()
	{
		ServerThread server = new ServerThread(lastRequete, this);
		server.addEcouteur(this);
		headComponent.setInfos("Calcul en cours...");
		viewServerComponent.redessine();
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
		JComboBox comboBox = (JComboBox)evt.getItemSelectable();
		if(comboBox == headComponent.getQueryCombo())
		{
			String requete = comboBox.getSelectedItem().toString();
			if(!requete.equals("Entrez une requête...") && !requete.equals(lastRequete))
			{
				lastRequete = requete;
				if(!headComponent.getBtnRefresh().isEnabled())
				{
					headComponent.getBtnRefresh().setEnabled(true);
				}
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
		viewServerComponent.stopDessin();
	}

	@Override
	public void actionPerformed(ActionEvent evt) 
	{
		if(!lastRequete.isEmpty())
		{
			callServeur();
		}
	}

}
