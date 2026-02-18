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

## ARQUITECTURA DEL SISTEMA 🔺

### FRONTEND 🔻

> [!NOTE]
> ***INTERFAZ DE USUARIO, NO TIENE LÓGICA DE NEGOCIO, SOLO CAPTURA LOS DATOS Y LOS ENVÍA AL BACKEND***

- ***Esta página web nunca habla directamente con las bases de datos, ese rol lo tiene `con-external`, el orquestador***
  
  - ***DISEÑO 💗💗***
  <br>
  <img width="664" height="406" alt="image" src="https://github.com/user-attachments/assets/0bf4300e-ce76-46cc-8aea-8ac021b49492" />

---

### CON-EXTERNAL 8095🔻

> [!NOTE]
> ***ES EL INTERMEDIARIO, SIENDO EL ÚNICO PUNTO DE CONTACTO CON EL FRONTEND***

- ***Recibe peticiones de usuario y decide a qué microservicio llamar basándose en la intención del usuario (Depende de la petición del usuario en la web)***
  
  - ***Para escirbir (POST) = Se encarga de distribuir el libro a los 3 destinos, manda crear un XML en `prd-rex` y ordena guardar en `Postgres` y `Mongo`***
  - ***PARA LEER (GET) = Consulta directamente a la base de datos relacional para máxima velocidad***


<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/2b81c6eb-b6f4-4259-8bcd-994575a74000" />


---


### PRD-REX 8090 🔻

>[!TIP]
> ***MICROSERVICIO DEDICADO A LA PERSISTENCIA DE FICHEROS***

- ***Utiliza la librería jackson XML para serializar los objetos java recibidos a formato XML***

<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/9241dc94-6e1c-4746-b500-c22c09083776" />


---

### RELATIONAL-PRD-QUERY 8091

>[!NOTE] 
> ***USA POSTGRES PARA GUARDAR LOS DATOS ESTRUCTURADOS Y REALIZAR BÚSQUEDAS RÁPIDAS***

<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/9115cfd6-0dc4-42b2-95d3-559ddf06c47a" />


---

### NON RELATIONAL-PRD-QUERY

>[!NOTE] 
> ***USA MONGODB PARA GUARDAR EL DOCUMENTO JSON OCMO RESPALDO NoSQL***

<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/94ce509e-a987-4cdc-a8f1-9a5d76ecaa50" />








---



## MERMAID PARA ENTENDER LA ARQUITECTURA Y FUNCIONAMIENTO 🏡

```mermaid
graph TD
    %% --- DEFINICIÓN DE ESTILOS (Paleta Suave) ---
    classDef frontend fill:#e1f5fe,stroke:#01579b,stroke-width:2px,color:#000;
    classDef orchestrator fill:#d1c4e9,stroke:#512da8,stroke-width:3px,color:#000;
    classDef workers fill:#b2dfdb,stroke:#00695c,stroke-width:2px,color:#000;
    classDef storage fill:#fff9c4,stroke:#fbc02d,stroke-width:2px,color:#000;

    %% --- BLOQUE 1: CLIENTE ---
    subgraph CLIENTE [" 💻 FRONTEND "]
        UI["Interfaz Web (HTML/JS)"]:::frontend
    end

    %% --- BLOQUE 2: EL JEFE ---
    subgraph ORQUESTADOR [" 📡 CON-EXTERNAL (8095) "]
        GW["DISTRIBUIDOR CENTRAL"]:::orchestrator
    end

    %% --- BLOQUE 3: LOS TRABAJADORES ---
    subgraph WORKERS [" ⚙️ MICROSERVICIOS "]
        Rex["PRD-REX (8090)<br/>Generador XML"]:::workers
        SqlServ["RELATIONAL (8091)<br/>Motor SQL"]:::workers
        MongoServ["NON-RELATIONAL (8093)<br/>Motor NoSQL"]:::workers
    end

    %% --- BLOQUE 4: ALMACENAMIENTO ---
    subgraph DATA [" 💾 PERSISTENCIA "]
        XML[("Fichero .xml")]:::storage
        DB_PG[("PostgreSQL")]:::storage
        DB_MG[("MongoDB")]:::storage
    end

    %% --- CONEXIONES ---
    UI -->|1. POST JSON| GW
    
    %% EL JEFE MANDA A LOS 3 A LA VEZ
    GW -.->|2a. Crea| Rex
    GW -->|2b. Guarda| SqlServ
    GW -->|2c. Guarda| MongoServ

    %% PERSISTENCIA FINAL
    Rex --> XML
    SqlServ --> DB_PG
    MongoServ --> DB_MG

```

```mermaid

sequenceDiagram
    autonumber
    
    %% PARTICIPANTES CON ALIAS
    actor User as 👤 USUARIO
    participant Front as 💻 FRONTEND
    participant Jefe as 📡 CON-EXTERNAL
    participant XmlWorker as 📄 PRD-REX
    participant SqlWorker as 🐘 RELATIONAL
    participant MongoWorker as 🍃 NON-RELATIONAL

    %% INICIO
    User->>Front: Click "GUARDAR LIBRO"
    
    %% ENVIO AL JEFE
    Front->>Jefe: POST /registro (JSON)
    Note right of Jefe: Recibe y distribuye
    
    %% FASE 1: XML
    rect rgb(235, 245, 251)
        Note right of Jefe: **1. GENERACIÓN DE FICHERO**
        Jefe->>XmlWorker: POST /prdrex/registro
        Note right of XmlWorker: Crea XML localmente
        XmlWorker-->>Jefe: 200 OK
    end

    %% FASE 2: BBDD
    rect rgb(253, 237, 236)
        Note right of Jefe: **2. PERSISTENCIA EN BBDD**
        par En paralelo (Lógico)
            Jefe->>SqlWorker: POST /relational/registro
            Note right of SqlWorker: INSERT INTO Postgres
            SqlWorker-->>Jefe: 200 OK
        and
            Jefe->>MongoWorker: POST /nonrelational/registro
            Note right of MongoWorker: db.save(Mongo)
            MongoWorker-->>Jefe: 200 OK
        end
    end

    %% FINAL
    Jefe-->>Front: 200 OK "Todo Guardado"
    Front-->>User: ✅ ALERTA: "Éxito Total"
```


