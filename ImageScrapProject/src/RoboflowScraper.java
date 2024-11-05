import java.time.Duration;
import java.util.Random;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
	

	 @Override
	    public void downloadImages(String url) {
	        driver.get(url);
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(webDriverWaitSeconds));

	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.relative.h-24.w-24:first-of-type")));
	        element.click();

	        while (true) {
	            try {
	                WebElement classElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.used.has-used > div:only-child")));
	                String className = classElement.getText();
	                System.out.println(className);

	                // rawDataButton öğesini JavaScript ile tıklama
	                WebElement rawDataButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-section=\"raw\"]")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rawDataButton);

	                // Kısa bir bekleme
	                Thread.sleep(1000);

	                WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(45));
	                WebElement rawDataElement = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rawPanel > div:nth-child(2) > pre")));
	                String rawData = rawDataElement.getText();

	                JSONObject jsonObject = new JSONObject(rawData);
	                String extension = jsonObject.getString("extension");
	                String id = jsonObject.getString("id");
	                String owner = jsonObject.getString("owner");

	                System.out.println("Extension: " + extension);
	                System.out.println("ID: " + id);
	                System.out.println("Owner: " + owner);

	                String imageUrl = "https://source.roboflow.com/" + owner + "/" + id + "/original." + extension;
	                System.out.println(imageUrl);

	                try {
	                    Random rnd = new Random();
	                    downloadImage(imageUrl, downloadPath + "\\" + IMAGE_FILENAME_PREFIX + counter.getImageNameIdentifierCount() + IMAGE_EXTENSION);
	                    counter.incrementDownload();
	                    counter.setImageNameIdentifierCount(counter.getImageNameIdentifierCount() + rnd.nextInt(4) + 1);
	                    System.out.println(counter.getDownloadCount() + " - " + imageUrl);
	                } catch (RuntimeException e) {
	                    counter.incrementError();
	                    System.out.println("Image download failed: " + e.getMessage());
	                }

	                WebElement labelsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("annotationsTab")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", labelsButton);

	                WebElement counterDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.number.text-sm.text-gray-700")));
	                String counterElementText = counterDiv.getText();
	                String[] parts = counterElementText.split(" / ");

	                if (parts.length == 2 && parts[0].trim().equals(parts[1].trim())) {
	                    break;
	                }

	                if (imageCount != -1 && counter.getDownloadCount() >= imageCount) {
	                    break;
	                }

	                boolean clicked = false;
	                int retries = 0;
	                while (!clicked && retries < 3) {
	                    try {
	                        WebElement nextIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.next.icon")));
	                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextIcon);
	                        clicked = true;
	                    } catch (StaleElementReferenceException e) {
	                        System.out.println("Stale element reference encountered, retrying...");
	                        retries++;
	                    }
	                }

	                if (!clicked) {
	                    System.out.println("Failed to click next icon after multiple retries.");
	                    break;
	                }

	                counter.incrementRequest();
	            } catch (StaleElementReferenceException e) {
	                System.out.println("Encountered StaleElementReferenceException, retrying main loop...");
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
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