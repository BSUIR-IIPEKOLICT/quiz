# Гайд по подключению MongoDB к андроид-проге

1. Зарегаться на [MongoDB](https://www.mongodb.com/)
2. Создать новый проект.
3. Указать имя создаваемого проекта: ![image](docs/1.png)
4. Нажать на кнопку "Create Project": ![image](docs/2.png)
5. Нажать на кнопку "Build a Database": ![image](docs/3.png)
6. В 3 колонке (там, где free) нажать "Create": ![image](docs/4.png)
7. Пролистнуть чуть ниже, выбрать регион (любой, главное, чтоб относительно близко был расположен географически): 
![image](docs/5.png)
8. Пролистнуть ниже, если есть желание, можно поменять название кластера. Далее нажать на "Create Cluster": 
![image](docs/6.png)
9. Подождать 5-10 минут или пока не пропадет индикация создания кластера, обновить страницу. Когда кластер будет создан, 
будет такая картина: ![image](docs/7.png)
10. Нажать на кнопку "Browse Collections"
11. Нажать на кнопку "Add my own data": ![image](docs/8.png)
12. Ввести название базы данных (Database name) и название коллекции (Collection name). Обе эти штуки нужно будет потом 
использовать в проге. Нажать на кнопку "Create": ![image](docs/9.png) ![image](docs/10.png)
13. Нажать в меню слева на пункт "Network Access". Далее нажать на кнопку "Add IP Address": ![image](docs/11.png)
14. Выбрать в открывшемся окне "ADD CURRENT IP ADDRESS" или "ALLOW ACCESS FROM ANYWHERE" (лучше второе). Нажать 
"Confirm": ![image](docs/12.png)
15. Теперь перейти на вкладку Realm. Ввести название приложения (любое), выбрать кластер, к которому будет подключение. 
Нажать кнопку "Create Realm Application": ![image](docs/13.png) ![image](docs/14.png)
16. Перейти на вкладку Rules, найти в блоке Collections созданную пункте 12 коллекцию. Настройки можно оставить, как 
есть, нажать кнопку "Configure Collection": ![image](docs/15.png)
17. Далее поставить галочки, как у меня на скрине и нажать "SAVE", затем "REVIEW DRAFT & DEPLOY": ![image](docs/16.png)
18. В появившемся окне пролистать до низа и нажать "Deploy": ![image](docs/17.png)
19. Перейти на вкладку Authentication в меню слева слева ![image](docs/18.png)
в пункте "Allow users to log in anonymously" нажать на кнопку "Edit", в открывшемся окошке нажать на 
тумблер напротив "Provider Enabled" и нажать на кнопку "Save Draft" ![image](docs/19.png)
нажать на кнопку "REVIEW DRAFT & DEPLOY", пролистать до низа и нажать "Deploy"
20. Теперь перейти на вкладку с названием Realm приложения и скопировать в буфер обмена App ID: 
![image](docs/20.png)
21. В классе, работающем с базой данных (model/Database) добавить app id, название базы данных и 
коллекции (в appId вставить строчку из пункта 20, в database - название базы данных из пункта 12, 
в playerCol - название коллекции из пункта 12): ![image](docs/21.png)

> Если все сделано правильно, после работы програмы снизу во вкладке Realm в блоке, откуда копировали 
>ранее App ID будет такая картина: ![image](docs/22.png) в разделе Atlas в коллекциях можно будет лицезреть
>это: ![image](docs/23.png)

# Подключение Realm к проге в gradle-файлах
Нужно открыть через Android Studio проект с андроид-прогой. В корневом build.gradle файл проекта добавить эту 
строчку: ![image](docs/24.png)
после чего нажать "Sync now" сверху, в app/build.gradle файле добавить это: ![image](docs/25.png) ![image](docs/26.png)
тоже нажать "Sync now"

# Работа с бд в коде
Пример класса объекта коллекции:
```java
import org.bson.types.ObjectId;

public class Player {

    public ObjectId _id; // mongodb id key
    public String name;
    public int score;

    public Player(String name, int score) {
        this._id = new ObjectId();
        this.name = name;
        this.score = score;
    }

    public Player(String name, int score, ObjectId _id) {
        this._id = _id;
        this.name = name;
        this.score = score;
    }
}
```

Пример кода в классе, отвечающем за работу с бд:
```Java
public class Database {

    private static final String appId = "test-aufed"; // app id из пункта 20
    private static final String service = "mongodb-atlas";
    private static final String database = "Test"; // название бд из пунка 12
    private static final String playerCol = "Player"; // название коллекции из пункта 12
        // если коллекций несколько, то добавить еще по такому же принципу

    public static App app = new App(new AppConfiguration.Builder(appId).build()); // конфиг realm-a
    public static User user; // переменная-пользователь бд

    static MongoClient client; // клиент
    static MongoDatabase data; // база данных
    static MongoCollection<Document> playersData; // получаемая из базы коллекция

    public static void init() { // метод, который нужно вызвать при успешной аутентификации
        user = app.currentUser(); // получить вошедшего пользователя
        client = Objects.requireNonNull(user).getMongoClient(service); // получить его монго-клиент
        data = client.getDatabase(database); // получить нужную бд
        playersData = data.getCollection(playerCol); // и коллекцию
    }
}
```

Пример кода в контроллере/активити, устанавливающий соединение с базой:
```java
public class Quiz extends Application {

    public static Set<Player> players = new HashSet<>(); // Сет игроков

    public void onCreate() {
        // Connect to Mongo
        Realm.init(this);
        Database.app.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
                Database.init();
                players = Database.getPlayers();
            }
        });
        //
    }
}
```

Пример метода для получения коллекции:
```java
public class Database {
    public static Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();

        // get players collection from main database
        playersData.find().iterator().getAsync(result -> {
            if (result.isSuccess()) {
                MongoCursor<Document> collection = result.get();

                while (collection.hasNext()) {
                    Document cur = collection.next();
                    players.add(new Player(
                        cur.getString("name"),
                        cur.getInteger("score"),
                        cur.getObjectId("_id")
                    ));
                }
            }
        });

        return players;
    }
}
```

Пример метода для добавления элемента в коллекцию:
```java
public class Database {
    public static void addPlayer(Player player) {
        playersData.insertOne(new Document()
            .append("name", player.name)
            .append("score", player.score)
            .append("_id", player._id)
        ).getAsync(result -> {});
    }
}
```
Пример метода для удаления элемента из коллекции:
```java
public class Database {
    public static void removePlayer(Player player) {
        playersData.deleteOne(new Document().append("_id", player._id)).getAsync(result -> {});
    }
}
```
