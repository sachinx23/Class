// Script para crear la base de datos Tel--ONDA con esquemas de validación.
// Ejecutar con:  mongosh "mongodb://localhost:27017" database/crear_bd.js
// (o copiar y pegar en la consola mongosh de Compass)

db = db.getSiblingDB("Tel--ONDA");

// OJO: borra la base de datos entera para crearla de cero (también los datos que tenga)
db.dropDatabase();

// ---------------------------------------------------------------- Clientes
db.createCollection("Clientes", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nombreUsuario", "nombre", "contrasena", "DNI", "correo"],
      properties: {
        nombreUsuario: { bsonType: "string", minLength: 1 },
        nombre:        { bsonType: "string", minLength: 1 },
        contrasena:    { bsonType: "string", minLength: 4 },
        DNI:           { bsonType: "string", pattern: "^[0-9]{8}[A-Z]$" },
        correo:        { bsonType: "string", pattern: "^.+@.+\\..+$" },
        telefono:      { bsonType: "string" },
        direccion:     { bsonType: "string" },
        cuentaBancaria:{ bsonType: "string" },
        dadoDeBaja:    { bsonType: "bool" },
        ticketSoporte: { bsonType: "array", items: { bsonType: "objectId" } }
      }
    }
  }
});
db.Clientes.createIndex({ nombreUsuario: 1 }, { unique: true });

// ---------------------------------------------------------------- Tarifas
db.createCollection("Tarifas", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nombreTarifa", "precio", "tipotarifa"],
      properties: {
        nombreTarifa: { bsonType: "string" },
        precio:       { bsonType: ["int", "double"], minimum: 0 },
        detalles:     { bsonType: "string" },
        tipotarifa:   { bsonType: "string" },
        activa:       { bsonType: "bool" }
      }
    }
  }
});

// ---------------------------------------------------------------- Contratos
db.createCollection("Contratos", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["telefono", "estado", "idCliente", "idTarifa", "fechaComienzo", "tarifa"],
      properties: {
        tarifa: {
          bsonType: "object",
          required: ["nombreTarifa", "precio"],
          properties: {
            nombreTarifa: { bsonType: "string" },
            precio:       { bsonType: ["int", "double"] }
          }
        },
        estado:    { enum: ["pendiente", "activo", "baja pendiente", "cancelado"] },
        idCliente: { bsonType: "objectId" },
        idTarifa:  { bsonType: "objectId" }
      }
    }
  }
});

// ---------------------------------------------------------------- Resto
db.createCollection("Empleados");
db.createCollection("Administradores");
db.createCollection("TicketsSoportes");
db.createCollection("Sucursales");
db.createCollection("Valoraciones", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["puntuacion", "texto", "idCliente"],
      properties: {
        puntuacion: { bsonType: "int", minimum: 1, maximum: 5 },
        texto:      { bsonType: "string", minLength: 1 },
        idCliente:  { bsonType: "objectId" }
      }
    }
  }
});

// ---------------------------------------------------------------- Datos de ejemplo
db.Tarifas.insertMany([
  { nombreTarifa: "Onda Fibra+Móvil 600", precio: 40, detalles: "600 MB simétricos + 50 GB móvil", tipotarifa: "Fibra + Móvil", activa: true },
  { nombreTarifa: "Onda Móvil 30",        precio: 15, detalles: "30 GB + llamadas ilimitadas",     tipotarifa: "Móvil",         activa: true },
  { nombreTarifa: "Onda Fibra 1G",        precio: 35, detalles: "1 GB simétrico",                  tipotarifa: "Fibra",         activa: true }
]);

db.Administradores.insertOne({
  nombre: "Aitor fernandez", nombreUsuario: "Aitorfernandez0", rol: "Admin",
  contrasena: "abcd", correo: "aitorfernandez@gmail.com", cuentaBancaria: "ES7921000813610123456789"
});

const idSucursal = ObjectId();
const idPablo = ObjectId();

db.Sucursales.insertOne({
  _id: idSucursal, nombre: "Tel-ONDA Bilbao", horario: "Lunes a viernes: 09:00-20:00",
  telefono: "987 687 654",
  Ubicacion: { Direccion: "Lonbo Kalea", Localidad: "Bilbao", CodigoPostal: 48480 }
});

db.Empleados.insertOne({
  _id: idPablo, nombre: "Pablo diaz", nombreUsuario: "Pablodiaz0", rol: "Empleado",
  contrasena: "abcd", correo: "pablodiaz@gmail.com", cuentaBancaria: "ES7921000813610123456789",
  idSucursal: idSucursal
});

db.Clientes.insertOne({
  nombreUsuario: "PedroDiaz0", nombre: "Pedro", contrasena: "abcd", DNI: "12345678F",
  correo: "pedrodiaz@gmail.com", telefono: "666 666 666", direccion: "calle abando 2",
  cuentaBancaria: "ES7921000813610123456789", dadoDeBaja: false, ticketSoporte: []
});

print("Base de datos Tel--ONDA creada");
