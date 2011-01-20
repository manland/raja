package Server.Indoor.Graphic.ViewInTab.ViewTab;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Query.Pair;
import Server.Server;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class ViewTabComponent extends JPanel {

	private JScrollPane scrollPane;
	private JTable table;
	
	public ViewTabComponent()
	{
		super();
	}
	
	public void build(Model model, String requete)
	{
		if(scrollPane != null)
		{
			remove(scrollPane);
		}
		Vector<Pair<String, String>> prefix = Server.getInstance().getPrefix();
		String ma_requete = "";
		for(int i=0; i<prefix.size(); i++)
		{
			ma_requete = ma_requete+"PREFIX "+prefix.get(i).getFirst()+":<"+prefix.get(i).getSecond()+">\n";
		}
		ma_requete = ma_requete + requete;
		Query q = QueryFactory.create(ma_requete.replace("SELECT2", "SELECT"));
		QueryExecution qexec = QueryExecutionFactory.create(q,model);
		ResultSet rs = qexec.execSelect();
		List var = rs.getResultVars();
		QuerySolution solution = null;
		Vector<Vector<String>> rowData = new Vector<Vector<String>>();
		while(rs.hasNext())
		{
			solution = rs.nextSolution();
			Vector<String> data = new Vector<String>();
			for(int i=0; i<var.size(); i++)
			{
				String res = solution.get(var.get(i).toString()).toString();
				if(res.contains("#"))
				{
					res = res.split("#")[1];
				}
				data.add(res);
			}
			rowData.add(data);
		}
		Vector<String> columnName = new Vector<String>();
		for(int i=0; i<var.size(); i++)
		{
			columnName.add(var.get(i).toString());
		}
		table = new JTable(rowData, columnName);
		table.setPreferredSize(new Dimension(700, (int)table.getPreferredSize().getHeight()));
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		validate();
	}
}
