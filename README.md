# 📅 MyFinance Planning

## 📌 Descripción

Este repositorio contiene el **microservicio de planificación financiera** de **MyFinance**, encargado de gestionar presupuestos, metas y estrategias de ahorro.  
Permite a los usuarios definir objetivos financieros, establecer límites de gasto y hacer seguimiento de su progreso.

Este microservicio está desarrollado con **Spring Boot**, proporcionando una API REST segura y eficiente para manejar la planificación financiera de los usuarios.

---

## ✨ Características Principales

- ✅ **Gestión de Presupuestos** – Creación y administración de presupuestos mensuales.
- ✅ **Definición de Metas Financieras** – Establecimiento de objetivos de ahorro o inversión.
- ✅ **Seguimiento de Gastos** – Comparación entre gastos y presupuesto definido.
- ✅ **Alertas y Recomendaciones** – Notificaciones cuando se excede el presupuesto.
- ✅ **Historial de Planificación** – Registro de cambios y progresos en la planificación.
- ✅ **APIs Seguras** – Protección con autenticación basada en JWT.
- ✅ **Base de Datos SQL** – Persistencia eficiente de los planes financieros.

---

## 🛠 Tecnologías Utilizadas

- **Spring Boot** – Framework para el desarrollo del backend.
- **Spring Security & JWT** – Manejo de autenticación segura.
- **Spring Data JPA** – Interacción con la base de datos.
- **MySQL** – Base de datos relacional para almacenamiento.
- **Docker** – Contenedorización del microservicio.

---

## 🚀 Instalación y Ejecución

### 📌 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17 o superior**
- **Maven**
- **Docker** (opcional)
- **Base de datos MySQL**

### 📥 Clonar el Repositorio

```sh
git clone https://github.com/MONTESDANIEL/myfinance-planning.git
cd myfinance-planning
```

### 🗃️ Configurar la base de datos

```sh
Utilizar el archivo .sql del proyecto para generar la base.
```

### ⚙️ Configurar el application.properties

Ajustar el application.properties de la siguiente forma según la base de datos:

```sh
spring.datasource.url=           # Url de acceso a la base de datos.
spring.datasource.username=      # Usuario de la base de datos
spring.datasource.password=      # Contraseña de la base de datos
```

### 📦 Construir y Ejecutar el Proyecto

Para compilar y ejecutar el proyecto:

```sh
mvn clean install
mvn spring-boot:run
```

---

## 📂 Estructura del Proyecto

```sh
myfinance-planning/
│── src/main/java/com/myfinance/backend/planning/
│   ├── config/         # Configuración de configuración
│   ├── controllers/    # Controladores REST
│   ├── entities/       # Entidades
│   ├── exceptions/     # Control de excepciones
│   ├── repositories/     # Acceso a la base de datos
│   ├── services/        # Lógica de negocio
│── src/main/java/com/myfinance/backend/planning/resources/
│   ├── application.properties      # Configuración del microservicio
│── Dockerfile          # Configuración para contenedorización
│── planning_db.sql    # Archivo de creación de la base de datos
│── README.md           # Documentación del repositorio
```

## 📜 Licencia

Este proyecto está bajo la licencia MIT, por lo que puedes usarlo y modificarlo libremente.

## ⛓️Relacionado

🔗 Repositorio Principal: [MyFinance](https://github.com/MONTESDANIEL/myfinance)
