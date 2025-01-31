package wm;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.util.Properties;
// --- <<IS-END-IMPORTS>> ---

public final class openpgp

{
	// ---( internal utility methods )---

	final static openpgp _instance = new openpgp();

	static openpgp _newInstance() { return new openpgp(); }

	static openpgp _cast(Object o) { return (openpgp)o; }

	// ---( server methods )---




	public static final void execPGPs (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(execPGPs)>> ---
		// @sigtype java 3.5
		// [i] field:0:required cmdline
		// [i] object:0:required inputStream
		// [o] field:0:required status
		// [o] object:0:required outputStream
		// [o] field:0:required error
		 
		IDataCursor idc = pipeline.getCursor();
		
		String cmdlineparms = IDataUtil.getString(idc, "cmdline");
		
		int timeout = IDataUtil.getInt(idc, "timeout", 30);
		
		// Get the OpenPGP command to run
		cmdlineparms = openpgpprops.getProperty("pgp.command", "/usr/bin/gpg") + " " + cmdlineparms;
		
		BufferedInputStream execInput;
		if (idc.first("inputStream")) {
			execInput =
				new BufferedInputStream(
					(InputStream) IDataUtil.get(idc, "inputStream"));
		} else {
			execInput = null;
		}
		
		CommandRunner cr = new CommandRunner();
		
		// TODO: make these real streams from the forked process
		ByteArrayOutputStream execError = new ByteArrayOutputStream();
		ByteArrayOutputStream execOutput = new ByteArrayOutputStream();
		try {
		
			//System.out.println("Running " + cmdlineparms);
		
			cr.setCommand(cmdlineparms);
			cr.setInputStream(execInput);
			cr.setStdErrorStream(execError);
			cr.setStdOutputStream(execOutput);
			cr.setTimeout(timeout);
			cr.evaluate();
		} catch (IOException ioe) {
			throw new ServiceException(
				"I/O Exception running command: " + ioe.toString());
		}
		
		int status = cr.getExitValue();
		
		ByteArrayInputStream bais =
			new ByteArrayInputStream(execOutput.toByteArray());
		
		idc.insertAfter("outputStream", bais);
		idc.insertAfter("status", Integer.toString(status));
		idc.insertAfter("error", execError.toString());
		
		idc.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void getProperty (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getProperty)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required name
		// [o] field:0:required value
		
		IDataCursor idc = pipeline.getCursor();
		String name = IDataUtil.getString(idc, "name");
		String value = openpgpprops.getProperty(name);
		
		if (value == null) {
			throw new ServiceException("openpgp property " + name + " not found.");
		}
		
		IDataUtil.put(idc, "value", value);
		IDataUtil.remove(idc, "name");
		idc.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void loadProperties (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(loadProperties)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		try {
			File f = com.wm.app.b2b.server.ServerAPI.getPackageConfigDir("COpenPGP");
			openpgpprops.load(new FileInputStream(new File(f, "openpgp.properties")));
		} catch (Exception e) {
			throw new ServiceException("Error loading openpgp.properties: " + e);
		}
		
		String openpgphome = openpgpprops.getProperty("pgp.home");
		if (openpgphome == null) {
			throw new ServiceException("pgp.home not set.");
		}
			
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static Properties openpgpprops = new Properties();
		
		
	// --- <<IS-END-SHARED>> ---
}

