package com.Anemoi.Resource;

import java.io.InputStream;

public class Media {
	private String mediaId;
	private InputStream mediaInputStream;
	private String mediaName;
	private long length;
	private String mediaType;
	private long uploadDate;
	private InputStream thumbnailInputStream;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public InputStream getMediaInputStream() {
		return mediaInputStream;
	}

	public void setMediaInputStream(InputStream mediaInputStream) {
		this.mediaInputStream = mediaInputStream;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public long getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(long uploadDate) {
		this.uploadDate = uploadDate;
	}

	public InputStream getThumbnailInputStream() {
		return thumbnailInputStream;
	}

	public void setThumbnailInputStream(InputStream thumbnailInputStream) {
		this.thumbnailInputStream = thumbnailInputStream;
	}

	@Override
	public String toString() {
		return "Media [mediaId=" + mediaId + ", mediaInputStream=" + mediaInputStream + ", mediaName=" + mediaName
				+ ", length=" + length + ", mediaType=" + mediaType + ", uploadDate=" + uploadDate
				+ ", thumbnailInputStream=" + thumbnailInputStream + "]";
	}

	public Media(String mediaId, InputStream mediaInputStream, String mediaName, long length, String mediaType,
			long uploadDate, InputStream thumbnailInputStream) {
		super();
		this.mediaId = mediaId;
		this.mediaInputStream = mediaInputStream;
		this.mediaName = mediaName;
		this.length = length;
		this.mediaType = mediaType;
		this.uploadDate = uploadDate;
		this.thumbnailInputStream = thumbnailInputStream;
	}

	public Media() {
		super();
		// TODO Auto-generated constructor stub
	}

}
