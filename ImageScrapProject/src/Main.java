import java.io.File;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
public class Main {
	public static void main(String[] args) {
		
		// Bu kod, eğitim amaçlı olarak hazırlanmıştır.
        // Web scraping ve Selenium kullanarak Google Görseller'den resim indirme işlemi gerçekleştirmektedir.
        // 
        // This code is prepared for educational purposes.
        // It performs image downloading from Google Images using web scraping and Selenium.

		//scrap();
		
		//removeDuplicateImages();
		
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ImageHelper imageHelper = new ImageHelper();
        String imagePath = "C:\\Users\\umutg\\OneDrive\\Belgeler\\YazLabSonVerilerKopya\\PituitaryTumor";
        //ImageHelper.renameImagesInFolder(imagePath, "notumor-k");
        
        //toGrayScale(imageHelper, imagePath);
        //augmentImages(imageHelper, imagePath+"\\Grayscale");
        resizeImages(imageHelper, imagePath,256,256);
        //ImageHelper.renameImagesInFolder(imagePath, "glioma-k");
    }
		
		
	public static void toGrayScale(ImageHelper imageHelper, String folderPath) {
	    // Gri tonlama görüntüleri için klasörü oluşturun
	    String grayFolderPath = folderPath + "\\Grayscale";
	    createDirectory(grayFolderPath);
	    File folder = new File(folderPath);
	    File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
	    
	    if (listOfFiles != null) {
	        for (File file : listOfFiles) {
	            Mat image = Imgcodecs.imread(file.getAbsolutePath());
	            if (image.empty()) {
	                System.err.println("Error - Image is empty:" + file.getAbsolutePath());
	                continue;
	            }
	            Mat grayImage = imageHelper.toGrayscale(image);
	            String grayImagePath = grayFolderPath + "\\" + file.getName(); // Gri tonlama görüntüleri için klasör
	            imageHelper.saveImage(grayImage, grayImagePath);
	        }
	    } else {
	        System.out.println("Klasör boş veya bulunamadı.");
	    }
	}


	    // Resimleri artırma işlemi
	    public static void augmentImages(ImageHelper imageHelper, String folderPath) {
	        File folder = new File(folderPath);
	        File[] listOfFiles = folder.listFiles();

	        if (listOfFiles != null) {
	            for (File file : listOfFiles) {
	                if (file.isFile() && isImageFile(file)) {
	                    Mat image = Imgcodecs.imread(file.getAbsolutePath());
	                    if (image.empty()) {
	                        System.err.println("Error: Could not open or find the image: " + file.getAbsolutePath());
	                        continue;
	                    }

	                    Mat flippedImage = imageHelper.flipImage(image, 1); // Yatay flip
	                    String flippedOutputPath = folderPath + File.separator + "flipped_" + file.getName();
	                    imageHelper.saveImage(flippedImage, flippedOutputPath);

	                    Mat rotatedImage = imageHelper.rotateImage(image, 90); // 90 derece döndür
	                    String rotatedOutputPath = folderPath + File.separator + "rotated_" + file.getName();
	                    imageHelper.saveImage(rotatedImage, rotatedOutputPath);

	                    Mat adjustedImage = imageHelper.adjustContrastAndBrightness(image, 1.2, 0); // Kontrast artırma
	                    String adjustedOutputPath = folderPath + File.separator + "adjusted_" + file.getName();
	                    imageHelper.saveImage(adjustedImage, adjustedOutputPath);
	                    
	                }
	            }
	        }
	    }
	    
	    public static void resizeImages(ImageHelper imageHelper,String folderPath,int width ,int height) {
	        File folder = new File(folderPath);
	        File[] listOfFiles = folder.listFiles();
		    String resizedFolderPath = folderPath + "\\Resized";
		    createDirectory(resizedFolderPath);
	        if (listOfFiles != null) {
	            for (File file : listOfFiles) {
	                if (file.isFile() && isImageFile(file)) {
	                    Mat image = Imgcodecs.imread(file.getAbsolutePath());
	                    if (image.empty()) {
	                        System.err.println("Error: Could not open or find the image: " + file.getAbsolutePath());
	                        continue;
	                    }
	                    
	                    Mat resizedImage = imageHelper.resizeImage(image, width, height);
	                    String resizedOutputPath = resizedFolderPath + File.separator + "resized_" + file.getName();
	                    imageHelper.saveImage(resizedImage, resizedOutputPath);
	                    
	                }
	            }
	        }
	    }

	    // Resim dosyası kontrolü
	    private static boolean isImageFile(File file) {
	        String[] imageExtensions = {".jpg", ".jpeg", ".png"};
	        for (String extension : imageExtensions) {
	            if (file.getName().toLowerCase().endsWith(extension)) {
	                return true;
	            }
	        }
	        return false;
	    }

	    // Klasör oluşturma metodu
	    private static void createDirectory(String dirPath) {
	        File directory = new File(dirPath);
	        if (!directory.exists()) {
	            boolean created = directory.mkdirs();
	            if (created) {
	                System.out.println("Klasör oluşturuldu: " + dirPath);
	            } else {
	                System.err.println("Klasör oluşturulamadı: " + dirPath);
	            }
	        }
	    }
	    
	    
	public static void removeDuplicateImages() {
        String folderPath = "C:\\Users\\umutg\\OneDrive\\Belgeler\\YazLabSonVeriler\\PituitaryTumor";
        ImageHelper.removeDuplicateImages(folderPath);
	}
	
	public static void scrap() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        String downloadPath = "C:\\Users\\umutg\\OneDrive\\Belgeler\\Yazilim-Lab-Proje\\Yedekler\\Normal+"; // Change this to your preferred directory

        System.out.print("Enter number of images to download: ");
        int imageCount = scanner.nextInt();

        System.out.print("Enter wait time in milliseconds: ");
        int waitTime = scanner.nextInt();
        scanner.nextLine(); // Tüketilen yeni satırı temizle

        System.out.print("Enter browser type (CHROME / FIREFOX): ");
        String browserInput = scanner.nextLine().toUpperCase();
        BrowserType browserType = BrowserType.valueOf(browserInput);
        
     
        Counter counter = new Counter();

        // get start id
        System.out.print("Start Id:");
        int id = scanner.nextInt();
        counter.setImageNameIdentifierCount(id);
        scanner.close();
   
        ImageScraper scraper = new GoogleImageScraper(downloadPath, imageCount, waitTime,counter, browserType);
        scraper.downloadImages(searchTerm);
        scraper.quit();
	}
}