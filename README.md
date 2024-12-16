# Curso de TDD y Automatización de Pruebas

## Instalación

- [JDK](https://www.oracle.com/java/technologies/downloads/)
- [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2024-09/R/eclipse-jee-2024-09-R-win32-x86_64.zip)
  - Help > Eclipse Marketplace ... > [Spring Tools 4 (aka Spring Tool Suite 4)](https://marketplace.eclipse.org/content/spring-tools-4-aka-spring-tool-suite-4)
  - [Project Lombok](https://projectlombok.org/downloads/lombok.jar)
- [Spring Tools](https://spring.io/tools)

### Pruebas E2E

- [Selenium IDE](https://chromewebstore.google.com/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd?hl=es&utm_source=ext_sidebar)
- [Postman](https://www.postman.com/downloads/)
- [SoapUI](https://www.soapui.org/downloads/soapui/)

### Pruebas de rendimiento

- [JMeter](https://jmeter.apache.org/download_jmeter.cgi)

## Integración continua

### Instalación Docker

- [WSL 2 feature on Windows](https://learn.microsoft.com/es-es/windows/wsl/install)
- [Docker Desktop](https://www.docker.com/get-started/)

### Alternativas a Docker Desktop

- [Podman](https://podman.io/docs/installation)
- [Rancher Desktop](https://rancherdesktop.io/)

### Contenedores

#### SonarQube

    docker run -d --name sonarqube --publish 9000:9000 sonarqube:latest

#### Selenium Grid

    cd docker\selenium && docker compose up -d

### Comandos

#### Maven

    docker run --rm -it --name maven -v "%cd%":/proyecto -v maven-repository:/root/.m2 maven:3.8.6-eclipse-temurin-11 sh

#### NodeJS

    docker run --rm -v .:/proyecto node:lts-alpine sh -c "cd /proyecto && npm test"

#### SonarQube Scanner

    docker run --rm -v .:/usr/src -e SONAR_HOST_URL="http://host.docker.internal:9000"  sonarsource/sonar-scanner-cli

## Repositorios

- <https://github.com/jmagit/Web4Testing>
- <https://github.com/jmagit/MOCKWebServer>

## Kata

- [GildedRose](https://github.com/emilybache/GildedRose-Refactoring-Kata/blob/main/GildedRoseRequirements_es.md)

## Ejemplos

- <https://github.com/spring-projects/spring-petclinic>

## Laboratorios

- [Working a Getting Started guide with STS](https://spring.io/guides/gs/sts)
- [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)
- [Testing the Web Layer](https://spring.io/guides/gs/testing-web)
- [Consumer Driven Contracts](https://spring.io/guides/gs/contract-rest)
