package Server.Indoor.Graphic;

import java.util.Vector;

import Server.Server;

import com.hp.hpl.jena.rdf.model.Model;

public class ServerThread extends Thread implements Runnable {

	private Model model;
	private String requete;
	private Vector<IEcouteurServerThread> ecouteurs;
	
	public ServerThread(String requete)
	{
		this.requete = requete;
		setPriority(Thread.NORM_PRIORITY);
		ecouteurs = new Vector<IEcouteurServerThread>();
	}
	
	public void addEcouteur(IEcouteurServerThread ecouteur)
	{
		ecouteurs.add(ecouteur);
	}
	
	public void run()
	{
		model = Server.getInstance().call(requete);
		fireFinish();
	}
	
	private void fireFinish()
	{
		for(int i = 0; i<ecouteurs.size(); i++)
		{
			ecouteurs.get(i).finish(this);
		}
	}
	
	public Model getModel()
	{
		return model;
	}
}
