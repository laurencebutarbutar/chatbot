db.createUser(
    {
        user: "laurence",
        pwd: "laurence",
        roles: [
            {
                role: "admin",
                db: "chatbot"
            }
        ]
    }
);

db.createCollection('user');
db.getCollection('user').insertOne({username: 'laurence', password: '$2a$10$GBk.T8fAdRxdVYbaRnVqTuZhZ9yo1/q3J8UEXBbfSW2qXlw/ZYh4m'});