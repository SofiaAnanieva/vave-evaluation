package lancs.mobilemedia.core.ui.datamodel;

import java.util.Hashtable;
import javax.microedition.lcdui.Image;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.InvalidPhotoAlbumNameException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;


public class AlbumData {
	private ImageAccessor imageAccessor;
	protected Hashtable imageInfoTable = new Hashtable();
	public boolean existingRecords = false;
	public AlbumData() {
		imageAccessor = new ImageAccessor(this);
	}
	public String[]getAlbumNames() {
		try {
			imageAccessor.loadAlbums();
		}catch (InvalidImageDataException e) {
			e.printStackTrace();
		}catch (PersistenceMechanismException e) {
			e.printStackTrace();
		}
		return imageAccessor.getAlbumNames();
	}
	public ImageData[]getImages(String recordName)throws UnavailablePhotoAlbumException {
		ImageData[]result;
		try {
			result = imageAccessor.loadImageDataFromRMS(recordName);
		}catch (PersistenceMechanismException e) {
			throw new UnavailablePhotoAlbumException(e);
		}catch (InvalidImageDataException e) {
			throw new UnavailablePhotoAlbumException(e);
		}
		return result;
	}
	public void createNewPhotoAlbum(String albumName)throws PersistenceMechanismException,InvalidPhotoAlbumNameException {
		imageAccessor.createNewPhotoAlbum(albumName);
	}
	public void deletePhotoAlbum(String albumName)throws PersistenceMechanismException {
		imageAccessor.deletePhotoAlbum(albumName);
	}
	public Image getImageFromRecordStore(String recordStore,String imageName)throws ImageNotFoundException,PersistenceMechanismException {
		ImageData imageInfo = null;
		try {
			imageInfo = imageAccessor.getImageInfo(imageName);
		}catch (NullAlbumDataReference e) {
			imageAccessor = new ImageAccessor(this);
		}
		int imageId = imageInfo.getForeignRecordId();
		String album = imageInfo.getParentAlbumName();
		Image imageRec = imageAccessor.loadSingleImageFromRMS(album,imageName,imageId);
		return imageRec;
	}
	public void addNewPhotoToAlbum(String label,String path,String album)throws InvalidImageDataException,PersistenceMechanismException {
		imageAccessor.addImageData(label,path,album);
	}
	public void deleteImage(String imageName,String storeName)throws PersistenceMechanismException,ImageNotFoundException {
		try {
			imageAccessor.deleteSingleImageFromRMS(imageName,storeName);
		}catch (NullAlbumDataReference e) {
			imageAccessor = new ImageAccessor(this);
			e.printStackTrace();
		}
	}
	public void resetImageData()throws PersistenceMechanismException {
		try {
			imageAccessor.resetImageRecordStore();
		}catch (InvalidImageDataException e) {
			e.printStackTrace();
		}
	}
	public Hashtable getImageInfoTable() {
		return imageInfoTable;
	}
	public void setImageInfoTable(Hashtable imageInfoTable) {
		this.imageInfoTable = imageInfoTable;
	}
	public void setImageAccessor(ImageAccessor imageAccessor) {
		this.imageAccessor = imageAccessor;
	}
	public ImageData getImageInfo(String name)throws ImageNotFoundException,NullAlbumDataReference {
		return imageAccessor.getImageInfo(name);
	}
	public void updateImageInfo(ImageData oldData,ImageData newData)throws InvalidImageDataException,PersistenceMechanismException {
		imageAccessor.updateImageInfo(oldData,newData);
	}
}



