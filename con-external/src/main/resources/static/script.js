const URL_BASE = "http://localhost:8095/api/v1/libros";

async function guardarLibro() {
    const isbn = document.getElementById('isbn-reg').value;
    const nome = document.getElementById('nome-reg').value;
    const autor = document.getElementById('autor-reg').value;

    const cajaMsg = document.getElementById('msg-registro');

    if (!isbn || !nome || !autor) {
        pintarMensaje(cajaMsg, "RELLENA TODOS LOS CAMPOS", "error");
        return;
    }

    const libro = { isbn, nome, autor };

    try {
        const respuesta = await fetch(URL_BASE + "/registro", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(libro)
        });

        if (respuesta.ok) {
            document.getElementById('isbn-reg').value = '';
            document.getElementById('nome-reg').value = '';
            document.getElementById('autor-reg').value = '';

            pintarMensaje(cajaMsg, "LIBRO GUARDADO CORRECTAMENTE", "exito");
        } else {
            pintarMensaje(cajaMsg, "ERROR EN EL SERVIDOR AL GUARDAR", "error");
        }
    } catch (error) {
        pintarMensaje(cajaMsg, "ERROR DE CONEXIÓN", "error");
    }
}

async function buscarLibro() {
    const isbn = document.getElementById('isbn-busqueda').value;
    const cajaMsg = document.getElementById('msg-busqueda');

    if (!isbn) {
        pintarMensaje(cajaMsg, "ESCRIBE EL ISBN PRIMERO", "error");
        return;
    }

    try {
        const respuesta = await fetch(URL_BASE + "/consulta/isbn/" + isbn);

        if (respuesta.ok) {
            const libro = await respuesta.json();

            const html = `
                <u>ENCONTRADO</u><br><br>
                <span style="font-size:1.5rem">${libro.nome}</span><br>
                <small>DE: ${libro.autor}</small>
            `;
            pintarMensaje(cajaMsg, html, "info");
        } else {
            pintarMensaje(cajaMsg, "NO EXISTE ESE LIBRO", "error");
        }

    } catch (error) {
        pintarMensaje(cajaMsg, "ERROR AL BUSCAR", "error");
    }
}

async function buscarLibroNombre() {
    const nome = document.getElementById('nome-busqueda').value;
    const cajaMsg = document.getElementById('msg-busqueda');

    if (!nome) {
        pintarMensaje(cajaMsg, "ESCRIBE EL NOMBRE PRIMERO", "error");
        return;
    }

    try {
        const respuesta = await fetch(URL_BASE + "/consulta/nombre/" + nome);

        if (respuesta.ok) {
            const libros = await respuesta.json();

            if (libros.length === 0) {
                pintarMensaje(cajaMsg, "NO SE ENCONTRARON LIBROS", "error");
                return;
            }

            let html = `ENCONTRADOS: ${libros.length}<br><br>`;

            libros.forEach(libro => {
                html += `
                <div style="border-bottom:2px solid black; margin-bottom:15px; padding-bottom:10px;">
                    <span style="font-size:1.4rem; color:#9900ff;">${libro.nome}</span><br>
                    <small>AUTOR: ${libro.autor}</small><br>
                    <small>ISBN: ${libro.isbn}</small>
                </div>`;
            });

            pintarMensaje(cajaMsg, html, "info");
        } else {
            pintarMensaje(cajaMsg, "ERROR AL CONSULTAR", "error");
        }

    } catch (error) {
        pintarMensaje(cajaMsg, "ERROR DE CONEXIÓN", "error");
    }
}

function pintarMensaje(elemento, texto, clase) {
    elemento.innerHTML = texto;
    elemento.className = 'caja-mensaje';
    elemento.classList.add(clase);
}