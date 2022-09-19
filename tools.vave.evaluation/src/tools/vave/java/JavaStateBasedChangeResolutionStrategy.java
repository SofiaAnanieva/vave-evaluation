package tools.vave.java;

import static com.google.common.base.Preconditions.checkArgument;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import edu.kit.ipd.sdq.commons.util.org.eclipse.emf.ecore.resource.ResourceUtil;
import tools.vitruv.framework.change.description.VitruviusChange;

public class JavaStateBasedChangeResolutionStrategy {
	private void checkNoProxies(Resource resource, String stateNotice) {
		Iterable<EObject> proxies = ResourceUtil.getReferencedProxies(resource);
		checkArgument(!proxies.iterator().hasNext(), "%s '%s' should not contain proxies", stateNotice, resource.getURI());
	}

	public VitruviusChange getChangeSequence(ResourceSet oldResourceSet, ResourceSet newResourceSet) {
		checkArgument(oldResourceSet != null && newResourceSet != null, "old state or new state must not be null!");
		newResourceSet.getResources().forEach(newResource -> this.checkNoProxies(newResource, "new state"));
		oldResourceSet.getResources().forEach(oldResource -> this.checkNoProxies(oldResource, "old state"));

		new EcoreUtil.Copier();

		return null;
	}

}
