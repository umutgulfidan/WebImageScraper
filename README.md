# Web Image Scraper

Bu proje, Yazılım Geliştirme Laboratuvarı-I dersi kapsamında, beyin tümörü tiplerinin bilgisayarlı tomografi (BT) görüntüleri ile sınıflandırılması için kapsamlı bir veri seti oluşturmayı amaçlamaktadır. Menenjiyom, hipofiz tümörü, gliom ve tümörsüz beyin sınıflarını içeren veri seti, sınıflandırma modellerinde kullanılmak üzere hazırlanmıştır. Veriler, MedPix ve OpenI gibi Ulusal Sağlık Enstitüleri (NIH) ve Ulusal Tıp Kütüphanesi (NLM) tarafından geliştirilen tıbbi görüntüleme sitelerinden ve Google Görseller’den elde edilmiştir. Bu kod deposu ilgili verilerin toplanması ve ön işleme esnasında kullanılan kodları içermektedir.

## Gereksinimler

Projeyi çalıştırmadan önce aşağıdaki gereksinimlerin yüklü olduğundan emin olun:

1. **Java Development Kit (JDK) 23**: Projeyi geliştirmek ve çalıştırmak için gerekli olan Java derleyici.
2. **Selenium**: Web tarayıcı otomasyonu için kullanılan kütüphane. Projede **4.25.0** sürümü kullanılmaktadır. Bu sürüm veya daha yenisi kullanılabilir.
3. **OpenCV**: Görüntü işleme için kullanılan kütüphane. Projede **4.10.0** sürümü kullanılmaktadır. Bu sürüm veya daha yenisi kullanılabilir.
4. **JSON.org (JSONObject)**: JSON formatındaki verileri işlemek için kullanılan kütüphane.

> **Not:** Gerekli kütüphanelere ait bağlantılar proje GitHub sayfasında ve raporun ekler bölümünde belirtilmiştir.

## Projeyi Klonlayın

Öncelikle projeyi kendi bilgisayarınıza klonlayın. Git yüklü değilse, [Git'i buradan](https://git-scm.com/) indirip kurabilirsiniz.
```bash
git clone https://github.com/kullaniciadi/proje-adi.git
cd proje-adi
```
>Bu adım, proje dosyalarını yerel makinenize indirir ve proje dizinine geçiş yapar.

## JDK Kurulumu

Projeyi çalıştırmak için **JDK 23** sürümünün bilgisayarınızda yüklü olması gerekir. Aşağıdaki adımları izleyerek JDK kurulumunu tamamlayın:

1. [JDK 23'ü buradan](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html) indirin ve indirilen dosyayı çalıştırarak ekrandaki talimatları izleyin.
2. Kurulum tamamlandıktan sonra, komut satırında `java -version` komutunu kullanarak doğru yüklendiğini doğrulayın.

```bash
java -version
```

>Bu adım, JDK'nın doğru şekilde yüklendiğini kontrol etmek için önemlidir.

Çıktı olarak **JDK 23** sürümünü görmelisiniz.

## Kütüphane Yüklemeleri

Projede kullanılan kütüphaneleri yüklemek için, aşağıdaki adımları izleyerek her birini doğru şekilde kurduğunuzdan emin olun:

### 1. Selenium

Selenium, web tarayıcılarını otomatikleştirmek için kullanılan güçlü bir araçtır. Bu projede **Selenium 4.25.0** sürümü kullanılmıştır. Selenium'u projenize manuel olarak eklemek için aşağıdaki adımları izleyebilirsiniz.

#### 1.1. Selenium JAR Dosyalarını İndirin

Selenium'u projeye eklemek için, aşağıdaki adımları izleyerek gerekli JAR dosyalarını indirin:

- **Selenium'un En Son Sürümünü İndirin**:
   - Selenium'u [Selenium İndirme Sayfası](https://www.selenium.dev/downloads/) üzerinden indirebilirsiniz.
   - İndirdiğiniz dosya bir **ZIP** dosyası olacaktır. Bu dosyayı çıkardıktan sonra içerisindeki **JAR dosyalarını** projeye eklemeniz gerekecek.

#### 1.2. JAR Dosyalarını Projeye Ekleyin

- **Eclipse Kullanıyorsanız:**
   - Projeyi sağ tıklayın, **Build Path** > **Configure Build Path** seçeneklerini tıklayın.
   - **Libraries** sekmesine gidin ve **Add External JARs** butonuna tıklayın.
   - İndirdiğiniz Selenium JAR dosyasını ve içindeki `libs` klasöründeki tüm bağımlılıkları seçerek projeye ekleyin.

- **IntelliJ IDEA Kullanıyorsanız:**
   - Projeyi sağ tıklayın, **Add Framework Support** > **Java** > **Library** seçeneğini tıklayın.
   - Açılan pencerede **+** butonuna tıklayarak, Selenium JAR dosyasını ve `libs` klasöründeki tüm bağımlılıkları ekleyin.

### 2. OpenCV

OpenCV, görüntü işleme işlemleri için kullanılır. Projede **OpenCV 4.10.0** sürümü kullanılmıştır. OpenCV'yi manuel olarak yüklemek için aşağıdaki adımları izleyebilirsiniz.

#### 2.1. OpenCV JAR Dosyasını İndirin

OpenCV'yi indirmeniz için şu adrese gidin ve uygun sürümü indirin:  
[OpenCV İndir](https://opencv.org/releases/)

İndirdiğiniz dosya genellikle `.zip` formatında olacaktır.

#### 2.2. JAR Dosyasını Projeye Ekleyin

1. İndirdiğiniz `.zip` dosyasını çıkarın.
2. `opencv-4.x.x.jar` dosyasını bulun.
3. Bu JAR dosyasını projeye eklemek için:
    - Eğer **Eclipse** kullanıyorsanız:
      - Proje sağ tıklayın, **Build Path** > **Configure Build Path** seçeneklerini tıklayın.
      - **Libraries** sekmesine gidin ve **Add External JARs** butonuna tıklayın.
      - İndirdiğiniz `opencv-4.x.x.jar` dosyasını seçin ve ekleyin.
    - Eğer **IntelliJ IDEA** kullanıyorsanız:
      - Proje sağ tıklayın, **Add Framework Support** > **Java** > **Library** seçeneğini tıklayın.
      - Açılan pencerede **+** butonuna tıklayarak `opencv-4.x.x.jar` dosyasını ekleyin.

#### 2.3. OpenCV Kütüphanesini Yüklemek

OpenCV'nin doğru şekilde çalışabilmesi için bazı sistem dosyalarını (native library) da eklemeniz gerekecek. Bunları, OpenCV'nin `.dll` (Windows için) veya `.so` (Linux için) dosyalarıdır.

- **Windows** kullanıcıları, `opencv/build/java/x64/opencv_java460.dll` dosyasını bulup projenize eklemeli ve sistem path'ine dahil etmelidirler.

Eğer her şey doğru bir şekilde eklenirse, projenizde OpenCV'yi kullanmaya başlayabilirsiniz.
### 3. JSON.org (JSONObject)

Bu projede JSON verilerini işlemek için **JSON.org** kütüphanesi kullanılmaktadır. Aşağıda, **JSONObject** kütüphanesini projenize manuel olarak nasıl ekleyebileceğinizi adım adım bulabilirsiniz.

#### 3.1. JSON.org JAR Dosyasını İndirin

JSON.org kütüphanesini projeye eklemek için aşağıdaki adımları izleyerek gerekli JAR dosyasını indirin:

1. **JSON.org Kütüphanesini İndirin**:
   - JSON.org kütüphanesinin en son sürümünü [JSON.org İndirme Sayfası](https://mvnrepository.com/artifact/org.json/json) üzerinden indirebilirsiniz.
   - İndirdiğiniz dosya bir **JAR dosyası** olacaktır. Bu dosyayı çıkardıktan sonra projenize eklemeniz gerekecek.

#### 3.2. JAR Dosyasını Projeye Ekleyin

- **Eclipse Kullanıyorsanız**:
   - Projeyi sağ tıklayın, **Build Path** > **Configure Build Path** seçeneklerini tıklayın.
   - **Libraries** sekmesine gidin ve **Add External JARs** butonuna tıklayın.
   - İndirdiğiniz `json.jar` dosyasını projeye ekleyin.

- **IntelliJ IDEA Kullanıyorsanız**:
   - Projeyi sağ tıklayın, **Add Framework Support** > **Java** > **Library** seçeneğini tıklayın.
   - Açılan pencerede **+** butonuna tıklayarak, indirdiğiniz `json.jar` dosyasını projeye ekleyin.

## Projeyi Çalıştırma

Yukarıdaki kurulumu başarıyla tamamladıysanız projeyi çalıştırabilirsiniz.

### GoogleImageScraper Sınıfı İle Görsel İndirme

Google Görseller'den görsel indirmek için şu adımları izleyin:

1. **İndirme yolunu ve görsel sayısını belirleyin**:
   - Görsellerin kaydedileceği yeri tanımlayın.
   - İndirilmesini istediğiniz görsel sayısını belirleyin. Tüm görselleri indirmek isterseniz, `imageCount` değerini -1 olarak ayarlayabilirsiniz.

2. **GoogleImageScraper örneğini oluşturun ve `downloadImages` metodunu çağırın**:
   - İndirmek istediğiniz görsel arama terimini parametre olarak verin.

#### Örnek: Kedilerden 10 Görsel İndirme
```java
GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", 10, 5000, new Counter(), BrowserType.CHROME);
scraper.downloadImages("cats");
```
#### Örnek: Köpek Sorgusundaki Tüm Görselleri İndirme
```java
// Eğer `imageCount` değerini -1 olarak ayarlarsanız, scraper arama sonuçları sayfasındaki tüm mevcut görselleri indirir.
GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", -1, 3000, new Counter(), BrowserType.FIREFOX);
scraper.downloadImages("dogs");
scraper.quit();  // Tarayıcıyı scraping işlemi bitiminde kapatın
```
#### Örnek: Enum yerine WebDriver örneği oluşturarak kazıam yapmak
```java
System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); 
WebDriver driver = new ChromeDriver();
GoogleImageScraper scraper = new GoogleImageScraper("C:\\path\\to\\download", -1, 3000, new Counter(), driver);
scraper.downloadImages("dogs");
scraper.quit();
```
#### Örnek: Ana Sınıfta Kullanabileceğiniz Dinamik Bir Kazıma Metodu
```java
public static void scrap() {
Scanner scanner = new Scanner(System.in);
System.out.print("Enter search term: ");
String searchTerm = scanner.nextLine();

String downloadPath = "C:\\Users\\umutg\\OneDrive\\Belgeler\\Yazilim-Lab-Proje\\Yedekler\\Normal+"; // Change this to your preferred directory

System.out.print("Enter number of images to download: ");
int imageCount = scanner.nextInt();

System.out.print("Enter wait time in milliseconds: ");
int waitTime = scanner.nextInt();
scanner.nextLine(); // Tüketilen yeni satırı temizle

System.out.print("Enter browser type (CHROME / FIREFOX): ");
String browserInput = scanner.nextLine().toUpperCase();
BrowserType browserType = BrowserType.valueOf(browserInput);
        
     
Counter counter = new Counter();

// get start id
System.out.print("Start Id:");
int id = scanner.nextInt();
counter.setImageNameIdentifierCount(id);
scanner.close();
   
ImageScraper scraper = new GoogleImageScraper(downloadPath, imageCount, waitTime,counter, browserType);
scraper.downloadImages(searchTerm);
scraper.quit();
}
```
### MedpixImageScraper

#### Örnek: 10 Tane Beyin Tümörü Görseli İndirme
```java
WebDriver driver = new ChromeDriver();

// İndirme yolu, görsel sayısı, bekleme süresi ve sayfa arayüzünü ayarlayın
String downloadPath = "C:\\path\\to\\download"; // Görsellerin kaydedileceği dizin
int imageCount = 10; // İndirilmesi gereken görsel sayısı
int waitTime = 3000; // Her işlem arasında bekleme süresi (milisaniye cinsinden)
Counter counter = new Counter(); // Görsel sayacını başlat

// MedpixImageScraper sınıfı örneğini oluşturun
MedpixImageScraper scraper = new MedpixImageScraper(downloadPath, imageCount, waitTime, counter, driver);

// Arama terimi belirleyin ve görselleri indirin
String searchTerm = "brain tumor"; // Arama yapmak istediğiniz terim
scraper.downloadImages(searchTerm);
```

### OpeniImageScraper
#### Örnek: 10 Tane Meningioma Tümörü Görseli İndirme
```java
WebDriver driver = new ChromeDriver();

// İndirme yolu, görsel sayısı, bekleme süresi ve sayfa arayüzünü ayarlayın
String downloadPath = "C:\\path\\to\\download"; // Görsellerin kaydedileceği dizin
int imageCount = 10; // İndirilmesi gereken görsel sayısı
int waitTime = 3000; // Her işlem arasında bekleme süresi (milisaniye cinsinden)
Counter counter = new Counter(); // Görsel sayacını başlat

// OpeniWebScraper sınıfı örneğini oluşturun
OpeniWebScraper scraper = new OpeniWebScraper(downloadPath, imageCount, waitTime, counter, driver);

// Arama terimi belirleyin ve görselleri indirin
String searchTerm = "meningioma brain tumor ct"; // Arama yapmak istediğiniz terim
scraper.downloadImages(searchTerm);
```

### Counter Sınıfı Kullanımı
```java
// Counter nesnesini oluştur
Counter counter = new Counter();
counter.setImageNameIdentifierCount(105); // Bir dahaki resmi kaydetmeye 105. id ile başlayacak
counter.reset(); // Ana sınıfda birden fazla kazıma kodunuz varsa ve arada sayaçları sıfırlamak için kullanabilirsiniz.
counter.summarizeResults() // Hata sayısı , indirme sayısı , istek sayısı gibi verilerle çıktı oluşturur.
```

### İndirilen Resmin İsmini ve Uzantısını Değiştirme
Resmin indireleceği ismi uzantısını değiştirmek için **BaseImageScraper** sınıfındaki aşağıdaki alanı kendinize göre ayarlayabilirsiniz.

```java
protected static final String IMAGE_FILENAME_PREFIX = "glioma";
protected static final String IMAGE_EXTENSION = ".jpg";
```

### İndirilen Resimlerden Aynı Olanların Ayıklanması
```java
public static void removeDuplicateImages() {
String folderPath = "C:\\Users\\umutg\\OneDrive\\Belgeler\\YazLabSonVeriler\\PituitaryTumor";
ImageHelper.removeDuplicateImages(folderPath);
}
```
### Bir Resmi Gri Tonlamaya Çevirme
```java
Mat grayImage = imageHelper.toGrayscale(image);
imageHelper.saveImage(grayImage, folderPath);
```
### Bir Resmi Yansıtma
```java
Mat flippedImage = imageHelper.flipImage(image, 1); // Yatay flip
imageHelper.saveImage(flippedImage, folderPath);
```

### Bir Resmi Döndürme

```java
Mat rotatedImage = imageHelper.rotateImage(image, 90); // 90 derece döndür
imageHelper.saveImage(rotatedImage, folderPath);
```

### Parlaklık ve Kontrast Ayarlama

```java
Mat newImage = imageHelper.adjustContrastAndBrightness(image,1.2,50 ); //Resmin kontrastı 1.2 artırılacak ve parlaklık da 30 birim artırılacaktır.
imageHelper.saveImage(rotatedImage, folderPath);
```

>**Ana  Sınıfta Bazı Örnek Kodlar Hazır Olarak Verilmiştir
>scrap() metodunu kullanarak Google Görseller'den resim indirmeyi başlatabilirsiniz.
>Resimleri işlemek için, toGrayScale(), augmentImages() veya resizeImages() metodlarını kullanabilirsiniz.
>removeDuplicateImages() ile tekrarlanan resimleri silebilirsiniz**


## Ekler
- JDK indirme linki: [JDK](https://www.oracle.com/tr/java/technologies/downloads/)
- OpenCV indirme linki: [OpenCV](https://opencv.org/releases/)
- Selenium indirme linki: [Selenium](https://www.selenium.dev/downloads/)
- Elde Edilen Verilere İlişkin Tablo : [Excel](https://docs.google.com/spreadsheets/d/113_H-TnYGTuDJVdzqkttt-iReyHAHpU2B6Hdr_E7OhI/edit?gid=0#gid=0)
- Proje Raporu: [Rapor](https://github.com/user-attachments/files/17682725/yazlab-rapor-sonhal.docx)
- Proje Kapsamında Toplanan Veriler: [Veri Seti](https://drive.google.com/file/d/1mcfup7GPS_elTCEC0PZKnvi5YM5BD2Kr/view?usp=sharing)

## Teşekkürler
- [Selenium Documentation](https://www.selenium.dev/documentation/en/)
- [Medium](https://medium.com/@dian.octaviani/method-1-4-automation-of-google-image-scraping-using-selenium-3972ea3aa248)
- [GeeksforGeeks](https://www.geeksforgeeks.org/rotating-images-using-opencv-in-java/)
- [W3docs](https://www.w3docs.com/snippets/java/getting-a-files-md5-checksum-in-java.html)
