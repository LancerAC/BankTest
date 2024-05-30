# Тестовое задание Effective Mobile
Необходимо написать сервис для “банковских” операций. В нашей системе есть пользователи (клиенты), у каждого клиента есть строго один “банковский аккаунт”, в котором изначально лежит какая-то сумма. Деньги можно переводить между клиентами. На средства также начисляются проценты.

Функциональные требования:

В системе есть пользователи, у каждого пользователя есть строго один “банковский аккаунт”. У пользователя также есть телефон и email. Телефон и или email должен быть минимум один. На “банковском счету” должна быть какая-то изначальная сумма. Также у пользователя должна быть указана дата рождения и ФИО.
Для простоты будем считать что в системе нет ролей, только обычные клиенты. Пусть будет служебный апи (с открытым доступом), через который можно заводить новых пользователей в системе, указав логин, пароль, изначальную сумму, телефон и email (логин, телефон и email не должны быть заняты). 
Баланс счета клиента не может уходит в минус ни при каких обстоятельствах.
Пользователь может добавить/сменить свои номер телефона и/или email, если они еще не заняты другими пользователями.
Пользователь может удалить свои телефон и/или email. При этом нельзя удалить последний.
Остальные данные пользователь не может менять.
Сделать АПИ поиска. Искать можно любого клиента. Должна быть фильтрация и пагинация/сортировка. Фильтры:
Если передана дата рождения, то фильтр записей, где дата рождения больше чем переданный в запросе.
Если передан телефон, то фильтр по 100% сходству.
Если передано ФИО, то фильтр по like форматом ‘{text-from-request-param}%’
Если передан email, то фильтр по 100% сходству. 
Доступ к АПИ должен быть аутентифицирован (кроме служебного апи для создания новых клиентов).
Раз в минуту баланс каждого клиента увеличиваются на 5% но не более 207% от начального депозита.
Например:
Было: 100, стало: 105.
Было: 105, стало:110.25.
Реализовать функционал перевода денег с одного счета на другой. Со счета аутентифицированного пользователя, на счёт другого пользователя. Сделать все необходимые валидации и потокобезопасной.


Нефункциональные требования:

Добавить OpenAPI/Swagger
Добавить логирование
Аутентификация через JWT.
Нужно сделать тесты на покрытие функционала трансфера денег.

# Стек проекта:
 * Java
 * Spring
 * Junit5
 * TestContainers
 * Docker
 * Liquibase
 * Swagger
 * JWT
 * PostgreSQL

# Postman 
 Создание пользователя
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/4ef4e443-f049-467e-9af3-b9c83dd825ed)


 Получение токена
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/8cfc9083-2d82-482e-89d8-8a69cf554851)


 Поиск пользователя по критериям
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/a7939d8e-e5af-43f0-967e-d7b79459ed28)

 
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/026c8e70-30ea-4a3b-944d-22f08bb9b438)

 
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/9ff37dd3-4e88-42f0-a76d-c8a57109206d)


 Перевод денег
 ![image](https://github.com/LancerAC/BankTest/assets/152904551/0824012a-b355-42b5-9d4c-2b6a403e35c7)
