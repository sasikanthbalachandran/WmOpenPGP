package COpenPGP.services;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// --- <<IS-END-IMPORTS>> ---

public final class utils

{
	// ---( internal utility methods )---

	final static utils _instance = new utils();

	static utils _newInstance() { return new utils(); }

	static utils _cast(Object o) { return (utils)o; }

	// ---( server methods )---




	public static final void moveFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(moveFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required sourcePath
		// [i] field:0:required targetDir
		// [i] field:0:required useTimeStamp {"false","true"}
		// [o] field:0:required status
		// [o] field:0:required targetFilePath
		IDataCursor cursor = pipeline.getCursor();
		
		String sourcePath = null;
		String targetDir = null;
		
		if (cursor.first("sourcePath"))
		{
		  sourcePath = (String) cursor.getValue();
		}
		else
		{
		  throw new ServiceException("Input parameter \'sourcePath\' was not found.");
		}
		if (cursor.first("targetDir"))
		{
		  targetDir = (String) cursor.getValue();
		}
		else
		{
		  throw new ServiceException("Input parameter \'targetDir\' was not found.");
		}
		boolean useTimeStamp = cursor.first("useTimeStamp") ? Boolean.valueOf((String) cursor.getValue()).booleanValue() : false;
		cursor.destroy();
		
		boolean status = false;
		File original = new File(sourcePath);
		
		// String dtStamp = new Date().toString().replace(' ', '-').replace(':',
		// '-');
		String dtStamp = null;
		try
		{
		  IData d = IDataFactory.create();
		  IDataCursor c = d.getCursor();
		  c.first();
		  c.insertBefore("pattern", "yyyyMMddHHmmss");
		  IData d2 = Service.doInvoke("pub.date", "currentDate", d);
		  c = d2.getCursor();
		  if (c.first("value"))
		  {
		    dtStamp = (String) c.getValue();
		  }
		  else
		  {
		    throw new ServiceException("Missing returned value for Service currentDate.");
		  }
		}
		catch (Exception e)
		{
		  throw new ServiceException(e.getMessage());
		}
		String basename = original.getName();
		if (useTimeStamp)
		{
		  basename += "-" + dtStamp;
		}
		File targetFile = new File(targetDir, basename);
		status = original.renameTo(targetFile);
		String targetFilePath = null;
		try
		{
		  targetFilePath = targetFile.getCanonicalPath();
		}
		catch (IOException e)
		{
		  throw new ServiceException("Move file and obtain target path: " + e.getMessage());
		}
		cursor = pipeline.getCursor();
		cursor.last();
		cursor.insertAfter("status", "" + status);
		cursor.insertAfter("targetFilePath", targetFilePath);
		cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void writeToFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(writeToFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required userData
		// [i] field:0:required filename
		// [i] field:0:required appendOverwriteFlag
		IDataCursor idcPipeline = pipeline.getCursor();
		String strUserData = null;
		String strFullFilename = null;
		if (idcPipeline.first("userData"))
		{
		  strUserData = (String) idcPipeline.getValue();
		}
		if (idcPipeline.first("filename"))
		{
		  strFullFilename = (String) idcPipeline.getValue();
		}
		else
		{
		  throw new ServiceException("filename is null!");
		}
		idcPipeline.first("appendOverwriteFlag");
		String appendOverwriteFlag = (String) idcPipeline.getValue();
		
		// Separate filename into path and filename
		// This is done so that the directory can be written (if necessary)
		String separator = System.getProperty("file.separator");
		int indexSeparator = strFullFilename.lastIndexOf(separator);
		if (indexSeparator == -1)
		{
		  // Account for fact that you can use either '\' or '/' in Windows
		  indexSeparator = strFullFilename.lastIndexOf('/');
		}
		String strPathName = strFullFilename.substring(0, indexSeparator + 1);
		String strFileName = strFullFilename.substring(indexSeparator + 1);
		
		FileWriter fw = null;
		try
		{
		  File pathToBeWritten = new File(strPathName);
		  // System.out.println("canonical path = " +
		  // pathToBeWritten.getCanonicalPath());
		
		  // Write the directory...
		  if (pathToBeWritten.exists() == false)
		  {
		    throw new ServiceException("Path does not exist!");
		  }
		
		  // Check if file exists
		  File fileToBeWritten = new File(strFullFilename);
		  if (fileToBeWritten.exists() == true && appendOverwriteFlag != null && appendOverwriteFlag.equals("failIfFileExists"))
		  {
		    throw new ServiceException("File " + strFullFilename + " already exists!");
		  }
		
		  // Write the file...
		  if (appendOverwriteFlag != null && appendOverwriteFlag.equals("overwrite"))
		  {
		    // overwrite
		    fw = new FileWriter(strFullFilename, false);
		  }
		  else
		  {
		    // append
		    fw = new FileWriter(strFullFilename, true);
		  }
		  fw.write(strUserData);
		}
		catch (Exception e)
		{
		  throw new ServiceException(e.getMessage());
		}
		finally
		{
		  // Close the output stream....
		  try
		  {
		    fw.close();
		  }
		  catch (Exception e)
		  {
		  }
		
		  idcPipeline.destroy();
		}
			
		// --- <<IS-END>> ---

                
	}
}

