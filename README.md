# 🏦 BancoLi - Simulación Bancaria  

🎓 **Proyecto académico:** Desarrollo de aplicación móvil en **Java con Android Studio**  
💻 **Objetivo:** Gestionar clientes, cuentas, transacciones y beneficiarios de manera segura y coherente  
🚀 **Características principales:** CRUD completo, integridad referencial con llaves foráneas, escalabilidad en la base de datos y manejo de las operaciones más básicas de un banco  

---

## 📝 Descripción del proyecto  

**BancoLi** es una aplicación que simula el funcionamiento básico de un banco real.  
El diseño de la base de datos refleja de manera fiel la lógica del mundo real, manteniendo la integridad y consistencia de los datos.  

🔹 Actualmente, la aplicación **maneja las operaciones más esenciales de un sistema bancario**, como:  
- Registro y administración de **clientes**.  
- Creación y gestión de **cuentas**.  
- Registro de **transacciones** (depósitos, retiros, transferencias).  
- Asociación de **beneficiarios** a las cuentas, para transferencias y gestión de autorizados.  

- Al **crear un cliente**, este puede tener **una o varias cuentas**.  
- Cada **cuenta** puede registrar múltiples **transacciones** y beneficiarios asociados.  
- Gracias a la **regla ON DELETE CASCADE**, si un cliente es eliminado, también lo son sus cuentas, transacciones y beneficiarios relacionados, evitando inconsistencias.  

### 🔹 Beneficios del diseño  

1. **Reflejo del mundo real:**  
   Las entidades no existen en el vacío, sino interconectadas de forma jerárquica.  

2. **Integridad referencial:**  
   No puede existir una transacción ni un beneficiario sin cuenta, ni una cuenta sin cliente.  

3. **Consultas eficientes:**  
   - Obtener todas las cuentas de un cliente filtrando por `fkClienteId`.  
   - Obtener todas las transacciones de una cuenta filtrando por `fkCuentaId`.  
   - Obtener todos los beneficiarios de una cuenta filtrando por `fkCuentaId`.  

4. **Escalabilidad:**  
   El sistema puede crecer fácilmente. Por ejemplo, agregar **Préstamos** o **Tarjetas de Crédito** solo requeriría nuevas tablas relacionadas con **Cliente**.  

> En resumen, **BancoLi maneja lo básico de un banco**, pero está estructurado de forma que puede escalar hacia un sistema más robusto en el futuro.  

---

## ⚙️ Tecnologías utilizadas  

<p align="left">
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white"/>
  <img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white"/>
  <img src="https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=google&logoColor=white"/>
</p>  

---

## 🧪 Datos de prueba  

Cliente | Cuenta | Transacciones | Beneficiarios
--------|--------|---------------|--------------
Juan Pérez | Cuenta Ahorro | Depósito $1000, Retiro $200 | María Pérez  
Ana López | Cuenta Corriente | Depósito $5000, Transferencia $1200 | Pedro López  

---

## 📊 Estructura simplificada de la BD  

```mermaid
erDiagram
    CLIENTE ||--o{ CUENTA : tiene
    CUENTA ||--o{ TRANSACCION : registra
    CUENTA ||--o{ BENEFICIARIO : autoriza

    CLIENTE {
        int idCliente PK
        string nombre
        string apellido
    }

    CUENTA {
        int idCuenta PK
        int fkClienteId FK
        string tipoCuenta
        float saldo
    }

    TRANSACCION {
        int idTransaccion PK
        int fkCuentaId FK
        string tipo
        float monto
        date fecha
    }

    BENEFICIARIO {
        int idBeneficiario PK
        int fkCuentaId FK
        string nombre
        string relacion
    }
