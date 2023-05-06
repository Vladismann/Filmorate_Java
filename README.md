# java-filmorate
**Educational project. Social network for rating movies.**

**Database schema** https://dbdiagram.io/d/64564506dca9fb07c49d5b10
![Database Image](DBschema.png)

**Query examples**
<br />

Get user friends:
_SELECT *
FROM users
WHERE user_id IN (SELECT friend_id
FROM user_friends
WHERE user_id = 228
AND confirmed = 1);_
<br />
Get users who add like to the film:
_SELECT *
FROM users
WHERE user_id IN (SELECT user_id
FROM film_likes
WHERE film_id = 777);_
<br />

Get film genres names:
_SELECT name
<br />
FROM genres g
<br />
INNER JOIN film_genres fg ON fg.genre_id = g.genre_id
<br />
INNER JOIN films f ON f.film_id = fg.film_id_
<br />
WHERE f.film_id = 228;_
<br />

Get name an count of likes 10 most popular films
SELECT f.name
<br />
COUNT fl.user_id AS likes
<br />
FROM films f
<br />
INNER JOIN film_likes fl ON f.film_id = fl.film_id
<br />
GROUP BY f.name
<br />
ORDER BY likes
<br />
LIMIT 10;
