import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageHelper {
    public ImageHelper() {
    	//OpenCV'yi Java ile kullanmak için bu özel yükleme yöntemini kullanmanız gerekir, çünkü kütüphane yerel bir kütüphane olarak derlenmiştir
    	//ve Java'nın çalışma zamanı ortamında doğrudan erişilmesi gerekmez. Bu, Java ve C/C++ arasında bir köprü işlevi gören bir mekanizmadır.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Gri tonlama (grayscale) işlemi
    public Mat toGrayscale(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        return grayImage;
    }

    // Boyutlandırma işlemi
    public Mat resizeImage(Mat image, int width, int height) {
        Mat resizedImage = new Mat();
        Size size = new Size(width, height);
        Imgproc.resize(image, resizedImage, size);
        return resizedImage;
    }

    // Resmi flip (yatay veya dikey) yapma
    public Mat flipImage(Mat image, int flipCode) {
        Mat flippedImage = new Mat();
        Core.flip(image, flippedImage, flipCode);
        return flippedImage;
    }

    // Resmi döndürme
    public Mat rotateImage(Mat image, int angle) {
        Mat rotatedImage = new Mat();
        Point center = new Point(image.cols() / 2, image.rows() / 2); // resmi döndüreceğimiz noktayı belirtmek için ikiye böldük ve orta noktayı bulduk
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        Imgproc.warpAffine(image, rotatedImage, rotationMatrix, image.size());
        return rotatedImage;
    }

    // Kontrast ve parlaklık ayarlama
    public Mat adjustContrastAndBrightness(Mat image, double alpha, int beta) {
        Mat adjustedImage = new Mat();
        image.convertTo(adjustedImage, -1, alpha, beta); // alpha = kontrast, beta = parlaklık
        return adjustedImage;
    }

    // Resmi dosyaya kaydetme
    public void saveImage(Mat image, String filePath) {
        Imgcodecs.imwrite(filePath, image);
    }
    
    public static void renameImagesInFolder(String folderPath, String newName) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.out.println("Geçersiz klasör yolu: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|bmp|gif)$"));
        if (files == null || files.length == 0) {
            System.out.println("Klasörde görüntü dosyası bulunamadı.");
            return;
        }

        int counter = 1;
        for (File file : files) {
            String fileExtension = getFileExtension(file.getName());
            String newFileName = newName + counter + "." + fileExtension;
            Path newFilePath = file.toPath().resolveSibling(newFileName);

            try {
                Files.move(file.toPath(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(file.getName() + " başarıyla " + newFileName + " olarak yeniden adlandırıldı.");
            } catch (IOException e) {
                System.out.println("Dosya yeniden adlandırılamadı: " + file.getName());
                e.printStackTrace();
            }

            counter++;
        }
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    
    public static void removeDuplicateImages(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.out.println("Geçerli bir klasör yolu girin.");
            return;
        }

        Map<String, File> imageHashes = new HashMap<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")); // Yalnızca resim dosyalarını filtrele

        if (files != null) {
            for (File file : files) {
                try {
                    String hash = getFileContentHash(file);

                    if (!imageHashes.containsKey(hash)) {
                        imageHashes.put(hash, file); // İlk kez karşılaşılan dosyayı ekle
                    } else {
                        File duplicate = imageHashes.get(hash);
                        System.out.println("Aynı içerikte dosya bulundu: " + file.getName() + " == " + duplicate.getName());
                        
                        boolean deleted = file.delete(); // Aynı içerikli dosya varsa sil
                        if (deleted) {
                            System.out.println("Silindi: " + file.getName());
                        } else {
                            System.out.println("Silinemedi: " + file.getName());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Dosya okunamadı: " + file.getName());
                }
            }
        } else {
            System.out.println("Klasörde resim dosyası bulunamadı.");
        }
    }

    // Dosyanın içerik hash'ini hesapla
    private static String getFileContentHash(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fileContent);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IOException("Hash hesaplanırken hata oluştu", e);
        }
    }
}
