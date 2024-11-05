import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MedpixImageScraper extends BaseImageScraper {
    private String medpixUrl = "https://medpix.nlm.nih.gov/search"; // Google Images URL
    private String searchBoxId = "search-query";
    private String imagesButtonSelector ="#content > div.horizontal-menu.ng-scope > div:nth-child(2)";
    private String imageSelector = "img.image.ng-scope";
    private String fullResImageSelector = "#search-results-container > div > div.cow-container.selected.info > div > div > div.col-md-6.col-lg-5.visible-lg.visible-md.image.ng-scope > img";

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
	    wait(waitTime);

	    int totalImageCount = 0;
	    boolean scrollComplete = false;

	    while (!scrollComplete) {
	        List<WebElement> images = driver.findElements(By.cssSelector(imageSelector));
	        totalImageCount = images.size();
	        System.out.println("Bulunan resim sayısı: " + totalImageCount);

	        long oldImageCount = images.size();
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        wait(waitTime);
	        images = driver.findElements(By.cssSelector(imageSelector));

	        scrollComplete = (oldImageCount == images.size());
	    }

	    if (imageCount == -1) {
	        imageCount = totalImageCount;
	    }

	    System.out.println("Bulunan resim sayısı: " + totalImageCount);
	    System.out.println("İndirilecek toplam resim sayısı: " + imageCount);

	    while (counter.getDownloadCount() < imageCount) {
	        List<WebElement> images = driver.findElements(By.cssSelector(imageSelector));

	        for (int i = 0; i < images.size(); i++) {
	            try {
	                WebElement webElement = images.get(i);
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement);
	                wait(waitTime);

	                webElement.click();
	                WebElement fullResImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(fullResImageSelector)));
	                String imageUrl = fullResImage.getAttribute("src");

	                if (imageUrl != null && !imageUrl.isEmpty()) {
	                    downloadImage(imageUrl, downloadPath + "\\" + IMAGE_FILENAME_PREFIX + (counter.getImageNameIdentifierCount()) + IMAGE_EXTENSION);
	                    counter.incrementImageNameIdentifier();
	                    counter.incrementDownload();
	                } else {
	                    counter.incrementError();
	                    System.out.println("Resim URL geçersiz.");
	                }

	                webElement.click();
	                counter.incrementRequest();

	                if (counter.getDownloadCount() >= imageCount) {
	                    break;
	                }
	                if (counter.getRequestCount() >= totalImageCount) {
	                    System.out.println("Tüm resimler denendi ancak istenilen sayıda resim indirilemedi.");
	                    System.out.println("Hedeflenen resim sayısı: " + imageCount);
	                    System.out.println("İndirilen resim sayısı: " + counter.getDownloadCount());
	                    System.out.println("Başarısız istek sayısı: " + counter.getErrorCount());
	                    return;
	                }
	            } catch (StaleElementReferenceException e) {
	                System.out.println("Hata: Stale element referansı. Öğeyi tekrar bul ve devam et.");
	                images = driver.findElements(By.cssSelector(imageSelector)); // Resim listesini tekrar yükle
	                i--; // Aynı index ile devam et
	            } catch (Exception e) {
	                counter.incrementError();
	                System.out.println("Hata oluştu: " + e.getMessage());
	            }
	        }
	    }

	    counter.summarizeResults();
	}

	
}