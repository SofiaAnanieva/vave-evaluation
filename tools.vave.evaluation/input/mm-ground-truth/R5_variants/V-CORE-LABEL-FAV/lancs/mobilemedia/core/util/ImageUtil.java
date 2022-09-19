package lancs.mobilemedia.core.util;

import java.io.IOException;
import java.io.InputStream;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.InvalidImageFormatException;


public class ImageUtil {
	private static final String DELIMITER = "*";
	public ImageUtil() {
		super();
	}
	public byte[]readImageAsByteArray(String imageFile)throws ImagePathNotValidException,InvalidImageFormatException {
		byte bArray[] = new byte[1000];
		InputStream is = null;
		try {
			is = (InputStream) this.getClass().getResourceAsStream(imageFile);
		}catch (Exception e) {
			throw new ImagePathNotValidException("Path not valid for this image:" + imageFile);
		}
		int i,len = 0;
		byte bArray2[];
		byte b[] = new byte[1];
		try {
			while (is.read(b) != -1) {
				if (len + 1 >= bArray.) {
					bArray2 = new byte[bArray.];
					for (i = 0;i < len;i++)bArray2[i] = bArray[i];
					bArray = new byte[bArray2. + 500];
					for (i = 0;i < len;i++)bArray[i] = bArray2[i];
				}
				bArray[len] = b[0];
				len++;
			}
			is.close();
		}catch (IOException e1) {
			throw new InvalidImageFormatException("The file " + imageFile + " does not have PNG format");
		}catch (NullPointerException e2) {
			throw new ImagePathNotValidException("Path not valid for this image:" + imageFile);
		}
		return bArray;
	}
	public ImageData getImageInfoFromBytes(byte[]bytes)throws InvalidArrayFormatException {
		try {
			String iiString = new String(bytes);
			int startIndex = 0;
			int endIndex = iiString.indexOf(DELIMITER);
			String intString = iiString.substring(startIndex,endIndex);
			startIndex = endIndex + 1;
			endIndex = iiString.indexOf(DELIMITER,startIndex);
			String fidString = iiString.substring(startIndex,endIndex);
			startIndex = endIndex + 1;
			endIndex = iiString.indexOf(DELIMITER,startIndex);
			String albumLabel = iiString.substring(startIndex,endIndex);
			startIndex = endIndex + 1;
			endIndex = iiString.indexOf(DELIMITER,startIndex);
			if (endIndex == -1)endIndex = iiString.length();
			String imageLabel = "";
			imageLabel = iiString.substring(startIndex,endIndex);
			boolean favorite = false;
			startIndex = endIndex + 1;
			endIndex = iiString.indexOf(DELIMITER,startIndex);
			if (endIndex == -1)endIndex = iiString.length();
			favorite = (iiString.substring(startIndex,endIndex)).equalsIgnoreCase("true");
			Integer x = Integer.valueOf(fidString);
			ImageData ii = new ImageData(x.intValue(),albumLabel,imageLabel);
			ii.setFavorite(favorite);
			x = Integer.valueOf(intString);
			ii.setRecordId(x.intValue());
			return ii;
		}catch (Exception e) {
			throw new InvalidArrayFormatException();
		}
	}
	public byte[]getBytesFromImageInfo(ImageData ii)throws InvalidImageDataException {
		try {
			String byteString = new String();
			int i = ii.getRecordId();
			Integer j = new Integer(i);
			byteString = byteString.concat(j.toString());
			byteString = byteString.concat(DELIMITER);
			int i2 = ii.getForeignRecordId();
			Integer j2 = new Integer(i2);
			byteString = byteString.concat(j2.toString());
			byteString = byteString.concat(DELIMITER);
			byteString = byteString.concat(ii.getParentAlbumName());
			byteString = byteString.concat(DELIMITER);
			byteString = byteString.concat(ii.getImageLabel());
			byteString = byteString.concat(DELIMITER);
			if (ii.isFavorite())byteString = byteString.concat("true");else byteString = byteString.concat("false");
			return byteString.getBytes();
		}catch (Exception e) {
			throw new InvalidImageDataException("The provided data are not valid");
		}
	}
}



