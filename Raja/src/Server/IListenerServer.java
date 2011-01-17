package Server;

import Query.IQuery;

public interface IListenerServer 
{
	void initialization(Server server);
	void call(Server server, IQuery query);
	void finish(Server server, IQuery query);
}
