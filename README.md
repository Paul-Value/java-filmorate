# java-filmorate
Filmorate - социальная сеть по оценке фильмов пользователями. Пользователи могут добавлять друзей, ставить лайки, получать информацию о популярных фильмах и находить общих друзей.

## API

* POST /users — создание пользователя;
* PUT /users — обновление пользователя;
* PUT /users/{id}/friends/{friendId} — добавление в друзья.
* DELETE /users/{id}/friends/{friendId} — удаление из друзей.
* GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
* GET /users/{id}/friends/common/{otherId} — список общих друзей с другим пользователем.
* GET /users — получение списка всех пользователей.

**Film**
* POST /films — Добавление фильма.
* PUT /films — Обновляет фильма.
* PUT /films/{id}/like/{userId} — Пользователь ставит лайк фильму.
* DELETE /films/{id}/like/{userId} — Пользователь удаляет лайк.
* GET /films — Возвращает список всех фильмов
* GET /films/popular?count={count} — Возвращает список из первых count* фильмов по количеству лайков.
*
    + Если значение параметра count не задано, вернуть первые 10.

**Genre**
* GET /genres/{id} — получение существующего жанра.
* GET /genres — получение списка всех жанров.

**MPARating**
* GET /mpa/{id} — получение существующего рейтинга.
* GET /mpa — получение списка всех рейтингов.

### Шаги для развёртывания
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/Paul-Value/java-filmorate.git
   ```

3. Соберите проект с помощью Maven:
   ```bash
   mvn clean install
   ```

4. Для локальной работы с базой данных PostgreSQL, запустите Docker-контейнер:
   ```bash
   docker-compose up -d
   ```

5. Запустите приложение:
   ```bash
   java -jar target/filmorate-0.0.1-SNAPSHOT.jar
   ```

6. Приложение будет доступно по адресу:
   ```
   http://localhost:8080
   ```

## Схема базы данных 
![Scheme Data Base](https://github.com/Paul-Value/java-filmorate/blob/main/schemeBD.png)
