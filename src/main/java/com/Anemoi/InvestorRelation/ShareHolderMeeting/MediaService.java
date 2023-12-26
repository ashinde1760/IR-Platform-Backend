package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Requires(beans = LocalRepoConfiguration.class)
public class MediaService implements MediaServiceInterface {

	File baseDir;
	
	File fileDir;
	
	@Inject
	LocalRepoConfiguration localRepoConfiguration;
	
	


	public MediaService(LocalRepoConfiguration localRepoConfiguration) {

		System.out.print("\nBase Directory ::" + localRepoConfiguration.getBaseDir());

		baseDir = new File(localRepoConfiguration.getBaseDir());
		fileDir = new File(localRepoConfiguration.getfileDir());

		if (!baseDir.exists() ||  !fileDir.exists()) {
			fileDir.mkdir();
			baseDir.mkdirs();
		}
		System.out.print("\n Local File repo is initialized ");
	}

	

	private void createNewFile(InputStream inputStream, String key, String filename) throws IOException {
		String ext = getFileExtension(filename);
		File targetFile = new File(this.baseDir, key + ext);
		System.out.println("    getting data"+ this.baseDir);	
		
		System.out.print("\n New file is created :: " + targetFile.getAbsolutePath()); //this is actual path
		OutputStream outStream = new FileOutputStream(targetFile);
		byte[] buffer = new byte[8 * 1024];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();
	}

	private String getFileExtension(String filename) {
		int lastIndexOf = filename.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return filename.substring(lastIndexOf);
	}



	@Override
	public void upload(String key, CompletedFileUpload file) {
	
		System.out.print("\n Upload the file to the Local directory ");
		try {
			createNewFile(file.getInputStream(), key, file.getFilename());
			System.out.print("\nFile is created :: " + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  public File findFile(String mediakey)
  {
	  File[] matchingFiles=baseDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				String onlyfile=name!=null && name.lastIndexOf(".") > 0 ? name.substring(0, name.lastIndexOf(".")):name;
				return onlyfile.equalsIgnoreCase(mediakey);
			}
		});
	if(matchingFiles.length==0) 
		return null;
	return matchingFiles[0];

  }

	@Override
	public File getMediaFileByKey(String mediakey) {
		System.out.println("caa");
		File f=this.findFile(mediakey);
		System.out.println("f"+f);
		return f;
	}



	@Override
	public String deleteMediaFile(String mediakey) {
		File file=this.findFile(mediakey);
		if(file !=null)
		{
			file.delete();
			return mediakey;
		}
		return "file not found";
		
	}



	@Override
	public String uploadlogoFile(String logoKey, CompletedFileUpload logofile) {
		// TODO Auto-generated method stub
		System.out.print("\n Upload the file to the Local directory ");
		try {
		String filePath=	createlogofile(logofile.getInputStream(), logoKey, logofile.getFilename());
			System.out.print("\nFile is created :: " + logoKey);
			System.out.println("filepath"+filePath);
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	private String createlogofile(InputStream inputStream, String logoKey, String filename) throws IOException {
		// TODO Auto-generated method stub
//		String ext = getFileExtension(filename);
		File targetFile = new File(this.fileDir, logoKey );
		System.out.println("    getting data"+ this.fileDir);	
		
		System.out.print("\n New file is created :: " + targetFile.getAbsolutePath()); //this is actual path
		OutputStream outStream = new FileOutputStream(targetFile);
		byte[] buffer = new byte[8 * 1024];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
  
		inputStream.close();
		outStream.close();
		return targetFile.getAbsolutePath();
	}

	
}
	

	

