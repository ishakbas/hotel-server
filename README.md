# Kotlin сервер с использованием библиотек [Ktor](https://github.com/ktorio/ktor) и [Exposed](https://github.com/JetBrains/Exposed) для мобильного приложения
## Запуск сервера
* Предварительно установить [PostgreSQL](https://www.postgresql.org/)
* Восстановить базу данных из dump'а: [dump](https://drive.google.com/file/d/1h4iVBFf1ioc55OBcUBVkq3wGoLHZgZn4/view?usp=sharing)
* Запустить проект в [IntelliJ IDEA]()
* Поменять параметры подключения к базе данных в [файле](https://github.com/ishakbas/hotel-server/blob/d3997aa9fae01d8e4f1205492f678b4958c9661a/src/main/kotlin/com/example/plugins/DataBase.kt#L8)  (если необходимо)
* Запустить сервер [main функцией](https://github.com/ishakbas/hotel-server/blob/d3997aa9fae01d8e4f1205492f678b4958c9661a/src/main/kotlin/com/example/Application.kt#L8)
