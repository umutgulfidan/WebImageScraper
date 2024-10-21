import java.time.Duration;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RoboflowScraper extends BaseImageScraper {
	int  webDriverWaitSeconds = 30;
	
	public RoboflowScraper(String downloadPath, int imageCount, int waitTime, Counter counter,
			BrowserType browserType) {
		super(downloadPath, imageCount, waitTime, counter, browserType);
	}
	public RoboflowScraper(String downloadPath, int imageCount, int waitTime,
			BrowserType browserType) {
		super(downloadPath, imageCount, waitTime,browserType);
	}
	public RoboflowScraper(String downloadPath, int imageCount, int waitTime, Counter counter,
			WebDriver driver) {
		super(downloadPath, imageCount, waitTime, counter, driver);
	}
	public RoboflowScraper(String downloadPath, int imageCount, int waitTime,
			WebDriver driver) {
		super(downloadPath, imageCount, waitTime,driver);
	}
	
	// url like : https://universe.roboflow.com/think-tank-dt0nl/brain-tumor-bsq3w/browse 
	@Override
	public void downloadImages(String url) {
		driver.get(url);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(webDriverWaitSeconds)); // Setup WebDriverWait
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.relative.h-24.w-24:first-of-type")));
		element.click();
		
		
		
		while(counter.getDownloadCount()<imageCount) {
			

			
			WebElement classElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.used.has-used > div:only-child")));
			String className = classElement.getText();
			System.out.println(className);
			WebElement rawDataButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section=\"raw\"]")));
			
			rawDataButton.click();

			WebElement rawDataElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rawPanel > div:nth-child(2) > pre")));
			String rawData = rawDataElement.getText();

	        // JSON nesnesi oluştur
			JSONObject jsonObject = new JSONObject(rawData);
	        // Değerleri ayıklama
	        String extension = jsonObject.getString("extension");
	        String id = jsonObject.getString("id");
	        String owner = jsonObject.getString("owner");

	        // Sonuçları yazdırma
	        System.out.println("Extension: " + extension);
	        System.out.println("ID: " + id);
	        System.out.println("Owner: " + owner);
	        
	        String imageUrl = "https://source.roboflow.com/"+owner+"/"+id+"/original."+extension;
	        System.out.println(imageUrl);
	        
	        try {
                downloadImage(imageUrl, downloadPath + "\\"+IMAGE_FILENAME_PREFIX + (counter.getImageNameIdentifierCount()) + "." + IMAGE_EXTENSION);
                counter.incrementImageNameIdentifier();
                counter.incrementDownload();
                System.out.println(counter.getDownloadCount() + " - " + imageUrl);
            } catch (RuntimeException e) {
                // Catch downloadImage errors
            	counter.incrementError();
                System.out.println("Image download failed: " + e.getMessage());
            }
	        
			WebElement labelsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("annotationsTab")));
			labelsButton.click();
	        
			//
			
			// Döngüyü ilerletme kısmı
			WebElement counterDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.number.text-sm.text-gray-700")));
	        String counterElementText = counterDiv.getText();
	        // Yazıyı "/" ile ayır
	        String[] parts = counterElementText.split(" / ");
        
	        // Eğer yazının slashtan önceki ve sonraki kısımları eşitse, döngüden çık
	        if (parts.length == 2 && parts[0].trim().equals(parts[1].trim())) {
	        	break;
	        }
	        
	        boolean clicked = false;
	        while (!clicked) {
	            try {
	                WebElement nextIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.next.icon")));
	                nextIcon.click();
	                clicked = true; // If click is successful, set clicked to true
	            } catch (StaleElementReferenceException e) {
	                // If the element is stale, we will retry finding and clicking it
	                System.out.println("Stale element reference encountered, retrying...");
	            }
	        }
	        
	        counter.incrementRequest();
		}
		counter.summarizeResults();
		driver.close();
	}
    @Override
    public void setDownloadPath(String path) {
        this.downloadPath = path;
    }

    @Override
    public void setImageCount(int count) {
        this.imageCount = count;
    }

    @Override
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}