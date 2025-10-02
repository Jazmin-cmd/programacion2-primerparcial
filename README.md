# 🏦 BancoApp - Simulación Bancaria  

🎓 **Proyecto académico:** Desarrollo de aplicación móvil en **Java con Android Studio**  
💻 **Objetivo:** Gestionar clientes, cuentas y transacciones de manera segura y coherente  
🚀 **Características principales:** CRUD completo, integridad referencial con llaves foráneas y escalabilidad en la base de datos  

---

## 📝 Descripción del proyecto  

**BancoApp** es una aplicación que simula el funcionamiento de un banco real.  
El diseño de la base de datos refleja de manera fiel la lógica del mundo real, manteniendo la integridad y consistencia de los datos.  

- Al **crear un cliente**, este puede tener **una o varias cuentas**.  
- Cada **cuenta** puede registrar múltiples **transacciones**.  
- Gracias a la **regla ON DELETE CASCADE**, si un cliente es eliminado, también lo son sus cuentas y transacciones asociadas, evitando inconsistencias.  

### 🔹 Beneficios del diseño  

1. **Reflejo del mundo real:**  
   Las entidades no existen en el vacío, sino interconectadas de forma jerárquica.  

2. **Integridad referencial:**  
   No puede existir una transacción sin cuenta, ni una cuenta sin cliente.  

3. **Consultas eficientes:**  
   - Obtener todas las cuentas de un cliente filtrando por `fkClienteId`.  
   - Obtener todas las transacciones de una cuenta filtrando por `fkCuentaId`.  

4. **Escalabilidad:**  
   El sistema puede crecer fácilmente. Por ejemplo, agregar **Préstamos** o **Tarjetas de Crédito** solo requeriría nuevas tablas relacionadas con **Cliente**.  

> En resumen, **BancoApp no es solo un CRUD**, sino un sistema coherente y escalable, donde la lógica de la base de datos guía la lógica de negocio y la interfaz de usuario acompaña al flujo de manera intuitiva.  

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

Cliente | Cuenta | Transacciones
--------|--------|--------------
Juan Pérez | Cuenta Ahorro | Depósito $1000, Retiro $200  
Ana López | Cuenta Corriente | Depósito $5000, Transferencia $1200  

---

## 📊 Estructura simplificada de la BD  

```mermaid
erDiagram
    CLIENTE ||--o{ CUENTA : tiene
    CUENTA ||--o{ TRANSACCION : registra

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
