*quiz*
# Quiz app for android

**Задание по предмету РПОС в БГУИРе**

Разработать под андроид квиз с поддержкой свайпов, лидербоардом, 3 плюшками 50/50 и таймером на 15 сек для каждого 
вопроса сверху. Желательно использовать viewpager и фрагменты для отображения.

# Версии проги (они же ветки)
    Все требования задания выполнены, сверх того во всех версиях:
    
    1) построены на MVVM архитектуре
    2) инфа лидербороарда хранится на MongoDB
    
    Android 6.0+

### Beginner Edition
- Java
- максимально упрощенная реализация
- упрощенная логика
- никаких сложных наворотов
- вообще ничего лишнего
>Ветка java-be, [перейти](https://github.com/IIPEKOLICT/quiz/tree/java-be/)

### Community Edition
- Java
- используются тернарные операторы, сокращенный синтаксис
- более продвинутая логика
>Ветка java-ce, [перейти](https://github.com/IIPEKOLICT/quiz/tree/java-ce/)

### Java Edition
- Java
- использует [мою библиотеку](https://github.com/IIPEKOLICT/LOS), за счет чего круче визуалочка и user experience 
(ничто не мешает использовать эту версию, но тогда будет не лишним и разобраться хотя бы минимально в библиотеке, что 
не очень просто...)
- последняя версия на Java (~~наконец-то!~~)
>Ветка java, [перейти](https://github.com/IIPEKOLICT/quiz/tree/java/)

### Main
- Kotlin
- последняя стабильная версия на котлине
>Ветка main, [перейти](https://github.com/IIPEKOLICT/quiz/tree/main/)

### Kotlin
- Kotlin
- тестируемые фичи и самые свежие плюшки
>Ветка kotlin, [перейти](https://github.com/IIPEKOLICT/quiz/tree/kotlin/)

# Материалы
- [Плейлист по основам Java](https://youtube.com/playlist?list=PLIU76b8Cjem48KXIy83YIm-QM6SwvzjQd) (диктор такой себе, 
так что рекомендуется найти что-нить в таком духе)
- [Мой Android(Java) плейлист](https://youtube.com/playlist?list=PLqgAvARfkffXhdKxjjvIQs77IAlWQMoS1) (возможно есть 
лишнее...)
- [Мой Android(Kotlin) плейлист](https://youtube.com/playlist?list=PLqgAvARfkffXBfPdHvuuFjSmuLRByM6cB) (возможно есть 
лишнее...)
- [Плейлист, по инфе с которого я подключил MongoDB](https://youtube.com/playlist?list=PLBqHLq3IFiRLzpPgWwP-eUfazUBOOBm-F) 
(осторожно, индус (!!!))
- [Гайд по подключению MongoDB (мой)](https://github.com/IIPEKOLICT/quiz/tree/java-ce/MONGO.md)
- [Примеры работы с MongoDB на kotlin](MONGO.md)
- [Расшифровка некоторых используемых мной конструкций](https://github.com/IIPEKOLICT/quiz/tree/java-ce/LISTING.md)
- [Create swipe views with tabs using ViewPager2](https://developer.android.com/guide/navigation/navigation-swipe-view-2)
- [Slide between fragments using ViewPager](https://developer.android.com/training/animation/screen-slide)
- [Create dynamic lists with RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [CountDownTimer](https://developer.android.com/reference/android/os/CountDownTimer)

# Super Pro Edition
- [Как передавать данные между Activity и Fragment](https://youtu.be/qm3RxvIPfrw?list=PL_RkZ4J60MDk9XXw2kyydhaWR7iMX0SQJ)
- [Recycler View](https://youtube.com/playlist?list=PL_RkZ4J60MDkxuLuejSXyyvugMBR_ttZY)
- [MVVM-плейлист](https://youtube.com/playlist?list=PLY8G5DMG6TiMlF-iZmLSnrThvZQHuSpt2)
- [MVVM на Android Developers](https://developer.android.com/topic/libraries/architecture/viewmodel)