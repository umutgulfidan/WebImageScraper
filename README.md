# Web Image Collector

A simple and efficient tool for downloading images from Google Images based on specified search terms.

## Requirements

- Java 11 or higher
- Selenium WebDriver
- A compatible web browser (Chrome or Firefox)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/umutgulfidan/WebImageCollector.git
   
2. Navigate to the project directory:

   ```bash
   cd WebImageCollector

4. Add the Selenium dependencies to your project.

5. Usage

    Set your desired download path and image count in the code.
    Create an instance of GoogleImageScraper and call the downloadImages method with your search term.
    Run the application to start downloading images.
    - Example
       ```java
       GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", 10, 5000, BrowserType.CHROME);
       scraper.downloadImages("cats");

## Acknowledgements
- [Selenium Documentation](https://www.selenium.dev/documentation/en/)
- [Medium](https://medium.com/@dian.octaviani/method-1-4-automation-of-google-image-scraping-using-selenium-3972ea3aa248)
