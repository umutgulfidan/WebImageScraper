# Web Image Scraper

A simple and efficient tool for downloading images from Google Images based on specified search terms.

## Requirements

- Java 11 or higher
- Selenium WebDriver
- A compatible web browser (Chrome or Firefox)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/umutgulfidan/WebImageScraper.git
   
2. Navigate to the project directory:

   ```bash
   cd WebImageScraper

3. Add the Selenium dependencies to your project.
4. Add [Json Library](https://github.com/stleary/JSON-java/tree/master)

5. Usage
    To scrape images from Google Images, follow these steps:

    Set your desired download path and image count:
      
    Define where the images will be saved.
    Set the number of images to download. If you want to download all available images, use -1 for imageCount.
    Create an instance of GoogleImageScraper and call downloadImages method: Pass in the search term for the images you want to download.
    - Example: Download 10 Images of Cats from Google Images
       ```java
        GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", 10, 5000, BrowserType.CHROME);
        scraper.downloadImages("cats");
   - Example: Download All Available Images of Dogs
     ```java
     //If you set imageCount to -1, the scraper will download all available images from the search results page.
     GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", -1, 3000, BrowserType.FIREFOX);
     scraper.downloadImages("dogs");
     scraper.quit();  // Quit the browser after the scraping session
   - Example: Interactive Example with User Input
     ```java
     import java.util.Scanner;
      
      public class Main {
          public static void main(String[] args) {
              Scanner scanner = new Scanner(System.in);
              System.out.print("Enter search term: ");
              String searchTerm = scanner.nextLine();
      
              System.out.print("Enter number of images to download (-1 for all): ");
              int imageCount = scanner.nextInt();
      
              System.out.print("Enter wait time in milliseconds: ");
              int waitTime = scanner.nextInt();
              scanner.nextLine(); // Clean up the newline
      
              System.out.print("Enter browser type (CHROME / FIREFOX): ");
              String browserInput = scanner.nextLine().toUpperCase();
              BrowserType browserType = BrowserType.valueOf(browserInput);
      
              scanner.close();
              
              Counter counter = new Counter();
              counter.setImageNameIdentifierCount(1);  // You can start naming from a custom ID
      
              GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", imageCount, waitTime, counter, browserType);
              scraper.downloadImages(searchTerm);
              scraper.quit();  // Ensure to quit the browser after use
          }
      }

     

## Acknowledgements
- [Selenium Documentation](https://www.selenium.dev/documentation/en/)
- [Medium](https://medium.com/@dian.octaviani/method-1-4-automation-of-google-image-scraping-using-selenium-3972ea3aa248)
