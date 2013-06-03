package no.runsafe.eventengine.functions;

import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class player extends OneArgFunction
{
	public player() {}

	@Override
	public LuaValue call(LuaValue env)
	{
		LuaTable player = new LuaTable();
		player.set("kill", new kill());
		env.set("player", player);
		env.get("package").get("loaded").set("player", player);
		return player;
	}

	static class kill extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue playerName)
		{
			RunsafePlayer player = RunsafeServer.Instance.getPlayerExact(playerName.toString());
			player.setHealth(0);
			return null;
		}
	}
}
