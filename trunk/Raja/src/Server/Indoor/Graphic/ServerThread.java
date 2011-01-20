package Server.Indoor.Graphic;

import java.util.Timer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import Server.Server;

import com.hp.hpl.jena.rdf.model.Model;

public class ServerThread extends Thread implements Runnable {

	private Model model;
	private String requete;
	private Vector<IEcouteurServerThread> ecouteurs;
	private JComponent errorParent;
	private long time;
	
	public ServerThread(String requete, JComponent errorParent)
	{
		this.requete = requete;
		setPriority(Thread.NORM_PRIORITY);
		ecouteurs = new Vector<IEcouteurServerThread>();
		this.errorParent = errorParent;
	}
	
	public void addEcouteur(IEcouteurServerThread ecouteur)
	{
		ecouteurs.add(ecouteur);
	}
	
	public void run()
	{
		long debut = System.currentTimeMillis(); 
		try
		{
			if(requete.equals("schéma global"))
			{
				model = Server.getInstance().getGlobalSchema();
			}
			else
			{
				model = Server.getInstance().call(requete);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(errorParent, 
				    e.getMessage(),
				    e.getClass().getSimpleName(),
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		time = System.currentTimeMillis() - debut;
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
	
	public long getTime()
	{
		return time;
	}
}
