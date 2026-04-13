# 💰 FinTrack - Personal Finance Tracker API

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-green?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql" />
  <img src="https://img.shields.io/badge/Redis-7-red?style=for-the-badge&logo=redis" />
</p>

## 📖 Hakkında

FinTrack, kişisel finans yönetimi için geliştirilmiş modern bir REST API'dir. Gelir ve giderlerinizi takip edin, bütçe belirleyin ve finansal durumunuzu analiz edin.

## ✨ Özellikler

- 🔐 **JWT Authentication** - Güvenli kullanıcı kimlik doğrulama
- 💸 **Gelir/Gider Takibi** - İşlemlerinizi kategorilere göre kaydedin
- 📊 **Bütçe Yönetimi** - Aylık bütçe limitleri belirleyin
- 🏷️ **Kategori Sistemi** - Özelleştirilebilir kategoriler
- 📈 **Raporlama** - Aylık/yıllık finansal raporlar
- ⚡ **Redis Cache** - Hızlı performans için önbellekleme
- 🛡️ **Rate Limiting** - API güvenliği için istek sınırlandırma
- 📚 **Swagger UI** - İnteraktif API dokümantasyonu

## 🏗️ Teknoloji Stack

| Teknoloji | Versiyon | Açıklama |
|-----------|----------|----------|
| Java | 21 | Programlama dili |
| Spring Boot | 4.0.5 | Web framework |
| Spring Security | 6.x | Güvenlik |
| Spring Data JPA | 3.x | ORM |
| PostgreSQL | 16 | Veritabanı |
| Redis | 7 | Önbellekleme |
| JWT | 0.12.3 | Token tabanlı auth |
| Swagger/OpenAPI | 3.0 | API dokümantasyonu |
| Lombok | - | Boilerplate azaltma |
| Bucket4j | 8.7.0 | Rate limiting |

## 🚀 Kurulum

### Gereksinimler

- Java 21+
- Maven 3.9+
- PostgreSQL 16+
- Redis 7+

### Yerel Geliştirme

1. **Repoyu klonlayın**
```bash
git clone https://github.com/yasinakciljvt/fintrack-backend.git
cd fintrack-backend
```

2. **Veritabanı oluşturun**
```sql
CREATE DATABASE fintrack;
CREATE USER fintrack_user WITH PASSWORD 'fintrack_pass';
GRANT ALL PRIVILEGES ON DATABASE fintrack TO fintrack_user;
```

3. **Environment variables ayarlayın**
```bash
export DB_URL=jdbc:postgresql://localhost:5432/fintrack
export DB_USER=fintrack_user
export DB_PASS=fintrack_pass
export JWT_SECRET=your-super-secret-key-min-32-characters
export SPRING_DATA_REDIS_HOST=localhost
export SPRING_DATA_REDIS_PORT=6379
```

4. **Uygulamayı çalıştırın**
```bash
./mvnw spring-boot:run
```

### Docker ile Çalıştırma

```bash
docker-compose up -d
```

## 📡 API Endpoints

### Auth
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/auth/register` | Yeni kullanıcı kaydı |
| POST | `/api/auth/login` | Kullanıcı girişi |

### Transactions
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/transactions` | Tüm işlemleri listele |
| POST | `/api/transactions` | Yeni işlem ekle |
| PUT | `/api/transactions/{id}` | İşlem güncelle |
| DELETE | `/api/transactions/{id}` | İşlem sil |

### Categories
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/categories` | Kategorileri listele |
| POST | `/api/categories` | Yeni kategori ekle |
| PUT | `/api/categories/{id}` | Kategori güncelle |
| DELETE | `/api/categories/{id}` | Kategori sil |

### Budgets
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/budgets` | Bütçeleri listele |
| POST | `/api/budgets` | Yeni bütçe oluştur |
| PUT | `/api/budgets/{id}` | Bütçe güncelle |
| DELETE | `/api/budgets/{id}` | Bütçe sil |

### Reports
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/reports/monthly` | Aylık rapor |
| GET | `/api/reports/category-summary` | Kategori özeti |

## 📖 API Dokümantasyonu

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🌐 Canlı Demo

- **Backend API**: https://fintrack-backend-production-9991.up.railway.app
- **Swagger UI**: https://fintrack-backend-production-9991.up.railway.app/swagger-ui.html
- **Frontend**: https://fintrack-frontend-seven.vercel.app

## 🔧 Environment Variables

| Değişken | Açıklama | Varsayılan |
|----------|----------|------------|
| `DB_URL` | PostgreSQL bağlantı URL'i | `jdbc:postgresql://localhost:5432/fintrack` |
| `DB_USER` | Veritabanı kullanıcısı | `fintrack_user` |
| `DB_PASS` | Veritabanı şifresi | `fintrack_pass` |
| `JWT_SECRET` | JWT imzalama anahtarı | - |
| `SPRING_DATA_REDIS_HOST` | Redis sunucu adresi | `localhost` |
| `SPRING_DATA_REDIS_PORT` | Redis portu | `6379` |
| `SPRING_DATA_REDIS_USERNAME` | Redis kullanıcı | - |
| `SPRING_DATA_REDIS_PASSWORD` | Redis şifre | - |
| `APP_CORS_ORIGINS` | İzin verilen originler | `http://localhost:5173` |
| `PORT` | Sunucu portu | `8080` |

## 📁 Proje Yapısı

```
src/main/java/com/jxt/fintrack/
├── config/          # Konfigürasyon sınıfları
├── controller/      # REST Controller'lar
├── dto/             # Data Transfer Objects
│   ├── request/     # İstek DTO'ları
│   └── response/    # Yanıt DTO'ları
├── entity/          # JPA Entity'leri
├── exception/       # Exception handling
├── repository/      # JPA Repository'leri
├── scheduler/       # Zamanlanmış görevler
├── security/        # Security config ve JWT
└── service/         # Business logic
```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 👤 Geliştirici

**Yasin Akçil**
- GitHub: [@yasinakciljvt](https://github.com/yasinakciljvt)

---

<p align="center">
  ⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!
</p>

