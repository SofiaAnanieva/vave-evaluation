package lancs.mobilemedia.core.ui.datamodel;


public class ImageData {
	private int recordId;
	private int foreignRecordId;
	private String parentAlbumName;
	private String imageLabel;
	private int numberOfViews = 0;
	private boolean favorite = false;
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
	public void toggleFavorite() {
		this.favorite = !favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void increaseNumberOfViews() {
		this.numberOfViews++;
	}
	public int getNumberOfViews() {
		return numberOfViews;
	}
	public void setNumberOfViews(int views) {
		this.numberOfViews = views;
	}
}



