// =====================================================================
//  Script para crear la base de datos "Tel--ONDA"
//
//  Cómo ejecutarlo (elige una):
//   - En MongoDB Compass: abre la consola ">_MONGOSH" de abajo, pega todo y pulsa Enter.
//   - En una terminal:    mongosh "mongodb://localhost:27017" database/crear_bd.js
//
//  OJO: borra la base de datos y la crea de cero (se pierden los datos que hubiera).
// =====================================================================
//
//  Qué es un "esquema de validación":
//  son las reglas que MongoDB comprueba antes de guardar un documento.
//  Si un documento no las cumple, MongoDB no lo guarda y da un error.
//
//    required:  campos que el documento TIENE que tener
//    bsonType:  tipo de cada campo ("string" = texto, "int" = número entero,
//               "double" = número con decimales, "bool" = true/false,
//               "objectId" = un _id, "object" = documento embebido, "array" = lista)
//    enum:      lista de valores permitidos
//
//  Lo que pide el reto y dónde está:
//    - 8 colecciones relacionadas entre sí (por sus _id)
//    - Información EMBEBIDA:  Contratos.tarifa  y  Sucursales.Ubicacion
//    - ARRAY de _id:          Clientes.ticketSoporte
//    - Todas las colecciones con esquema de validación
// =====================================================================

db = db.getSiblingDB("Tel--ONDA");
db.dropDatabase();


// ---------- CLIENTES ----------
// "ticketSoporte" es un ARRAY con los _id de los tickets del cliente
db.createCollection("Clientes", {
  validator: { $jsonSchema: {
    required: ["nombreUsuario", "nombre", "contrasena", "correo"],
    properties: {
      nombreUsuario: { bsonType: "string" },
      nombre:        { bsonType: "string" },
      contrasena:    { bsonType: "string" },
      correo:        { bsonType: "string" },
      dadoDeBaja:    { bsonType: "bool" },
      ticketSoporte: { bsonType: "array" }
    }
  }}
});


// ---------- TARIFAS ----------
db.createCollection("Tarifas", {
  validator: { $jsonSchema: {
    required: ["nombreTarifa", "precio", "tipotarifa"],
    properties: {
      nombreTarifa: { bsonType: "string" },
      precio:       { bsonType: ["int", "double"] },
      tipotarifa:   { bsonType: "string" },
      activa:       { bsonType: "bool" }
    }
  }}
});


// ---------- CONTRATOS ----------
// "tarifa" está EMBEBIDA: se guarda una copia del nombre y el precio de la tarifa
db.createCollection("Contratos", {
  validator: { $jsonSchema: {
    required: ["estado", "idCliente", "idTarifa", "tarifa"],
    properties: {
      estado:    { enum: ["pendiente", "activo", "baja pendiente", "cancelado"] },
      idCliente: { bsonType: "objectId" },
      idTarifa:  { bsonType: "objectId" },
      tarifa:    { bsonType: "object" }
    }
  }}
});


// ---------- EMPLEADOS ----------
db.createCollection("Empleados", {
  validator: { $jsonSchema: {
    required: ["nombre", "nombreUsuario", "contrasena", "correo"],
    properties: {
      nombre:        { bsonType: "string" },
      nombreUsuario: { bsonType: "string" },
      contrasena:    { bsonType: "string" },
      correo:        { bsonType: "string" }
    }
  }}
});


// ---------- ADMINISTRADORES ----------
db.createCollection("Administradores", {
  validator: { $jsonSchema: {
    required: ["nombre", "nombreUsuario", "contrasena", "correo"],
    properties: {
      nombre:        { bsonType: "string" },
      nombreUsuario: { bsonType: "string" },
      contrasena:    { bsonType: "string" },
      correo:        { bsonType: "string" }
    }
  }}
});


// ---------- TICKETS DE SOPORTE ----------
db.createCollection("TicketsSoportes", {
  validator: { $jsonSchema: {
    required: ["asunto", "descripcion", "estado", "idCliente"],
    properties: {
      asunto:      { bsonType: "string" },
      descripcion: { bsonType: "string" },
      estado:      { enum: ["pendiente", "en proceso", "resuelto", "cerrado"] },
      idCliente:   { bsonType: "objectId" }
    }
  }}
});


// ---------- SUCURSALES ----------
// "Ubicacion" está EMBEBIDA: dirección, localidad y código postal
db.createCollection("Sucursales", {
  validator: { $jsonSchema: {
    required: ["nombre", "telefono", "Ubicacion"],
    properties: {
      nombre:    { bsonType: "string" },
      telefono:  { bsonType: "string" },
      Ubicacion: { bsonType: "object" }
    }
  }}
});


// ---------- VALORACIONES ----------
db.createCollection("Valoraciones", {
  validator: { $jsonSchema: {
    required: ["puntuacion", "texto", "idCliente"],
    properties: {
      puntuacion: { bsonType: "int", minimum: 1, maximum: 5 },
      texto:      { bsonType: "string" },
      idCliente:  { bsonType: "objectId" }
    }
  }}
});


// =====================================================================
//  DATOS DE EJEMPLO (contraseña de todos: abcd)
// =====================================================================

db.Tarifas.insertMany([
  { nombreTarifa: "Onda Fibra+Móvil 600", precio: 40, detalles: "600 MB simétricos + 50 GB móvil", tipotarifa: "Fibra + Móvil", activa: true },
  { nombreTarifa: "Onda Móvil 30",        precio: 15, detalles: "30 GB + llamadas ilimitadas",     tipotarifa: "Móvil",         activa: true },
  { nombreTarifa: "Onda Fibra 1G",        precio: 35, detalles: "1 GB simétrico",                  tipotarifa: "Fibra",         activa: true }
]);

db.Administradores.insertOne({
  nombre: "Aitor fernandez", nombreUsuario: "Aitorfernandez0", rol: "Admin",
  contrasena: "abcd", correo: "aitorfernandez@gmail.com", cuentaBancaria: "ES7921000813610123456789"
});

// Se crea el _id de la sucursal antes, para poder ponerlo también en el empleado
const idSucursal = ObjectId();

db.Sucursales.insertOne({
  _id: idSucursal, nombre: "Tel-ONDA Bilbao", horario: "Lunes a viernes: 09:00-20:00", telefono: "987 687 654",
  Ubicacion: { Direccion: "Lonbo Kalea", Localidad: "Bilbao", CodigoPostal: 48480 }
});

db.Empleados.insertOne({
  nombre: "Pablo diaz", nombreUsuario: "Pablodiaz0", rol: "Empleado",
  contrasena: "abcd", correo: "pablodiaz@gmail.com", cuentaBancaria: "ES7921000813610123456789",
  idSucursal: idSucursal
});

db.Clientes.insertOne({
  nombreUsuario: "PedroDiaz0", nombre: "Pedro", contrasena: "abcd", DNI: "12345678F",
  correo: "pedrodiaz@gmail.com", telefono: "666 666 666", direccion: "calle abando 2",
  cuentaBancaria: "ES7921000813610123456789", dadoDeBaja: false, ticketSoporte: []
});

print("Base de datos Tel--ONDA creada");
