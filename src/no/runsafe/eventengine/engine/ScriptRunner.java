package no.runsafe.eventengine.engine;

import no.runsafe.eventengine.Plugin;
import no.runsafe.framework.api.IOutput;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

import java.io.File;

public class ScriptRunner
{
	public ScriptRunner(Plugin eventEngine, IOutput output)
	{
		// Setup folders
		this.path = String.format("plugins/%s/scripts/", eventEngine.getName());
		if (!new File(this.path).mkdirs())
			output.warning("Failed to create scripts directory at: " + this.path);

		this.output = output;
		this.runScripts();
	}

	public void runScripts()
	{
		int succeeded = 0;
		int failed = 0;

		File folder = new File(this.path);
		File[] files = folder.listFiles();

		if (files != null)
		{
			for (File file : files)
			{
				String fileName = file.getName();
				if (file.isFile() && fileName.endsWith(".lua"))
				{
					String output = this.runScript(fileName);
					if (output != null)
					{
						this.output.write(output);
						failed += 1;
					}
					else
					{
						succeeded += 1;
					}
				}
			}
		}

		if (succeeded > 0)
			this.output.logInformation("%d lua scripts loaded.", succeeded);

		if (failed > 0)
			this.output.logError("%d lua scripts failed to load.", failed);
	}

	public String runScript(String script)
	{
		String file = path + script;

		if (!new File(file).exists())
			return "&cUnable to find script: " + file;

		try
		{
			Environment.global.get("dofile").call(LuaValue.valueOf(file));
		}
		catch (LuaError error)
		{
			return "&cLua Error: " + error.getMessage();
		}
		return null;
	}

	private final String path;
	private IOutput output;
}
