# Указываем базовый образ с JDK 21
FROM openjdk:21-jdk-slim as builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

# Копируем исходный код и строим приложение
COPY src /app/src
RUN ./mvnw clean package -DskipTests

# Указываем второй этап для создания конечного образа
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл из первого этапа
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8080

# Запускаем приложение
# ENTRYPOINT ["java", "-jar", "app.jar"]
