# chatbot
Simulator Mini Chatbot adalah sebuah aplikasi yang digunakan untuk membalas chat secara otomatis. 
Beberapa fitur Simulator Mini Chatbot ini antara lain :
1. Create Transaksi,
2. Send Email apabila Transaksi Sukses,
3. User Token, dan
4. Promo Transaksi.

Teknologi yang digunakan antara lain :
1. Java (Spring boot),
2. MongoDb,
3. Elasticsearch,
4. Kafka,
5. Docker, dan
6. Redis.

Configuration :
1. Apabila ingin menggunakan fitur send email, anda dapat memasukan username dan password gmail anda pada application.properties. 
Seperti pada parameter berikut :
spring-mail.sender.username=
spring-mail.sender.password=

2. Jalankan docker-compose up --build pada root aplikasi, untuk mendownload dan menjalankan image dan container pada docker.

3. Jalankan aplikasi java chatbot dengan mvn spring-boot:run atau melalui jar.

4. Buka link swagger, agar memudahkan untuk mencoba aplikasi http://localhost:8080/chatbot/v1/swagger-ui/

5. Buka user-controller dan jalankan login untuk mendapatkan token yang berlaku selama 1 jam atau dapat di configuration pada application properties dalam satuan jam.
{
  "password": "Pass1234",
  "username": "laurence"
}

6. Ambil response token dan masukan pada gembok pada swagger, dengan menambahkan prefix "Bearer ".

7. Untuk melihat dan mengggunakan promo anda harus menjalankan addPromoToElastic terlebih dahulu, API ini tidak membutuhkan token.
Hal ini dikarenakan untuk melihat atau menggunakan promo, aplikasi menggunakan Elasticsearch, sedangkan API ini digunakan untuk mengupload data dari
mongodb ke elasticsearch.

8. Promo dipengaruhi oleh beberapa hal, antara lain tanggal aktif dan selesai promo, minimum pembelian, maksimum potongan, penggunaan metode pembayaran,
dll.

9. Pada fitur chatbot anda dapat membuat dan melihat transaksi yang telah dibuat.

10. Terdapat simulator transaksi untuk mengupdate status transaksi sekaligus mengirim email kepada customer email yang telah dibuat.

## Noted
Pada aplikasi ini belum menggunakan integration/service/unit testing.
