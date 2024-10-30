import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class ImageDuplicateRemover {


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
