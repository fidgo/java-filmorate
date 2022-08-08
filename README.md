# java-filmorate

## 1.Задача

Пользователи дружат с другими пользователями и оценивают(лайкают) фильмы. Дружба может быть односторонней. 
Общие друзья двух пользователей включают и друзей, которые не приняли дружбу в ответ. 

Фильмы и пользователи хранятся в базе данных. У фильма может считаться рейтинг(сумма лайков пользователей), который
определяется суммой уникальных, поставленных пользователям лайков.

При создании и получении фильмов передают список идентификаторов жанров и идентификатор mpa.
Эти же данные должны передаваться при обновлении, создании и получении фильмов.

## 2.Требования к задаче
### Пользователи 
* Получение всех пользователей.
* Получение пользователя по id.
* Получения всех друзей пользователя, взятого по id.
* Получения всех общих друзей двух пользователей по id и otherId.
* Создание пользователя.
* Обновление пользователя, если такой пользователь существует.
* Добавить пользователю по id в друзья другого пользователя, который задан friendId. 
* Удалить у пользователя по id из друзей пользователя, который задан friendId.

### Фильмы
* Получения всех фильмов
* Получения фильмы по id
* Получение первых count популярных фильмов. По умолчанию count равен 10.
* Создание фильма
* Обновление фильма, если существует такой фильм
* Фильму по id поставить лайк от пользователя, заданным userId
* Фильму по id убрать лайк от пользователя, заданным userId
* Получение списка всех жанров
* Получение жанра по id
* Получение списка всех рейтингов MPA
* Получение рейтинга MPA по id

## 3.Схема
![Схема базы данных](/assets/dtb_shema.png)

## 4.Ограничения

friendship уникальные пары (user_id, friends_id)

likes уникальные пары (user_id, film_id)

mpas: name должна быть из {'G', 'PG', 'PG-13', 'R', 'NC-17'}

genres: name должна быть из {'Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик'},
уникальные пары (film_id, name);

genres_per_film: уникальные пары (film_id, genres_id);

users: email уникальный, login уникальный 

## 5.Примеры запросов

Получение списка друзей пользователя (userId).
```postgresql
SELECT * FROM USERS JOIN FRIENDSHIP ON USERS.ID = FRIENDSHIP.FRIEND_ID WHERE FRIENDSHIP.USER_ID = userId;
```

Получение списка общих друзей двух пользователей (userId, otherId).
```postgresql
SELECT U.ID, U.NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY FROM USERS AS U JOIN FRIENDSHIP AS F ON U.ID = F.FRIEND_ID
WHERE (F.USER_ID = userId OR F.USER_ID = otherId) GROUP BY U.ID HAVING COUNT(*) = 2;
```

Получение фильма по id.
```postgresql
SELECT * FROM FILMS as F JOIN MPAS AS M ON F.MPA_ID = M.ID WHERE F.ID = id;
```

Получения первых (count) популярных фильмов.
```postgresql
SELECT COUNT(*) as RATING, FILMS.*, MPAS.NAME 
FROM LIKES JOIN FILMS ON LIKES.FILM_ID = FILMS.ID JOIN MPAS ON FILMS.MPA_ID = MPAS.ID
GROUP BY FILMS.ID
ORDER BY RATING DESC
LIMIT count;
```