package org.argouml.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.xml.sax.InputSource;


public interface XmiReader {
	Collection parse(InputSource pIs,boolean readOnly)throws UmlException;
	Map<String,Object>getXMIUUIDToObjectMap();
	public boolean setIgnoredElements(String[]elementNames);
	public String[]getIgnoredElements();
	public int getIgnoredElementCount();
	public String getTagName();
	public List<String>getSearchPath();
	public void addSearchPath(String path);
	public void removeSearchPath(String path);
	public String getHeader();
}



