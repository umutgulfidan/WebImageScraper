import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OpeniWebScraper extends BaseImageScraper {
	private String openiUrl = "https://openi.nlm.nih.gov/gridquery";
    private String searchBoxId = "search-text";
    private String imageSelector = "#grid img";

	public OpeniWebScraper(String downloadPath, int imageCount, int waitTime, Counter counter,
			BrowserType browserType) {
		super(downloadPath, imageCount, waitTime, counter, browserType);
		// TODO Auto-generated constructor stub
	}
	
	public OpeniWebScraper(String downloadPath, int imageCount, int waitTime, Counter counter,
			WebDriver driver) {
		super(downloadPath, imageCount, waitTime, counter, driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void downloadImages(String searchTerm) {
		driver.get(openiUrl);
		WebElement searchBox = driver.findElement(By.id(searchBoxId));
		searchBox.click();
		searchBox.sendKeys(searchTerm);
		searchBox.sendKeys(Keys.ENTER);
		
           // Resimleri indirme döngüsü
           while (counter.getDownloadCount() < imageCount) {
        	   
        	   WebElement spinner = driver.findElement(By.className("grid-query-spinner"));
               wait.until(ExpectedConditions.attributeToBe(spinner, "style", "display: none;"));
               
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
                   
                   // Şu anki URL'yi alın
                   String currentUrl = driver.getCurrentUrl();

                   // n= ve m= parametrelerini işlemek için düzenli ifadeleri tanımlayın
                   String nPattern = "n=(\\d+)";
                   String mPattern = "m=(\\d+)";

                   // n değerini güncelle
                   Pattern nRegex = Pattern.compile(nPattern);
                   Matcher nMatcher = nRegex.matcher(currentUrl);
                   if (nMatcher.find()) {
                       int nValue = Integer.parseInt(nMatcher.group(1));
                       currentUrl = currentUrl.replace(nMatcher.group(0), "n=" + (nValue + 100));
                   }

                   // m değerini güncelle
                   Pattern mRegex = Pattern.compile(mPattern);
                   Matcher mMatcher = mRegex.matcher(currentUrl);
                   if (mMatcher.find()) {
                       int mValue = Integer.parseInt(mMatcher.group(1));
                       currentUrl = currentUrl.replace(mMatcher.group(0), "m=" + (mValue + 100));
                   }

                   // Güncellenmiş URL'yi yazdırın
                   System.out.println("Güncellenmiş URL: " + currentUrl);

                   // Güncellenmiş URL'ye gidin
                   driver.get(currentUrl);
                   wait(waitTime);
               }
           }

           counter.summarizeResults();
		
	}

}
