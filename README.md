# Toplumsal Sorun Bildirim Sistemi

Spring Boot, Spring Security, Spring Data JPA ve Thymeleaf ile hazirlanmis katmanli mimariye sahip web projesi.

## Ozellikler

- USER ve ADMIN rolleri ile oturum yonetimi
- Sorun bildirimlerinde BLOB olarak veritabaninda saklanan fotograf
- Kullanici icin sorun ekleme, listeleme, guncelleme ve silme
- Admin icin tum sorunlari listeleme, onaylama, reddetme ve silme
- Onaylanan sorunlar icin kullanici puan sistemi
- Baslik veya kategoriye gore dinamik arama
- Form validation ve Thymeleaf hata mesajlari
- Ranking sayfasi

## Demo Kullanicilar

- Admin: `admin` / `admin123`
- Kullanici: `user` / `user123`

## Calistirma

Bu proje Java 8 uyumlu Spring Boot 2.7 uzerine kuruludur.

Makinede Maven kuruluysa:

```bash
mvn spring-boot:run
```

Uygulama acildiginda:

- Web arayuzu: `http://localhost:8080`
- H2 konsolu: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:toplumsaldb`
