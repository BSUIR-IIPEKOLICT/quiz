# Работа с MongoDB в Kotlin

Пример класса для получения всех монго коллекций при старте приложения:
```kotlin
class AppRepository : LOSApp() {

    private lateinit var realm: App // переменная для конфига Realm

    private var user: User? = null // пользователь
    private var client: MongoClient? = null // его клиент
    private var data: MongoDatabase? = null // загружаемая бд

    override fun onCreate() { // при создании инстанса приложения
        super.onCreate()

        if (isOnline(this)) { // если есть подключение к инету (изкоробочный метод класса LOSApp)
            // Connect to Mongo
            Realm.init(this) // инициализация
            realm = App(AppConfiguration.Builder(appId).build()) // постройка конфига
            realm.loginAsync(Credentials.anonymous()) { result: App.Result<User?> -> // запрос в монго на авторизацию
                if (result.isSuccess) { // если успешен
                    init() // метод для загрузки бд
                    get() // метод для загрузки всех коллекций из бд
                }
            }
            //
        }
    }

    companion object {
        private const val appId = "quiz-sjhgv" // id MongoDb Realm приложения
        private const val service = "mongodb-atlas" // используемый сервис
        private const val database = "Main" // имя бд
    }

    object Collection { // здесь перечислить названия всех нужных коллекций
        const val player = "Player" // имя коллекции
    }

    object Data { // здесь перечислить переменные для хранения всех нужных коллекций
        var playersData: MongoCollection<Document>? = null
    }

    private fun init() { // метод для загрузки бд
        user = realm.currentUser() // получаем юзера
        client = user?.getMongoClient(service) // получаем клиент
        data = client?.getDatabase(database) // получаем бд
    }

    fun get() { // метод для загрузки всех коллекций из бд
        Data.playersData = data?.getCollection(Collection.player)
    }
}
```

Пример класса-репозитория для работы с элементами коллекции:
```kotlin
object PlayerRepository {

    private val collection = AppRepository.Data.playersData // переменная для хранения монго-коолекции

    val data: MutableSet<Player>? // поле, к котором будет преобразованный в set результат
        get() { // (пример геттера коллекции)
            return if (collection != null) {
                val players: MutableSet<Player> = HashSet()
                collection.find().iterator().getAsync { result: App.Result<MongoCursor<Document>> ->
                    if (result.isSuccess) {
                        val collection = result.get()
                        while (collection.hasNext()) {
                            val cur = collection.next()
                            players.add(Player.convert(cur))
                        }
                    }
                }
                players
            } else null
        }

    fun addPlayer(player: Player) { // добавление элемента в коллекцию
        collection!!.insertOne(Player.convert(player)).getAsync { }
    }

    fun removePlayer(player: Player) { // удаление элемента из коллекции
        collection!!.deleteOne(Document().append(LOSApp.MONGO_ID, player._id)).getAsync { }
    }
}
```

Пример класса-модели:
```kotlin
class Player {
    var _id: ObjectId
    var name: String
    var score: Int

    constructor(name: String, score: Int) {
        _id = ObjectId()
        this.name = name
        this.score = score
    }

    constructor(name: String, score: Int, _id: ObjectId) {
        this._id = _id
        this.name = name
        this.score = score
    }

    companion object {
        // названия полей
        private const val NAME = "name"
        private const val SCORE = "score"
        private const val ID = "_id"

        fun convert(document: Document): Player { // преобразование из документа в Player
            return Player(
                document.getString(NAME),
                document.getInteger(SCORE),
                document.getObjectId(ID)
            )
        }

        fun convert(player: Player): Document { // преобразование из Player в документ
            return Document()
                .append(NAME, player.name)
                .append(SCORE, player.score)
                .append(ID, player._id)
        }
    }
}
```