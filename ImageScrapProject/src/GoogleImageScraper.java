import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;

    public class GoogleImageScraper extends BaseImageScraper {

        // Configurable settings
        private String googleImagesUrl = "https://images.google.com/"; // Google Images URL
        private String searchBoxSelector = "q"; // Search box selector
        private String imageSelector = ".H8Rx8c g-img img.YQ4gaf"; // Image selector
        private String downloadableImageSelector  = "div.p7sI2 a img.sFlh5c.FyHeAf.iPVvYb"; // Selector for the image to download
        private int waitDuration = 7; // Wait time in seconds

        // Constructor
        public GoogleImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, BrowserType browserType) {
            super(downloadPath, imageCount, waitTime,counter, browserType);
        }
        public GoogleImageScraper(String downloadPath, int imageCount, int waitTime,Counter counter, WebDriver driver) {
            super(downloadPath, imageCount, waitTime,counter, driver);
        }

        @Override
        public void downloadImages(String searchTerm) {
            driver.get(googleImagesUrl); // Google Görseller sayfasına git
            WebElement searchBox = driver.findElement(By.name(searchBoxSelector)); // Arama kutusunu bul
            searchBox.sendKeys(searchTerm); // Arama terimini gir
            searchBox.submit(); // Arama terimini gönder

            wait(waitTime); // Sayfanın yüklenmesini bekle
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration)); // WebDriverWait ayarla
            
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
                    webElement.click(); // Resme tıkla

                    try {
                        WebElement downloadableImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(downloadableImageSelector))); // Resmin görünmesini bekle
                        String imageUrl = downloadableImage.getAttribute("src"); // Resim URL'sini al

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
