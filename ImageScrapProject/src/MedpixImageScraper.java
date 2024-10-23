import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MedpixImageScraper extends BaseImageScraper {
    private String medpixUrl = "https://medpix.nlm.nih.gov/search"; // Google Images URL
    private String searchBoxId = "search-query";
    private String imagesButtonSelector ="#content > div.horizontal-menu.ng-scope > div:nth-child(2)";
    private String imageSelector = "img.image.ng-scope";

	public MedpixImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, BrowserType browserType) {
		super(downloadPath, imageCount, waitTime,counter,browserType);
	}
	public MedpixImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, WebDriver driver) {
		super(downloadPath, imageCount, waitTime,counter, driver);
	}

	@Override
	public void downloadImages(String searchTerm) {
		driver.get(medpixUrl);
		WebElement searchBox = driver.findElement(By.id(searchBoxId));
		searchBox.click();
		searchBox.sendKeys(searchTerm);
		searchBox.sendKeys(Keys.ENTER);
		WebElement imagesButton = waitUntilCssSelector(imagesButtonSelector);
		imagesButton.click();
		
		   wait(waitTime); // Sayfanın yüklenmesini bekle
           
           int totalImageCount = 0; // Toplam resim sayısını tutacak değişken
           boolean scrollComplete = false;

           // Tüm resim sayısını öğrenmek için sayfada scroll yapıyoruz
           while (!scrollComplete) {
               List<WebElement> images = driver.findElements(By.cssSelector(imageSelector)); // Resimleri bul
               totalImageCount = images.size(); // Şu anki toplam resim sayısı
               System.out.println("Bulunan resim sayısı: " + totalImageCount);

               // Daha fazla resim yüklenmiş mi kontrol et
               long oldImageCount = images.size();
               ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); // Sayfanın sonuna kadar scroll yap
               wait(waitTime); // Scroll sonrası bekle
               images = driver.findElements(By.cssSelector(imageSelector));
               
               scrollComplete = (oldImageCount == images.size()); // Eğer daha fazla resim yüklenmemişse scroll'u durdur
           }
           
           // Kullanıcı tüm resimleri indirmek isterse veya belli bir sayı verilmemişse (örneğin imageCount == -1)
           if (imageCount == -1) {
               imageCount = totalImageCount; // Kullanıcının indirmek istediği resim sayısını tüm resimlere ayarla
           }
           
           System.out.println("Bulunan resim sayısı: "+totalImageCount);
           System.out.println("İndirilecek toplam resim sayısı: " + imageCount);

           // Resimleri indirme döngüsü
           while (counter.getDownloadCount() < imageCount) {
               List<WebElement> images = driver.findElements(By.cssSelector(imageSelector)); // Resimleri bul
               System.out.println("Bulunan resim sayısı: " + images.size());

               for (WebElement webElement : images) {
                   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement); // Resmin görünür olması için scroll yap
                   wait(waitTime);

                   try {               
                       String imageUrl = webElement.getAttribute("src"); // Resim URL'sini al

                       if (imageUrl != null && !imageUrl.isEmpty()) {
                           // Resmi indir
                           try {
                               downloadImage(imageUrl, downloadPath + "\\" + IMAGE_FILENAME_PREFIX + (counter.getImageNameIdentifierCount()) + IMAGE_EXTENSION);
                               counter.incrementImageNameIdentifier();
                               counter.incrementDownload();
                           } catch (RuntimeException e) {
                               System.out.println("Resim indirme başarısız oldu: " + e.getMessage());
                           }
                       } else {
                           counter.incrementError();
                           System.out.println("Resim URL geçersiz.");
                       }
                   } catch (Exception e) {
                       counter.incrementError();
                       System.out.println("Hata oluştu: " + e.getMessage());
                   }

                   counter.incrementRequest();

                   // Eğer indirilecek resim sayısına ulaşıldıysa döngüyü kır
                   if (counter.getDownloadCount() >= imageCount) {
                       break;
                   }
                   // Eğer istek sayısı toplam resim sayısına ulaştıysa (başarılı veya başarısız tüm denemeler yapıldıysa)
                   if (counter.getRequestCount() >= totalImageCount) {
                       System.out.println("Tüm resimler denendi ancak istenilen sayıda resim indirilemedi.");
                       System.out.println("Hedeflenen resim sayısı: "+imageCount);
                       System.out.println("İndirilen resim sayısı: " + counter.getDownloadCount());
                       System.out.println("Başarısız istek sayısı: " + counter.getErrorCount());
                       return; // Döngüyü kır ve methodu bitir
                   }
               }
           }

           counter.summarizeResults();
	}

}
