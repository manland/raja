package Server.Indoor.Graphic;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame
{

	public MainWindow()
	{
		super();
		build();
	}
	
	private void build()
	{
		setTitle("Bases de données distribuées");
		setSize(800,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
	}
	
	private JPanel buildContentPane(){
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(new MyTabbedPane());
		return panel;
	}	
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				MainWindow mw = new MainWindow();
				mw.setVisible(true);
			}
		});
	}
}
