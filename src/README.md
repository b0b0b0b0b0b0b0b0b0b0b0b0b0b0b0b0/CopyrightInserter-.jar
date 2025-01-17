# CopyrightInserter
**CopyrightInserter** — это Java-приложение, которое модифицирует JAR-файлы плагинов Minecraft, добавляя в них определенные метаданные, такие как поля в классах, комментарии в YAML и JSON файлы, и обфускацию.

##### _Информация в данной статье может быть устаревшей. Пожалуйта, посмотрите актуальную на: https://black-minecraft.com/resources/copyrightinserter-jar.6062/_

## Основные функции
* **Добавление финального статического поля во все классы файла .jar**
* **Добавление комментариев в начало всех YAML файлов**
* **Добавление информации в конец всех JSON файлов**
* **Добавление комментария в архив**
* **Каждый модуль можно отключить**

## CLI
Программка работает на CLI, этому есть список команд, которые вы можете юзать для удобства.
* 1 - Запустить обработку файлов в папке 'input'
* 2 - Перезагрузка конфигурации
* 3 - Показать список доступных JAR-файлов
* 4 - Показать содержимое файла config.yml
* 5 - Открыть папку 'input'
* 6 - Открыть папку 'output'
* 7 - Очистить папку 'output'
* 8 - Обновить список файлов
* 9 - Справка
* 10 - Выход

## Конфигурация
```yml
# Конфигурация для добавления поля в классы Java
field:
  name: "BLACK_MINECRAFT" # Имя добавляемого поля
  value: "black-minecraft.com" # Значение добавляемого поля
  enabled: true # Включает или отключает добавление поля в классы

# Конфигурация для добавления комментариев в YAML файлы
comment: |
  #
  # Black-Minecraft
  #
commentEnabled: true # Включает или отключает добавление комментариев в YAML файлы

# Детализированный комментарий для добавления в ZIP-архив
detailedComment: |
  ################################# B M #################################
                    ___                  _
  #                | _ \  _     ____  __| | __                               #
  #                | __|_| |   / _  |/ _/ |/ /                               #
  #                |  _  \ |  / / | | / |   /  ____                          #
  #                | |_) | |__| |_| | \_|   \ /___/                          #
                   |_____|____\___,_|\__\_|\\_
  B          _                                                               B
  M  _     _(_)_   _  ___  ___ _ ___  ____ ____ _______   ___  ___  _     _  M
    | \   / | | \ | |/   \/ __|  ___|/ _  |  __|__   __| / _/ / _ \| \   / |
  # |  \_/  | |  \  |  $ | /  | |   / / | | |__   | |   / /  | | | |  \_/  | #
  # |       | | |\  |  __/ \__| |  |  |_| |  __|  | |  _\ \_ | |_| |       | #
  # |_|\_/|_|_|_| \_|___|___|_|   \___,_|_|     |_| (_)\\__\ \___/|_|\_/|_| #
  #                                                                          #
    ################################# B M #################################

                          BLACK-MINECRAFT.COM
detailedCommentEnabled: true # Включает или отключает добавление детализированного комментария в ZIP-архив

# Конфигурация для добавления поля в JSON файлы
bmField:
  name: "BM" # Имя добавляемого поля в JSON файлы
  value: "Black-Minecraft.com" # Значение добавляемого поля в JSON файлы
  enabled: true # Включает или отключает добавление поля в JSON файлы

```

## Требования
- Java 21 или новее
- Maven для сборки проекта (Собранное можете скачать тут: https://github.com/b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0/CopyrightInserter-.jar/releases)

## Сборка и установка
Склонируйте репозиторий:

```bash
   git clone https://github.com/b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0/CopyrightInserter-.jar/
   ```

### Соберите проект с использованием Maven:

```bash
mvn clean package
   ```

## Запуск с указанием версии java:
```bash
@echo off
set JAVA_PATH="C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot\bin\java.exe"
set JAR_NAME=CopyrightInserter

for %%f in (*%JAR_NAME%*.jar) do (
    echo start %%f...
    %JAVA_PATH% -jar "%%f"
    echo.
)
pause
   ```
## Запуск без указанием версии java:
```bash
@echo off
set JAR_NAME=CopyrightInserter

for %%f in ([I]%JAR_NAME%[/I].jar) do (
    echo start %%f...
    java -jar "%%f"
    echo.
)
pause
   ```