# CopyrightInserter

**CopyrightInserter** — это Java-приложение, которое модифицирует JAR-файлы плагинов Minecraft, добавляя в них определенные метаданные, такие как поля в классах, комментарии в YAML и JSON файлы, и обфускацию.

## Основные функции

1. **Добавление поля в классы**:
   - Добавляет статическое финальное поле в каждый класс внутри JAR-файла с определённым значением.
   - Имя поля и его значение конфигурируются через `config.yml`.

2. **Добавление комментариев в YAML файлы**:
   - Вставляет комментарии в начало каждого YAML файла.

3. **Добавление информации в JSON файлы**:
   - Вставляет поле `"BM": "Black-Minecraft.comI"` в JSON файлы.

4. **Автоматическое извлечение конфигурационного файла**:
   - При первом запуске извлекает конфигурационный файл `config.yml` из JAR-файла и сохраняет его на диск.
   - Можете всё настроить под себя

## Требования
- Java 21 или новее
- Maven для сборки проекта

## Сборка и установка
Склонируйте репозиторий:

```bash
   git clone https://github.com/b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0/CopyrightInserter-.jar/
   ```


Соберите проект с использованием Maven:


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
    echo Запуск %%f...
    java -jar "%%f"
    echo.
)
pause
   ```
