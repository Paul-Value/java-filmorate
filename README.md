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

## Схема базы данных 
![Scheme Data Base](https://github.com/Paul-Value/java-filmorate/blob/main/schemeBD.png)

Примеры запросов:

1. **Получение списка друзей пользователя:**

```sql
SELECT u.name 
FROM user_friends AS uf
JOIN user AS u ON uf.friendId = u.id
WHERE uf.userId = 1 AND uf.status = 'confirmed';
```

2. **Получение списка фильмов, которым пользователь поставил лайк:**

```sql
SELECT f.name
FROM user_film_likes AS ufl
JOIN film AS f ON f.id = ufl.filmId
WHERE userId = 1;
```

3. **Получение списка фильмов по жанру:**

```sql
SELECT f.name 
FROM film AS f
JOIN film_henre AS fg ON f.id = fg.filmId
WHERE fg.genreId = 1;
```

4. **Получение топ-10 самых популярных фильмов:**

```sql
SELECT 
    f.name, 
    COUNT(ufl.filmId) AS likes_count
FROM 
    film AS f
JOIN 
    user_film_likes AS ufl ON f.id = ufl.filmId
GROUP BY 
    f.id, f.name
ORDER BY 
    likes_count DESC
LIMIT 10;
```