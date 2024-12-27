package com.fishtankapps.bookitbaby.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	private FileUtils() {}

	
	public static void copyFile(File toCopy, String newLocation) throws IOException {
		File copied = new File(newLocation);
		copied.createNewFile();

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(toCopy));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copied));

		byte[] buffer = new byte[1024];
		int lengthRead;
		while ((lengthRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, lengthRead);
			out.flush();
		}

		in.close();
		out.close();

		if (!copied.exists())
			throw new IOException("New file did not exist");

	}

	public static void deleteFolder(File folder) {
		if (folder.isDirectory())
			for (File children : folder.listFiles())
				deleteFolder(children);
		
		folder.delete();
	}

	public static File bookitBabyTempDir = null;
	
	public static File createTempDir(String name) {
		try {
			if(bookitBabyTempDir == null) {			
				File locator = File.createTempFile("LocatorFile", "locator");				
				bookitBabyTempDir = new File(locator.getParent() + "/BookItBaby-temp");
				bookitBabyTempDir.mkdirs();

				locator.delete();
				
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					deleteFolder(bookitBabyTempDir);
				}));
			}
			
			File parentFolder = new File(bookitBabyTempDir.getPath() + File.separator + name);
			parentFolder.mkdirs();
		
			return parentFolder;
			
		} catch (Exception e) {}
		
		return null;
	}
	
	public static File createTempFile(String name, String suffix, String dir, boolean addUniqueNumberToName) {
		try {
			if(bookitBabyTempDir == null) {			
				File locator = File.createTempFile("LocatorFile", "locator");				
				bookitBabyTempDir = new File(locator.getParent() + "/BookItBaby-temp");
				bookitBabyTempDir.mkdirs();
				
				bookitBabyTempDir.deleteOnExit();
				locator.delete();
			}
			
			File parentFolder = new File(bookitBabyTempDir.getPath() + "/" + dir);
			parentFolder.mkdirs();
			
			File temp;
			if(addUniqueNumberToName) {
				temp = File.createTempFile(name, suffix, parentFolder);
				temp.deleteOnExit();
			} else {
				temp = new File(parentFolder.getPath() + "/" + name + suffix);
				temp.createNewFile();
				temp.deleteOnExit();
			}
			
			
			return temp;
			
		} catch (Exception e) {}
		
		return null;
	}
	
	public static void deleteTempFolder() {
		if(bookitBabyTempDir != null)
			deleteFolder(bookitBabyTempDir);
		
		bookitBabyTempDir = null;
	}
	
	
		
	public static void zipFolder(File folderLocation, String zipFilePath) {
		SOURCE_FOLDER = folderLocation.getAbsolutePath();
		ArrayList<String> fileList = new ArrayList<>();
		for (File file : folderLocation.listFiles())
			generateFileList(file, fileList);

		zipIt(zipFilePath, fileList);
	}
	public static void unzipFolder(File zipLocation, String folderFilePath) {
		extract(zipLocation, new File(folderFilePath));
	}
	
	private static void extract(File zipFile, File target) {
		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry entry;

			while ((entry = zip.getNextEntry()) != null) {
				File file = new File(target, entry.getName());

				if (!file.toPath().normalize().startsWith(target.toPath())) {
					throw new IOException("Bad zip entry");
				}

				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}

				byte[] buffer = new byte[4096];
				file.getParentFile().mkdirs();
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				int count;

				while ((count = zip.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();
			}
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String SOURCE_FOLDER;
	private static void generateFileList(File node, ArrayList<String> fileList) {

		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), fileList);
			}
		}
	}
	private static String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}
	private static void zipIt(String zipFile, ArrayList<String> fileList) {
		byte[] buffer = new byte[1024];
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			FileInputStream in = null;

			for (String file : fileList) {
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
