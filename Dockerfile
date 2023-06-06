#образ взятый за основу
FROM openjdk:17
#Записываем в переменную путь до варника (необязательно)
ARG jarFile=target/highstylewear-0.0.1-SNAPSHOT.war
#Куда переместить варник внутри контейнера
WORKDIR /opt/app
#копируем наш джарник в новый внутри контейнера
COPY ${jarFile} highstylewear-0.0.1-SNAPSHOT.war
#Открываем порт
EXPOSE 9090
#Команда для запуска
ENTRYPOINT ["java", "-jar", "highstylewear-0.0.1-SNAPSHOT.war"]
