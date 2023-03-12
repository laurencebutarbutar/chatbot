db.createUser(
    {
        user: "laurence",
        pwd: "mongo1234",
        roles: [
            {
                role: "readWrite",
                db: "chatbot"
            }
        ]
    }
);

db.createCollection('user');
db.getCollection('user').insertOne({username: 'laurence', password: '$2a$10$GBk.T8fAdRxdVYbaRnVqTuZhZ9yo1/q3J8UEXBbfSW2qXlw/ZYh4m'});

db.createCollection('template');
db.getCollection('template').insertOne({state: 'BAD_REQUEST', message: 'Mohon maaf, instruksi yang Anda masukkan tidak dikenali.'});
db.getCollection('template').insertOne({state: 'CHOOSE_MENU', message: 'Hi %s,\n' +
        'Silakan pilih menu:\n' +
        '1. Buat Pembayaran\n' +
        '2. Check Pembayaran\n' +
        '\n' +
        'Mohon membalas pesan ini hanya dengan angka.\n' +
        'Contoh: "1" tanpa tanda kutip.'});
db.getCollection('template').insertOne({state: 'INPUT_CUSTOMER_EMAIL', message: 'Silahkan masukkan email customer yang ingin anda tagihkan.'});
db.getCollection('template').insertOne({state: 'INPUT_AMOUNT', message: 'Silakan masukkan nominal yang ingin Anda tagihkan.'});
db.getCollection('template').insertOne({state: 'FINALIZE', message: 'Pembuatan transaksi dengan nomor %s telah berhasil.'});
db.getCollection('template').insertOne({state: 'CHECK_STATUS', message: 'Berikut 5 order status sukses terakhir pada hari ini : %s'});

db.createCollection('transaction');