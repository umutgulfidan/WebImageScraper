import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public abstract class BaseImageScraper implements ImageScraper {
    protected WebDriver driver; // Selenium WebDriver instance
    protected int waitDuration =9;
    protected WebDriverWait wait; // Setup WebDriverWait
    protected String downloadPath; // Download path
    protected int imageCount; // Total images to download
    protected Counter counter;
    protected int waitTime; // Wait time for page loading
    protected static final String IMAGE_FILENAME_PREFIX = "pituitary";
    protected static final String IMAGE_EXTENSION = ".jpg";

    // Constructor with parameters
    public BaseImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, BrowserType browserType) {
        this.downloadPath = downloadPath;
        this.imageCount = imageCount;
        this.waitTime = waitTime;
        this.counter=counter;

        // Set up the browser
        if (browserType == BrowserType.CHROME) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized"); // Open browser in fullscreen
            this.driver = new ChromeDriver(options);
        } else if (browserType == BrowserType.FIREFOX) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-start-maximized"); // Open browser in fullscreen
            this.driver = new FirefoxDriver(options);
        }
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
    }
    public BaseImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, WebDriver driver) {
        this.downloadPath = downloadPath;
        this.imageCount = imageCount;
        this.waitTime = waitTime;
        this.driver = driver;
        this.counter = counter;
    }
    public BaseImageScraper(String downloadPath, int imageCount, int waitTime, WebDriver driver) {
    	this(downloadPath,imageCount,waitTime,new Counter(),driver);
    }
    public BaseImageScraper(String downloadPath, int imageCount, int waitTime, BrowserType browserType) {
    	this(downloadPath,imageCount,waitTime,new Counter(),browserType);
    }

    // Helper method to wait for a specified time
    protected void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected WebElement waitUntilCssSelector(String cssSelector) {
    	WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
    	return element;
    }
    protected WebElement waitUntilId(String id) {
    	WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    	return element;
    }
    protected WebElement waitUntilXPath(String xpath) {
    	WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    	return element;
    }


    // Close the WebDriver
    @Override
    public void quit() {
        if (driver != null) {
            driver.quit(); // Close the browser
        }
    }

    // Download an image from the specified URL
    protected void downloadImage(String imageUrl, String destinationPath) {
        try {
            URI uri = URI.create(imageUrl); // Convert URL to URI
            URL url = uri.toURL(); // Convert URI to URL
            try (InputStream in = url.openStream()) {
                // Save the image to the specified path
                Files.copy(in, Path.of(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image downloaded: " + destinationPath);
            }
        } catch (IOException e) {
            // Handle errors
            if (e.getMessage().contains("403")) {
                System.out.println("Access denied: " + imageUrl); // Access denied error
            } else {
                System.out.println("Error occurred while downloading image: " + e.getMessage()); // General error
            }
            counter.incrementError();; // Increment error counter
            throw new RuntimeException("Error occurred while downloading the image: " + e.getMessage()); // Throw exception with message
        }
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