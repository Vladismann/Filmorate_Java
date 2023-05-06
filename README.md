# java-filmorate
**Educational project. Social network for rating movies.**

**Database schema** https://dbdiagram.io/d/64564506dca9fb07c49d5b10
![Database Image](filmorate.png)

**Query examples**<br />
Get user friends:
_SELECT *
FROM users
WHERE user_id IN (SELECT friends
FROM users)_
<br />
Get users who add like to the film:
_SELECT *
FROM users
WHERE user_id IN (SELECT likes
FROM films
WHERE film_id = 777)_
<br />
Get film genres names:
_SELECT name
FROM genres
WHERE genre_id IN (SELECT genres
FROM films
WHERE film_id = 228)_
