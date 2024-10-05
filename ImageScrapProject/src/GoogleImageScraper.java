import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class GoogleImageScraper extends BaseImageScraper {

    // Configurable settings
    private String googleImagesUrl = "https://images.google.com/"; // Google Images URL
    private String searchBoxSelector = "q"; // Search box selector
    private String imageSelector = ".H8Rx8c g-img img.YQ4gaf"; // Image selector
    private String imageToDownloadSelector = "div.p7sI2 a img.sFlh5c.FyHeAf.iPVvYb"; // Selector for the image to download
    private int waitDuration = 7; // Wait time in seconds

    // Constructor
    public GoogleImageScraper(String downloadPath, int imageCount, int waitTime, BrowserType browserType) {
        super(downloadPath, imageCount, waitTime, browserType);
    }

    @Override
    public void downloadImages(String searchTerm) {
        driver.get(googleImagesUrl); // Navigate to Google Images
        WebElement searchBox = driver.findElement(By.name(searchBoxSelector)); // Locate the search box
        searchBox.sendKeys(searchTerm); // Input the search term
        searchBox.submit(); // Submit the search

        wait(waitTime); // Wait for the page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration)); // Setup WebDriverWait

        while (downloadedImages < imageCount) {
            List<WebElement> images = driver.findElements(By.cssSelector(imageSelector)); // Find images
            System.out.println("Found images: " + images.size());

            if (images.size() < (imageCount - downloadedImages)) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); // Scroll down to load more images
                wait(waitTime); // Wait after scrolling
                continue;
            }

            for (WebElement webElement : images) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement); // Scroll to the image
                webElement.click(); // Click on the image

                try {
                    WebElement imageToDownload = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(imageToDownloadSelector))); // Wait for the image to be visible
                    String imageUrl = imageToDownload.getAttribute("src"); // Get image URL

                    // Check URL validity
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Download the image
                        try {
                            downloadImage(imageUrl, downloadPath + "\\image" + (downloadedImages + 1) + ".jpg");
                            downloadedImages++; 
                            System.out.println(downloadedImages + " - " + imageUrl);
                        } catch (RuntimeException e) {
                            // Catch downloadImage errors
                            System.out.println("Image download failed: " + e.getMessage());
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Image URL is invalid.");
                    }
                } catch (Exception e) {
                    errorCounter++; 
                    System.out.println("Error occurred: " + e.getMessage());
                }

                requestCounter++; 

                // Exit loop if the target number of images is reached
                if (downloadedImages >= imageCount) {
                    break;
                }
            }
        }

        summarizeResults();
    }

    // Summarize the results of the scraping
    private void summarizeResults() {
        System.out.println("---------------RESULT---------------");
        System.out.println("Requested Images: " + requestCounter);
        System.out.println("Downloaded Images: " + downloadedImages);
        System.out.println("Errors encountered: " + errorCounter);
        System.out.println("------------------------------------");
    }

    // Setup function to customize parameters
    public void setup(String googleImagesUrl, String searchBoxSelector, String imageSelector,
                      String imageToDownloadSelector, int waitDuration) {
        this.googleImagesUrl = googleImagesUrl;
        this.searchBoxSelector = searchBoxSelector;
        this.imageSelector = imageSelector;
        this.imageToDownloadSelector = imageToDownloadSelector;
        this.waitDuration = waitDuration;
    }

    // Getter and Setter methods
    public String getGoogleImagesUrl() {
        return googleImagesUrl;
    }

    public void setGoogleImagesUrl(String googleImagesUrl) {
        this.googleImagesUrl = googleImagesUrl;
    }

    public String getSearchBoxSelector() {
        return searchBoxSelector;
    }

    public void setSearchBoxSelector(String searchBoxSelector) {
        this.searchBoxSelector = searchBoxSelector;
    }

    public String getImageSelector() {
        return imageSelector;
    }

    public void setImageSelector(String imageSelector) {
        this.imageSelector = imageSelector;
    }

    public String getImageToDownloadSelector() {
        return imageToDownloadSelector;
    }

    public void setImageToDownloadSelector(String imageToDownloadSelector) {
        this.imageToDownloadSelector = imageToDownloadSelector;
    }

    public int getWaitDuration() {
        return waitDuration;
    }

    public void setWaitDuration(int waitDuration) {
        this.waitDuration = waitDuration;
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
