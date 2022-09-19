package lancs.mobilemedia.core.ui.datamodel;


public class ImageData {
	private int recordId;
	private int foreignRecordId;
	private String parentAlbumName;
	private String imageLabel;
	public ImageData(int foreignRecordId,String parentAlbumName,String imageLabel) {
		super();
		this.foreignRecordId = foreignRecordId;
		this.parentAlbumName = parentAlbumName;
		this.imageLabel = imageLabel;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getForeignRecordId() {
		return foreignRecordId;
	}
	public void setForeignRecordId(int foreignRecordId) {
		this.foreignRecordId = foreignRecordId;
	}
	public String getImageLabel() {
		return imageLabel;
	}
	public void setImageLabel(String imageLabel) {
		this.imageLabel = imageLabel;
	}
	public String getParentAlbumName() {
		return parentAlbumName;
	}
	public void setParentAlbumName(String parentAlbumName) {
		this.parentAlbumName = parentAlbumName;
	}
}



