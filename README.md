# MICROSERVICIO CHETADO 🪽

![img](https://i.pinimg.com/originals/4d/04/32/4d0432bb05c8def9fa01017aa50bfbcd.gif)

---

> [!WARNING]
> ***EL OBJETIVO DE ESTE MICROSERVICIO ES GESTIONAR EL INVENTARIO DE UNA BIBLIOTECA***

- ***El sistema implementa un patrón de persistencia políglota, almacenando simltáneamente en tres formatos distintos***
  
  - ***ARCHIVOS XML***
  - ***BASE DE DATOS RELACIONAL (POSTGRESQL)***
  - ***BASE DE DATOS NO RELACIONAL (MONGODB)***

---

## ARQUITECTURA DEL SISTEMA 🥎

### FRONTEND ⭕

> [!NOTE]
> ***INTERFAZ DE USUARIO, NO TIENE LÓGICA DE NEGOCIO, SOLO CAPTURA LOS DATOS Y LOS ENVÍA AL BACKEND***

- ***Esta página web nunca habla directamente con las bases de datos, ese rol lo tiene `con-external`***
  
  - **DISEÑO**
  <br>
  <img width="664" height="406" alt="image" src="https://github.com/user-attachments/assets/0bf4300e-ce76-46cc-8aea-8ac021b49492" />


### CON-EXTERNAL

> [!NOTE]
> ***ES EL INTERMEDIARIO, SIENDO EL ÚNICO PUNTO DE CONTACTO CON EL FRONTEND***

- ***Recibe peticiones de usuario y decide a quién llamar***
  - ****Para escirbir (POST) = Delega el trabajo al orquestador***
  - ***PARA LEER (GET) = Consulta directamente ***














---



## MERMAID PARA ENTENDER LA ARQUITECTURA Y FUNCIONAMIENTO 🏡

```mermaid

graph TD
    %% --- ESTILOS ---
    classDef frontend fill:#ff0055,stroke:#333,stroke-width:2px,color:white;
    classDef backend fill:#9900ff,stroke:#333,stroke-width:2px,color:white;
    classDef logic fill:#ffff00,stroke:#333,stroke-width:2px,color:black;
    classDef storage fill:#00ccff,stroke:#333,stroke-width:2px,color:black;

    %% --- BLOQUE 1: FRONTEND ---
    subgraph CLIENTE ["FRONTEND (Navegador)"]
        HTML["Interfaz HTML (Inputs)"]
        JS["Script.js (Fetch API)"]
    end

    %% --- BLOQUE 2: BACKEND ---
    subgraph SERVIDOR ["SERVIDOR SPRING (8095)"]
        Controller["RestController"]
        
        subgraph SERVICIO ["RexService.java"]
            Proceso["1. LÓGICA DE NEGOCIO<br/>(Gestión del Libro)"]
            Jackson["2. JACKSON: Crear XML"]
            RestT["3. REST: Enviar a APIs"]
        end
    end

    %% --- BLOQUE 3: ALMACENAMIENTO ---
    subgraph DATOS ["PERSISTENCIA"]
        Files[("Fichero XML Local")]
        
        subgraph EXTERNOS ["MICROSERVICIOS"]
            SQL[("Postgres 8091")]
            NoSQL[("Mongo 8093")]
        end
    end

    %% --- CONEXIONES ---
    HTML -->|1. Click Guardar| JS
    JS -->|2. POST JSON| Controller
    
    Controller -->|3. Objeto Libro| Proceso
    Proceso -->|4. Libro Procesado| Jackson
    Proceso -->|4. Libro Procesado| RestT

    Jackson -->|5. Escribe| Files
    RestT -->|6. POST JSON| SQL
    RestT -->|6. POST JSON| NoSQL

    %% --- APLICAR COLORES ---
    class HTML,JS frontend;
    class Controller,RestT,Jackson backend;
    class Proceso logic;
    class Files,SQL,NoSQL storage;

```

```mermaid
sequenceDiagram
    autonumber
    
    %% PARTICIPANTES
    actor User as USUARIO
    participant Front as FRONTEND (HTML/JS)
    participant Back as BACKEND (RexService)
    participant XML as FICHERO LOCAL (XML)
    participant ExtSQL as EXT. MICROSERVICIO (8091)
    participant ExtMongo as EXT. MICROSERVICIO (8093)

    %% INICIO
    Note over User, Front: Rellena ISBN, Título, Autor
    User->>Front: Click "GUARDAR"
    
    %% ENVIO AL BACKEND
    Front->>Back: POST /registro (JSON Libro)
    
    %% PROCESAMIENTO
    Note right of Back: **PROCESAMIENTO INTERNO**

    %% PERSISTENCIA LOCAL
    Back->>XML: Generar Archivo XML (Jackson)
    XML-->>Back: Archivo Creado OK

    %% COMUNICACIÓN EXTERNA (CAJA ROJA)
    rect rgb(255, 230, 230)
        Note right of Back: **DISTRIBUCIÓN A EXTERNOS**
        par Envio Paralelo
            Back->>ExtSQL: POST JSON (RestTemplate)
            ExtSQL-->>Back: 200 OK (Postgres)
        and
            Back->>ExtMongo: POST JSON (RestTemplate)
            ExtMongo-->>Back: 200 OK (Mongo)
        end
    end

    %% RESPUESTA FINAL
    Back-->>Front: Respuesta 200 OK
    Front-->>User: Mensaje: "LIBRO GUARDADO CORRECTAMENTE"

```


