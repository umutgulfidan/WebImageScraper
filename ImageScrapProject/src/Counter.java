public class Counter {
    private int requestCount;
    private int downloadCount;
    private int errorCount;
    private int imageNameIdentifierCount;

    public Counter() {
        this.requestCount = 0;
        this.downloadCount = 0;
        this.errorCount = 0;
        this.imageNameIdentifierCount = 0;
    }

    public void incrementRequest() {
        requestCount++;
    }

    public void incrementDownload() {
        downloadCount++;
    }

    public void incrementError() {
        errorCount++;
    }
    public void incrementImageNameIdentifier() {
    	imageNameIdentifierCount++;
    }

    public void reset() {
        requestCount = 0;
        downloadCount = 0;
        errorCount = 0;
        imageNameIdentifierCount = 1;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public int getErrorCount() {
        return errorCount;
    }
    public int getImageNameIdentifierCount(){
    	return imageNameIdentifierCount;
    }
    public void setImageNameIdentifierCount(int value){
    	this.imageNameIdentifierCount = value;
    }

    @Override
    public String toString() {
        return "Requests: " + requestCount + ", Downloads: " + downloadCount + ", Errors: " + errorCount;
    }
    // Summarize the results of the scraping
    public void summarizeResults() {
        System.out.println("---------------RESULT---------------");
        System.out.println("Requested Images: " + requestCount);
        System.out.println("Downloaded Images: " + downloadCount);
        System.out.println("Errors encountered: " + errorCount);
        System.out.println("------------------------------------");
    }
}