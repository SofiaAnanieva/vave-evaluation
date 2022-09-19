package lancs.mobilemedia.core.ui.datamodel;

import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import lancs.mobilemedia.core.util.ImageUtil;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.InvalidImageFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidPhotoAlbumNameException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


public class ImageAccessor {
	public static final String ALBUM_LABEL = "mpa-";
	public static final String INFO_LABEL = "mpi-";
	public static final String DEFAULT_ALBUM_NAME = "My Photo Album";
	public static final String IMAGE_LABEL = "ImageList";
	protected String[]albumNames;
	protected AlbumData model;
	private RecordStore imageRS = null;
	private RecordStore imageInfoRS = null;
	public ImageAccessor(AlbumData mod) {
		model = mod;
	}
	public void loadAlbums()throws InvalidImageDataException,PersistenceMechanismException {
		String[]currentStores = RecordStore.listRecordStores();
		if (currentStores != null) {
			System.out.println("ImageAccessor::loadAlbums: Found: " + currentStores. + " existing record stores");
			model.existingRecords = true;
			String[]temp = new String[currentStores.];
			int count = 0;
			for (int i = 0;i < currentStores.;i++) {
				String curr = currentStores[i];
				if (curr.startsWith(ALBUM_LABEL)) {
					curr = curr.substring(4);
					temp[i] = curr;
					count++;
				}
			}
			albumNames = new String[count];
			int count2 = 0;
			for (int i = 0;i < temp.;i++) {
				if (temp[i] != null) {
					albumNames[count2] = temp[i];
					count2++;
				}
			}
		}else {
			System.out.println("ImageAccessor::loadAlbums: 0 record stores exist. Creating default one.");
			resetImageRecordStore();
			loadAlbums();
		}
	}
	public void resetImageRecordStore()throws InvalidImageDataException,PersistenceMechanismException {
		String storeName = null;
		String infoStoreName = null;
		if (albumNames != null) {
			for (int i = 0;i < albumNames.;i++) {
				try {
					storeName = ALBUM_LABEL + albumNames[i];
					infoStoreName = INFO_LABEL + albumNames[i];
					System.out.println("<* ImageAccessor.resetImageRecordStore() *> delete " + storeName);
					RecordStore.deleteRecordStore(storeName);
					RecordStore.deleteRecordStore(infoStoreName);
				}catch (RecordStoreException e) {
					System.out.println("No record store named " + storeName + " to delete.");
					System.out.println("...or...No record store named " + infoStoreName + " to delete.");
					System.out.println("Ignoring Exception: " + e);
				}
			}
		}else {
			System.out.println("ImageAccessor::resetImageRecordStore: albumNames array was null. Nothing to delete.");
		}
		addImageData("Tucan Sam","/images/Tucan.png",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("Linux Penguin","/images/Penguin.png",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("Duke (Sun)","/images/Duke1.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("UBC Logo","/images/ubcLogo.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("Gail","/images/Gail1.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("J. Gosling","/images/Gosling1.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("Gregor","/images/Gregor1.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("Kris","/images/Kdvolder1.PNG",ImageAccessor.DEFAULT_ALBUM_NAME);
	}
	public void addImageData(String photoname,String path,String albumname)throws InvalidImageDataException,PersistenceMechanismException {
		try {
			imageRS = RecordStore.openRecordStore(ALBUM_LABEL + albumname,true);
			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + albumname,true);
			int rid;
			int rid2;
			ImageUtil converter = new ImageUtil();
			byte[]data1 = converter.readImageAsByteArray(path);
			rid = imageRS.addRecord(data1,0,data1.);
			ImageData ii = new ImageData(rid,ImageAccessor.ALBUM_LABEL + albumname,photoname);
			rid2 = imageInfoRS.getNextRecordID();
			ii.setRecordId(rid2);
			data1 = converter.getBytesFromImageInfo(ii);
			imageInfoRS.addRecord(data1,0,data1.);
			imageRS.closeRecordStore();
			imageInfoRS.closeRecordStore();
		}catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}
	public void addImageData(String photoname,ImageData imageData,String albumname)throws InvalidImageDataException,PersistenceMechanismException {
		try {
			imageRS = RecordStore.openRecordStore(ALBUM_LABEL + albumname,true);
			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + albumname,true);
			int rid2;
			ImageUtil converter = new ImageUtil();
			rid2 = imageInfoRS.getNextRecordID();
			imageData.setRecordId(rid2);
			byte[]data1 = converter.getBytesFromImageInfo(imageData);
			imageInfoRS.addRecord(data1,0,data1.);
		}catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}finally {
			try {
				imageRS.closeRecordStore();
				imageInfoRS.closeRecordStore();
			}catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			}catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}
	public ImageData[]loadImageDataFromRMS(String recordName)throws PersistenceMechanismException,InvalidImageDataException {
		Vector imagesVector = new Vector();
		try {
			String infoStoreName = ImageAccessor.INFO_LABEL + recordName;
			RecordStore infoStore = RecordStore.openRecordStore(infoStoreName,false);
			RecordEnumeration isEnum = infoStore.enumerateRecords(null,null,false);
			while (isEnum.hasNextElement()) {
				int currentId = isEnum.nextRecordId();
				byte[]data = infoStore.getRecord(currentId);
				ImageUtil converter = new ImageUtil();
				ImageData iiObject = converter.getImageInfoFromBytes(data);
				String label = iiObject.getImageLabel();
				imagesVector.addElement(iiObject);
				model.getImageInfoTable().put(label,iiObject);
			}
			infoStore.closeRecordStore();
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
		ImageData[]labelArray = new ImageData[imagesVector.size()];
		imagesVector.copyInto(labelArray);
		return labelArray;
	}
	public boolean updateImageInfo(ImageData oldData,ImageData newData)throws InvalidImageDataException,PersistenceMechanismException {
		boolean success = false;
		RecordStore infoStore = null;
		try {
			String infoStoreName = oldData.getParentAlbumName();
			infoStoreName = ImageAccessor.INFO_LABEL + infoStoreName.substring(ImageAccessor.ALBUM_LABEL.length());
			infoStore = RecordStore.openRecordStore(infoStoreName,false);
			ImageUtil converter = new ImageUtil();
			byte[]imageDataBytes = converter.getBytesFromImageInfo(newData);
			infoStore.setRecord(oldData.getRecordId(),imageDataBytes,0,imageDataBytes.);
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
		setImageInfo(oldData.getImageLabel(),newData);
		try {
			infoStore.closeRecordStore();
		}catch (RecordStoreNotOpenException e) {
		}catch (RecordStoreException e) {
			throw new PersistenceMechanismException(e);
		}
		return success;
	}
	public ImageData getImageInfo(String imageName)throws ImageNotFoundException,NullAlbumDataReference {
		if (model == null)throw new NullAlbumDataReference("Null reference to the Album data");
		ImageData ii = (ImageData) model.getImageInfoTable().get(imageName);
		if (ii == null)throw new ImageNotFoundException(imageName + " was NULL in ImageAccessor Hashtable.");
		return ii;
	}
	public void setImageInfo(String imageName,ImageData newData) {
		model.getImageInfoTable().put(newData.getImageLabel(),newData);
	}
	public Image loadSingleImageFromRMS(String recordName,String imageName,int recordId)throws PersistenceMechanismException {
		Image img = null;
		byte[]imageData = loadImageBytesFromRMS(recordName,imageName,recordId);
		img = Image.createImage(imageData,0,imageData.);
		return img;
	}
	public byte[]loadImageBytesFromRMS(String recordName,String imageName,int recordId)throws PersistenceMechanismException {
		byte[]imageData = null;
		try {
			RecordStore albumStore = RecordStore.openRecordStore(recordName,false);
			imageData = albumStore.getRecord(recordId);
			albumStore.closeRecordStore();
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
		return imageData;
	}
	public boolean deleteSingleImageFromRMS(String storeName,String imageName)throws PersistenceMechanismException,ImageNotFoundException,NullAlbumDataReference {
		boolean success = false;
		try {
			imageRS = RecordStore.openRecordStore(ALBUM_LABEL + storeName,true);
			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + storeName,true);
			ImageData imageData = getImageInfo(imageName);
			int rid = imageData.getForeignRecordId();
			imageRS.deleteRecord(rid);
			imageInfoRS.deleteRecord(rid);
			imageRS.closeRecordStore();
			imageInfoRS.closeRecordStore();
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
		return success;
	}
	public void createNewPhotoAlbum(String albumName)throws PersistenceMechanismException,InvalidPhotoAlbumNameException {
		RecordStore newAlbumRS = null;
		RecordStore newAlbumInfoRS = null;
		if (albumName.equals("")) {
			throw new InvalidPhotoAlbumNameException();
		}
		String[]names = getAlbumNames();
		for (int i = 0;i < names.;i++) {
			if (names[i].equals(albumName))throw new InvalidPhotoAlbumNameException();
		}
		try {
			newAlbumRS = RecordStore.openRecordStore(ALBUM_LABEL + albumName,true);
			newAlbumInfoRS = RecordStore.openRecordStore(INFO_LABEL + albumName,true);
			newAlbumRS.closeRecordStore();
			newAlbumInfoRS.closeRecordStore();
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
	}
	public void deletePhotoAlbum(String albumName)throws PersistenceMechanismException {
		try {
			RecordStore.deleteRecordStore(ALBUM_LABEL + albumName);
			RecordStore.deleteRecordStore(INFO_LABEL + albumName);
		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}
	}
	public String[]getAlbumNames() {
		return albumNames;
	}
}



