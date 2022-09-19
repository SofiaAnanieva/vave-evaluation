package org.argouml.profile;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.Profile;


public class UserDefinedProfile extends Profile {
	private static final Logger LOG = Logger.getLogger(UserDefinedProfile.class);
	private String displayName;
	private File modelFile;
	private Collection profilePackages;
	private UserDefinedFigNodeStrategy figNodeStrategy = new UserDefinedFigNodeStrategy();
	private class UserDefinedFigNodeStrategy implements FigNodeStrategy {
	private Map<String,Image>images = new HashMap<String,Image>();
	public Image getIconForStereotype(Object stereotype) {
		return images.get(Model.getFacade().getName(stereotype));
	}
	public void addDesrciptor(FigNodeDescriptor fnd) {
		images.put(fnd.stereotype,fnd.img);
	}
}
	private class FigNodeDescriptor {
	private String stereotype;
	private Image img;
	private String src;
	private int length;
	public boolean isValid() {
		return stereotype != null&&src != null&&length > 0;
	}
}
	public UserDefinedProfile(File file)throws ProfileException {
		LOG.info("load " + file);
		displayName = file.getName();
		modelFile = file;
		ProfileReference reference = null;
		try {
			reference = new UserProfileReference(file.getPath());
		}catch (MalformedURLException e) {
			throw new ProfileException("Failed to create the ProfileReference.",e);
		}
		profilePackages = new FileModelLoader().loadModel(reference);
		finishLoading();
	}
	public UserDefinedProfile(URL url)throws ProfileException {
		LOG.info("load " + url);
		ProfileReference reference = null;
		reference = new UserProfileReference(url.getPath(),url);
		profilePackages = new URLModelLoader().loadModel(reference);
		finishLoading();
	}
	public UserDefinedProfile(String dn,URL url,Set<String>dependencies)throws ProfileException {
		LOG.info("load " + url);
		this.displayName = dn;
		if (url != null) {
			ProfileReference reference = null;
			reference = new UserProfileReference(url.getPath(),url);
			profilePackages = new URLModelLoader().loadModel(reference);
		}else {
			profilePackages = new ArrayList(0);
		}
		for (String profileID:dependencies) {
			addProfileDependency(profileID);
		}
		finishLoading();
	}
	private void finishLoading() {
		Collection packagesInProfile = filterPackages();
		for (Object obj:packagesInProfile) {
			if (Model.getFacade().isAModelElement(obj)&&(Model.getExtensionMechanismsHelper().hasStereotype(obj,"profile")||(packagesInProfile.size() == 1))) {
				String name = Model.getFacade().getName(obj);
				if (name != null) {
					displayName = name;
				}
				LOG.info("profile " + displayName);
				String dependencyListStr = Model.getFacade().getTaggedValueValue(obj,"Dependency");
				StringTokenizer st = new StringTokenizer(dependencyListStr," ,;:");
				String profile = null;
				while (st.hasMoreTokens()) {
					profile = st.nextToken();
					if (profile != null) {
						LOG.debug("AddingDependency " + profile);
						this.addProfileDependency(ProfileFacade.getManager().lookForRegisteredProfile(profile));
					}
				}
			}
		}
		Collection allStereotypes = Model.getExtensionMechanismsHelper().getStereotypes(packagesInProfile);
		for (Object stereotype:allStereotypes) {
			Collection tags = Model.getFacade().getTaggedValuesCollection(stereotype);
			for (Object tag:tags) {
				String tagName = Model.getFacade().getTag(tag);
				if (tagName == null) {
					LOG.debug("profile package with stereotype " + Model.getFacade().getName(stereotype) + " contains a null tag definition");
				}else if (tagName.toLowerCase().equals("figure")) {
					LOG.debug("AddFigNode " + Model.getFacade().getName(stereotype));
					String value = Model.getFacade().getValueOfTag(tag);
					File f = new File(value);
					FigNodeDescriptor fnd = null;
					try {
						fnd = loadImage(Model.getFacade().getName(stereotype).toString(),f);
						figNodeStrategy.addDesrciptor(fnd);
					}catch (IOException e) {
						LOG.error("Error loading FigNode",e);
					}
				}
			}
		}
	}
	private Collection filterPackages() {
		Collection ret = new ArrayList();
		for (Object object:profilePackages) {
			if (Model.getFacade().isAPackage(object)) {
				ret.add(object);
			}
		}
		return ret;
	}
	@SuppressWarnings("unchecked")private Collection<Object>getAllCommentsInModel(Collection objs) {
		Collection<Object>col = new ArrayList<Object>();
		for (Object obj:objs) {
			if (Model.getFacade().isAComment(obj)) {
				col.add(obj);
			}else if (Model.getFacade().isANamespace(obj)) {
				Collection contents = Model.getModelManagementHelper().getAllContents(obj);
				if (contents != null) {
					col.addAll(contents);
				}
			}
		}
		return col;
	}
	public String getDisplayName() {
		return displayName;
	}
	@Override public FormatingStrategy getFormatingStrategy() {
		return null;
	}
	@Override public FigNodeStrategy getFigureStrategy() {
		return figNodeStrategy;
	}
	public File getModelFile() {
		return modelFile;
	}
	@Override public String toString() {
		File str = getModelFile();
		return super.toString() + (str != null?" [" + str + "]":"");
	}
	@Override public Collection getProfilePackages() {
		return profilePackages;
	}
	private FigNodeDescriptor loadImage(String stereotype,File f)throws IOException {
		FigNodeDescriptor descriptor = new FigNodeDescriptor();
		descriptor.length = (int) f.length();
		descriptor.src = f.getPath();
		descriptor.stereotype = stereotype;
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
		byte[]buf = new byte[descriptor.length];
		try {
			bis.read(buf);
		}catch (IOException e) {
			e.printStackTrace();
		}
		descriptor.img = new ImageIcon(buf).getImage();
		return descriptor;
	}
}



