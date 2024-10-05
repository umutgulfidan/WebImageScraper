public interface ImageScraper {
    /**
     * Downloads images based on the given search term.
     * @param searchTerm The term to search for images.
     */
    void downloadImages(String searchTerm);

    /**
     * Sets the download path for the images.
     * @param path The directory path where images will be saved.
     */
    void setDownloadPath(String path);

    /**
     * Sets the total number of images to download.
     * @param count The number of images to download.
     */
    void setImageCount(int count);

    /**
     * Sets the wait time between actions.
     * @param waitTime The time to wait in milliseconds.
     */
    void setWaitTime(int waitTime);

    /**
     * Quits the WebDriver session.
     */
    void quit();
}
