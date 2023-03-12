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

db.createCollection('email');
db.getCollection('email').insertOne({status: 'SUCCESS', template: '<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Transaction</title></head><body><div><div><div style="display:flex"><h2 style="margin:0;color:#2c333c">Transaction</h2></div><div style="background-color:#e6e6e6;height:1px;width:100%;margin:16px 0"></div><p style="margin:0 0 16px 0;color:#4d4d4d">Dear customer, your transaction is<span style="color:#0aa59f;font-weight:400"> success.</span></p><div style="margin-bottom:16px;color:#2c333c"><div style="color:#2c333c">Thanks for using this apps</div></div></div></div></body></html>'});