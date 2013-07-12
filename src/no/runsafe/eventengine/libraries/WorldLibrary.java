package no.runsafe.eventengine.libraries;

import no.runsafe.framework.lua.FunctionParameters;
import no.runsafe.framework.lua.RunsafeLuaFunction;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.block.RunsafeBlock;
import no.runsafe.framework.minecraft.chunk.RunsafeChunk;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.ArrayList;
import java.util.List;

public class WorldLibrary extends OneArgFunction
{
	@Override
	public LuaValue call(LuaValue env)
	{
		LuaTable lib = new LuaTable();
		lib.set("setBlock", new SetBlock());
		lib.set("getBlock", new GetBlock());

		env.get("api").set("world", lib);
		return lib;
	}

	public static void prepareLocationForEdit(RunsafeLocation location)
	{
		RunsafeChunk chunk = location.getChunk();
		if (chunk.isUnloaded()) chunk.load();
	}

	static class SetBlock extends RunsafeLuaFunction
	{
		@Override
		public List<Object> run(FunctionParameters parameters)
		{
			RunsafeLocation location = parameters.getLocation(0);
			WorldLibrary.prepareLocationForEdit(location);

			RunsafeBlock block = location.getBlock();
			block.setTypeId(parameters.getInt(4));

			if (parameters.hasParameter(5))
				block.setData((byte) (int) parameters.getInt(5));

			return null;
		}
	}

	static class GetBlock extends RunsafeLuaFunction
	{
		@Override
		public List<Object> run(FunctionParameters parameters)
		{
			List<Object> returns = new ArrayList<Object>();
			RunsafeLocation location = parameters.getLocation(0);
			WorldLibrary.prepareLocationForEdit(location);

			RunsafeBlock block = location.getBlock();
			returns.add(block.getTypeId());
			returns.add(block.getData());

			return returns;
		}
	}
}
