package Server;

import Server.Adapter.CompositeAdapter;
import Server.Adapter.TerminalAdapter;
import Server.Translator.Translator;

public interface IVisiteur 
{
	void visitServer(Server server);
	void visitBeforeCompositeAdapter(CompositeAdapter compositeAdapter);
	void visitCompositeAdapter(CompositeAdapter compositeAdapter);
	void visitAfterCompositeAdapter(CompositeAdapter compositeAdapter);
	void visitTerminalAdapter(TerminalAdapter terminalAdapter);
	void visitTranslator(Translator translator);
}
