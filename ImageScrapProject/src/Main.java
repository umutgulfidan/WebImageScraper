import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		// Bu kod, eğitim amaçlı olarak hazırlanmıştır.
        // Web scraping ve Selenium kullanarak Google Görseller'den resim indirme işlemi gerçekleştirmektedir.
        // 
        // This code is prepared for educational purposes.
        // It performs image downloading from Google Images using web scraping and Selenium.
		
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        String downloadPath = "C:\\Users\\Mert\\Desktop\\SeleniumTest"; // Change this to your preferred directory

        System.out.print("Enter number of images to download: ");
        int imageCount = scanner.nextInt();

        System.out.print("Enter wait time in milliseconds: ");
        int waitTime = scanner.nextInt();
        scanner.nextLine(); // Tüketilen yeni satırı temizle

        System.out.print("Enter browser type (CHROME / FIREFOX): ");
        String browserInput = scanner.nextLine().toUpperCase();
        BrowserType browserType = BrowserType.valueOf(browserInput);

        scanner.close();
        
        
        ImageScraper scraper = new GoogleImageScraper(downloadPath, imageCount, waitTime, browserType);
        scraper.downloadImages(searchTerm);
        scraper.quit();

	}
	
}
