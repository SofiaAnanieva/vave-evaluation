package org.argouml.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collection;


public class TransferableModelElements implements Transferable {
	public static final DataFlavor UML_COLLECTION_FLAVOR = new DataFlavor(Collection.class,"UML ModelElements Collection");
	private static DataFlavor[]flavors =  {UML_COLLECTION_FLAVOR};
	private Collection theModelElements;
	public TransferableModelElements(Collection data) {
		theModelElements = data;
	}
	public Object getTransferData(DataFlavor dataFlavor)throws UnsupportedFlavorException,IOException {
		if (dataFlavor.match(UML_COLLECTION_FLAVOR)) {
			return theModelElements;
		}
		throw new UnsupportedFlavorException(dataFlavor);
	}
	public DataFlavor[]getTransferDataFlavors() {
		return flavors;
	}
	public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
		return dataFlavor.match(UML_COLLECTION_FLAVOR);
	}
}



