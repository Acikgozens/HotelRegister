package tr.edu.medipol.ilerijava.mezuniyet.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.medipol.ilerijava.mezuniyet.data.MusteriDTO;
import tr.edu.medipol.ilerijava.mezuniyet.entity.Musteri;
import tr.edu.medipol.ilerijava.mezuniyet.service.MusteriService;


import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class OtelMusteriController {

    Logger logger = LoggerFactory.getLogger(OtelMusteriController.class);
    @Autowired
    MusteriService musteriService;

    @GetMapping("/bilgiler")
    public String bilgi(){
        return "HR210027-ENES EFE ACIKGOZ";
    }

    @PostMapping("/musteri-ekle")
    public ResponseEntity<Object> musteriEkle(@RequestBody @Valid MusteriDTO musteriDTO) {
        logger.info("Ekleme servisi çağırıldı !");
        Musteri musteri;

        LocalDate birthDate = musteriDTO.getDogumTarihi();
        LocalDate today = LocalDate.now();
        LocalDate eighteenYearsAgo = today.minusYears(18);

        try {
            try {
                Long.parseLong(musteriDTO.getTcKimlikNo());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("Tc Kimlik Numarası Sadece Rakam İçermelidir");
            }
            if (musteriDTO.getTcKimlikNo().length() != 11) {
                throw new Exception("Tc Kimlik Numarası Boş Geçilemez ve 11 karakterli olmalıdır !");
            }
            int toplam;
            int[] hane;
            try {
                String tcKimlikNo = musteriDTO.getTcKimlikNo();
                hane = new int[11];
                toplam = 0;
                for (int i = 0; i < 11; i++) { // DÖNGÜ İLE TEK TEK RAKAMLARI DİZİYE DİZİYE ATIYORUZ.
                    hane[i] = Integer.parseInt(String.valueOf(musteriDTO.getTcKimlikNo().charAt(i))); // DİZİYE ATMIŞ OLDUĞUMUZ SAYILARI INTEGER A DÖNÜŞTÜRME İŞLEMİ YAPIYORUZ.
                    toplam += hane[i]; // TOPLAM ADLI DEĞİŞKENE RAKAMLARIN TOPLAMINI GÖNDERİYORUZ.
                }
                toplam -= hane[10]; //DİZİLERDE SIRALAMA 0DAN BAŞLADIĞI İÇİN 10. HANEYİ YANİ TC NOYA GÖRE 11. HANEYİ TOPLAM DEĞİŞKENİNDEN ÇIKARTIYORUZ.
            } catch (StringIndexOutOfBoundsException e) {
                return ResponseEntity.status(400).body("Tc Kimlik Numarası Geçersiz");
            }
            if ((toplam % 10) != hane[10]) {
                // TC KİMLİK NUMARANIZIN İLK 10 BASAMAMAĞININ TOPLAMININ 10'A BÖLÜMÜNDEN KALAN 11. BASAMAĞI VERİR. KENDİ NUMARANIZDA DENEYEBİLİRSİNİZ.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (((hane[0] + hane[2] + hane[4] + hane[6] + hane[8]) * 7 + (hane[1] + hane[3] + hane[5] + hane[7]) * 9) % 10 != hane[9]) {
                // (1-3-5-7-9. BASAMAKLARIN TOPLAMININ 7 İLE ÇARPIMI) + (2-4-6-8. BASAMAKLARIN TOPLAMININ 9 İLE ÇARPIMINDAN 10 BÖLÜMÜNÜN KALANI 10. BASAMAĞI VERİR.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (((hane[0] + hane[2] + hane[4] + hane[6] + hane[8]) * 8) % 10 != hane[10]) {
                // 1-3-5-7-9. BASAMAKLARIN TOPLAMI 8 İLE ÇARPILIP 10'A BÖLÜNÜMÜNDEN KALAN 11. BASAMAĞI VERİR.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (!musteriDTO.getTcKimlikNo().matches("\\d+")) {
                throw new Exception("Tc Kimlik Numarası Sadece Sayılar İçermelidir");
            }
            if (musteriDTO.getAd().length() < 2 || musteriDTO.getAd().length() > 30) {
                throw new Exception("Adın uzunluğu 2 ile 30 karakter arası olmalıdır !");
            }
            if (!musteriDTO.getAd().matches("[a-zA-Z.]+")) {
                throw new Exception("Ad sadece harf içermelidir !");
            }
            if (musteriDTO.getSoyad().length() < 2 || musteriDTO.getSoyad().length() > 30) {
                throw new Exception("Soyadın uzunluğu 2 ile 30 karakter arası olmalıdır !");
            }
            if (!musteriDTO.getSoyad().matches("[a-zA-Z.]+") && !musteriDTO.getSoyad().contains(".")) {
                throw new Exception("Soyad sadece harf ve nokta içermelidir !");
            }
            if (musteriDTO.getGirisTarihi() == null) {
                throw new Exception("Giriş Tarihi Girilmelidir");
            }
            if (musteriDTO.getDogumTarihi() == null) {
                throw new Exception("Dogum Tarihi Girilmelidir");
            }
            if (birthDate.isAfter(eighteenYearsAgo)) {
                throw new Exception("Yaş 18'den büyük olmalıdır");
            }
            try {
                Long.parseLong(musteriDTO.getTelefon());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("Telefon Numarası Sadece Rakam İçermelidir");
            }
            if (musteriDTO.getTelefon().length() != 11) {
                throw new Exception("Telefon numarası boş geçilemez ve 11 karakterli olmalı !");
            }
            if (!musteriDTO.getTelefon().matches("\\d+")) {
                throw new Exception("Telefon Numarası Sadece Sayılar İçermelidir");
            }
            if (!musteriDTO.getEposta().matches("^(.+)@(.+)\\.(com|com\\.tr|net|org|gov|edu)$") && musteriDTO.getEposta().length() <= 35) {
                throw new Exception("Geçersiz e-posta adresi !");
            }
            musteri = musteriService.musteriEkle(musteriDTO);
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Kişi eklenemedi! Hata: " + e.getMessage());
        }

        return ResponseEntity.ok(musteri);
    }
    @GetMapping("/musteri-listele")
    public List<Musteri> musterileriListele(){
        logger.info("Listeleme servisi çağırıldı !");
        List<Musteri> musteriListesi = musteriService.musterileriListele();
        return musteriListesi;
    }

    @GetMapping("/musteri-getir/{id}")
    public Musteri musteriGetir(Long id){
        logger.info("Getirme servisi çağırıldı !");
        return musteriService.musteriGetir(id);
    }

    @DeleteMapping("/musteri-sil/{id}")
    public String musteriSil(@PathVariable Long id){
        logger.info("Silme servisi çağırıldı !");
        return musteriService.musteriSil(id);
    }

    @PutMapping("/musteri-guncelle/{id}")
    public ResponseEntity<Object>  musteriGuncelle(@PathVariable Long id, @RequestBody @Valid MusteriDTO musteriDTO){
        logger.info("Güncelleme servisi çağırıldı !");
        Musteri musteri;

        LocalDate birthDate = musteriDTO.getDogumTarihi();
        LocalDate today = LocalDate.now();
        LocalDate eighteenYearsAgo = today.minusYears(18);

        try {
            if (id<=0){
                throw new Exception("İd 0 veya 0 dan küçük girilemez");
            }
            try {
                Long.parseLong(musteriDTO.getTcKimlikNo());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("Tc Kimlik Numarası Sadece Rakam İçermelidir");
            }
            if (musteriDTO.getTcKimlikNo().length() != 11) {
                throw new Exception("Tc Kimlik Numarası Boş Geçilemez ve 11 karakterli olmalıdır !");
            }
            int toplam;
            int[] hane;
            try {
                String tcKimlikNo = musteriDTO.getTcKimlikNo();
                hane = new int[11];
                toplam = 0;
                for (int i = 0; i < 11; i++) { // DÖNGÜ İLE TEK TEK RAKAMLARI DİZİYE DİZİYE ATIYORUZ.
                    hane[i] = Integer.parseInt(String.valueOf(musteriDTO.getTcKimlikNo().charAt(i))); // DİZİYE ATMIŞ OLDUĞUMUZ SAYILARI INTEGER A DÖNÜŞTÜRME İŞLEMİ YAPIYORUZ.
                    toplam += hane[i]; // TOPLAM ADLI DEĞİŞKENE RAKAMLARIN TOPLAMINI GÖNDERİYORUZ.
                }
                toplam -= hane[10]; //DİZİLERDE SIRALAMA 0DAN BAŞLADIĞI İÇİN 10. HANEYİ YANİ TC NOYA GÖRE 11. HANEYİ TOPLAM DEĞİŞKENİNDEN ÇIKARTIYORUZ.
            } catch (StringIndexOutOfBoundsException e) {
                return ResponseEntity.status(400).body("Tc Kimlik Numarası Geçersiz");
            }
            if ((toplam % 10) != hane[10]) {
                // TC KİMLİK NUMARANIZIN İLK 10 BASAMAMAĞININ TOPLAMININ 10'A BÖLÜMÜNDEN KALAN 11. BASAMAĞI VERİR. KENDİ NUMARANIZDA DENEYEBİLİRSİNİZ.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (((hane[0] + hane[2] + hane[4] + hane[6] + hane[8]) * 7 + (hane[1] + hane[3] + hane[5] + hane[7]) * 9) % 10 != hane[9]) {
                // (1-3-5-7-9. BASAMAKLARIN TOPLAMININ 7 İLE ÇARPIMI) + (2-4-6-8. BASAMAKLARIN TOPLAMININ 9 İLE ÇARPIMINDAN 10 BÖLÜMÜNÜN KALANI 10. BASAMAĞI VERİR.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (((hane[0] + hane[2] + hane[4] + hane[6] + hane[8]) * 8) % 10 != hane[10]) {
                // 1-3-5-7-9. BASAMAKLARIN TOPLAMI 8 İLE ÇARPILIP 10'A BÖLÜNÜMÜNDEN KALAN 11. BASAMAĞI VERİR.
                throw new Exception("TC Kimlik Numarası Yanlış!");
            }
            if (!musteriDTO.getTcKimlikNo().matches("\\d+")) {
                throw new Exception("Tc Kimlik Numarası Sadece Sayılar İçermelidir");
            }
            if (musteriDTO.getAd().length() < 2 || musteriDTO.getAd().length() > 30) {
                throw new Exception("Adın uzunluğu 2 ile 30 karakter arası olmalıdır !");
            }
            if (!musteriDTO.getAd().matches("[a-zA-Z.]+")) {
                throw new Exception("Ad sadece harf içermelidir !");
            }
            if (musteriDTO.getSoyad().length() < 2 || musteriDTO.getSoyad().length() > 30) {
                throw new Exception("Soyadın uzunluğu 2 ile 30 karakter arası olmalıdır !");
            }
            if (!musteriDTO.getSoyad().matches("[a-zA-Z.]+") && !musteriDTO.getSoyad().contains(".")) {
                throw new Exception("Soyad sadece harf ve nokta içermelidir !");
            }
            if (musteriDTO.getGirisTarihi() == null) {
                throw new Exception("Giriş Tarihi Girilmelidir");
            }
            if (musteriDTO.getDogumTarihi() == null) {
                throw new Exception("Dogum Tarihi Girilmelidir");
            }
            if (birthDate.isAfter(eighteenYearsAgo)) {
                throw new Exception("Yaş 18'den büyük olmalıdır");
            }
            try {
                Long.parseLong(musteriDTO.getTelefon());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("Telefon Numarası Sadece Rakam İçermelidir");
            }
            if (musteriDTO.getTelefon().length() != 11) {
                throw new Exception("Telefon numarası boş geçilemez ve 11 karakterli olmalı !");
            }
            if (!musteriDTO.getTelefon().matches("\\d+")) {
                throw new Exception("Telefon Numarası Sadece Sayılar İçermelidir");
            }
            if (!musteriDTO.getEposta().matches("^(.+)@(.+)\\.(com|com\\.tr|net|org|gov|edu)$") && musteriDTO.getEposta().length() <= 35) {
                throw new Exception("Geçersiz e-posta adresi !");
            }
            musteri = musteriService.musteriGuncelle(id,musteriDTO);
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Kişi Güncellenemedi! Hata: " + e.getMessage());
        }

        return ResponseEntity.ok(musteri);
    }
}
