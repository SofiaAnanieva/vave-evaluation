package org.argouml.uml.reveng;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.argouml.uml.reveng.ImportInterface;


public final class ImporterManager {
	private static final Logger LOG = Logger.getLogger(ImporterManager.class);
	private static final ImporterManager INSTANCE = new ImporterManager();
	public static ImporterManager getInstance() {
		return INSTANCE;
	}
	private Set<ImportInterface>importers = new HashSet<ImportInterface>();
	private ImporterManager() {
	}
	public void addImporter(ImportInterface importer) {
		importers.add(importer);
		LOG.debug("Added importer " + importer);
	}
	public boolean removeImporter(ImportInterface importer) {
		boolean status = importers.remove(importer);
		LOG.debug("Removed importer " + importer);
		return status;
	}
	public Set<ImportInterface>getImporters() {
		return Collections.unmodifiableSet(importers);
	}
	public boolean hasImporters() {
		return!importers.isEmpty();
	}
}



