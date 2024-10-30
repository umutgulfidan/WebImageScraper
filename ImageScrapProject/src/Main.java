import java.util.Scanner;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
	public static void main(String[] args) {
		
		// Bu kod, eğitim amaçlı olarak hazırlanmıştır.
        // Web scraping ve Selenium kullanarak Google Görseller'den resim indirme işlemi gerçekleştirmektedir.
        // 
        // This code is prepared for educational purposes.
        // It performs image downloading from Google Images using web scraping and Selenium.
		
		// Google Image Scraper for Main
		
		
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        String downloadPath = "C:\\Users\\umutg\\OneDrive\\Masaüstü\\Yazılım-Lab-Proje\\PituitaryTumor"; // Change this to your preferred directory

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
        
		
		
		/*
        Scanner scanner = new Scanner(System.in);
		
        String downloadPath = "C:\\Users\\umutg\\OneDrive\\Masaüstü\\Yazılım-Lab-Proje"; // Change this to your preferred directory

        System.out.print("Enter number of images to download: ");
        int imageCount = scanner.nextInt();
        
        System.out.print("Enter wait time in milliseconds: ");
        int waitTime = scanner.nextInt();
        scanner.nextLine(); // Tüketilen yeni satırı temizle
        
        ChromeOptions options = new ChromeOptions();
        
        // Kullanıcı ajanını değiştirin
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36");
        
        // Tarayıcı penceresinin boyutunu ayarlayın
        options.addArguments("window-size=1920,1080");
        // Tarayıcı penceresini tam ekran yap
        options.addArguments("--start-fullscreen");
        
        // Başka bazı argümanlar
        options.addArguments("--disable-infobars"); // Bilgilendirme çubuklarını kapat
        options.addArguments("--disable-notifications"); // Bildirimleri kapat
        options.addArguments("--disable-extensions"); // Uzantıları devre dışı bırak
        
        WebDriver driver = new ChromeDriver(options);
        Counter counter = new Counter();
        counter.setImageNameIdentifierCount(51);
		ImageScraper scraper = new RoboflowScraper(downloadPath,imageCount,waitTime,counter,driver);
		scraper.downloadImages("https://universe.roboflow.com/think-tank-dt0nl/brain-tumor-bsq3w/browse?queryText=&pageSize=50&startingIndex=0&browseQuery=true");
		driver.close();
		*/
		
		
		
        //String folderPath = "C:\\Users\\umutg\\OneDrive\\Masaüstü\\Yazılım-Lab-Proje\\NormalBrain";
        //ImageDuplicateRemover.removeDuplicateImages(folderPath);
        
		
		
	}
}