## Как запустить
- gradlew build
- docker-compose up --no_start
- docker-compose start
- далее:
  - либо открыть http://0.0.0.0:8080
  - либо сгенерировать клиент из описания API

## Структура
service container:
- web: обработка запросов и подготовка ответов
- service: разная логика
- service.di: попытка применить Dependency Inversion
- database: работа с базой данных

db container:
- PostgreSQL DB


