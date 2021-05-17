package data.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import data.Data;
import data.Session;
import data.StockExchange;


public class FileHelper {


	private static final String DOWNLOAD_FOLDER_NAME = "Downloads/";
	private static final int BUFFER_SIZE = 4096;

	private static FileHelper signletonInstanceCrawler;
	
	private void log(String str) {
		System.out.println(str);
	}

	private FileHelper() {

	}

	public static FileHelper getInstance() {
		if (signletonInstanceCrawler == null) {
			return new FileHelper();
		}

		return signletonInstanceCrawler;
	}

	private boolean checkNotNull(String string) {
		return string != "";
	}

	
	private String downloadFile (String urlString , String fileNameString ) {
		
		String downloadFilePath = "";
		
		try {
			downloadFilePath = DOWNLOAD_FOLDER_NAME + fileNameString;
			ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(urlString).openStream());
			
			FileOutputStream fileOutputStream = new FileOutputStream(downloadFilePath);
			FileChannel fileChannel = fileOutputStream.getChannel();
		
			fileChannel
			  .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return downloadFilePath;
	}
	
	
	private String getFileExtension(String fileString) {
		if (this.checkNotNull(fileString)) {
			String fileNameString = new File(fileString).getName();
			int dotIndex = fileNameString.lastIndexOf(".");
			return (dotIndex == -1) ? "" : fileNameString.substring(dotIndex + 1);
		}
		return "";
	}

	
	
	private File newZipFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	
	
	private ArrayList<String> unzipFile(String sourceFileString, String dest) {

		ArrayList<String> unzippedFiles = new  ArrayList<String>();
		
		
		String fileExtension = this.getFileExtension(sourceFileString);
		
		if (fileExtension.equals("zip") && fileExtension.equals("csv")) {
			return unzippedFiles;
		}

		
		if( fileExtension.equals("csv")) {
			return unzippedFiles;
		}
		
		String fileZip = sourceFileString;
		File destDir = new File(dest);
		byte[] buffer = new byte[1024];
		ZipInputStream zis;
		try {
		
			zis = new ZipInputStream(new FileInputStream(fileZip));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newZipFile(destDir, zipEntry);
				
				unzippedFiles.add(newFile.getCanonicalPath());
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return unzippedFiles;
	}

	
	private void removeFiles() {
		File downloadDirectory  = new File(DOWNLOAD_FOLDER_NAME);
		
		for(File file : downloadDirectory.listFiles()) {
			String fileExtension = this.getFileExtension(file.getName());
			if(fileExtension.equals("zip") || fileExtension.equals("csv")) {
				file.delete();
			}
		}
		
		return ;
	}

	
	private StockExchange getStockExchangeByFileName(File file) {
		String fileNameString = file.getName();
		if(fileNameString.indexOf("HNX") > -1) {
			return StockExchange.HNX;
		}
		if(fileNameString.indexOf("UPCOM") > -1) {
			return StockExchange.UPCOM;
		}

		return StockExchange.HSX;

	}
	
	
	private Data scanCsvFile(String filePath) {
		
		File scanFile = new File(filePath);
		
		StockExchange stockExchange = this.getStockExchangeByFileName(scanFile);
		
        List<Session> sessions = new ArrayList<>();

        Scanner sc = null;
        try {
            sc = new Scanner(scanFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert sc != null;
        sc.useDelimiter("[,\n]");
        
        // Using for skip the first line
        for(int i = 0 ; i< 7; i++) {
        	sc.next();
        }
        while (sc.hasNext())  //returns a boolean value
        {
            Session session = new Session();
            session.setTicker(sc.next());

            // e.g: "20201015" -> "2020-10-15"
            String date = sc.next();
            Date d = null;
            try {
                d = (Date) new SimpleDateFormat("yyyyMMdd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            session.setDate(d);

            session.setOpen(Float.parseFloat(sc.next()));
            session.setHigh(Float.parseFloat(sc.next()));
            session.setLow(Float.parseFloat(sc.next()));
            session.setClose(Float.parseFloat(sc.next()));
            session.setVolume(Integer.parseInt(sc.next().trim()));

            sessions.add(session);
        }
        sc.close();

        Session[] ret = new Session[sessions.size()];
        for(int i = 0; i < sessions.size(); i++){
            ret[i] = sessions.get(i);
        }

        return new Data(stockExchange, ret);
		
	}

	
	
	public ArrayList<Data> scanFile(String filePath) {
		
		ArrayList<Data> datas =  new ArrayList<Data>();
		File scanFile = new File(filePath); 
		String extensionString = this.getFileExtension(scanFile.getName());
		
		if(extensionString.equals("csv")) {
			Data newData = this.scanCsvFile(filePath);
			datas.add(newData);
		}
		
		if(extensionString.equals("zip")) {
			ArrayList<String> unzipFilePaths = this.unzipFile(filePath, DOWNLOAD_FOLDER_NAME);
			
			for(String unzipFilePath: unzipFilePaths) {
				Data newData = this.scanCsvFile(unzipFilePath);
				
				datas.add(newData);
			}
			
		}
		
		
		return datas;
	}
	
	
	private String getFileNameFromUrl(String url) {
		int lastIndex = url.lastIndexOf("/");
		return url.substring(lastIndex+1);
	}
	
	public ArrayList<Data> scanFileByUrl(String url){
		
		String fileNameString = this.getFileNameFromUrl(url);
		
		File file = new File(DOWNLOAD_FOLDER_NAME + fileNameString);
		String downloadPath ;
		if(file.exists()) {
			downloadPath = DOWNLOAD_FOLDER_NAME + fileNameString;
			System.out.println("FILE EXIST");
		}else {
			this.removeFiles();
			downloadPath = this.downloadFile(url, fileNameString);
		}
		 

		return this.scanFile(downloadPath);
	}
}
