# Sistema de Gestión Académica

Aplicación de consola desarrollada en **Java**, utilizando Programación Orientada a Objetos (POO) y arquitectura **MVC**, para administrar estudiantes, profesores, aulas y sus relaciones.

## Funcionalidades

- **Estudiantes y profesores:** ABM, búsquedas y reactivación.
- **Aulas:** gestión de aulas y sus capacidades.
- **Inscripciones:** asignación de estudiantes a aulas, cambios y cancelaciones.
- **Asignaciones:** vinculación de profesores con aulas y reasignaciones.
- **Consultas académicas:** aula de un estudiante, estudiantes a cargo de un profesor y profesor asignado a un estudiante.
- **Validaciones:** control de capacidad, restricciones de asignación y bajas lógicas.

## Tecnologías

- Java
- Programación Orientada a Objetos
- Arquitectura MVC
- Persistencia en archivos TXT (formato CSV)
- Apache NetBeans (Ant)

## Estructura del proyecto

```text
sistemaestudiantil/
├── controllers/
├── daos/
├── dtos/
├── exceptions/
├── interfaces/
├── models/
├── services/
├── utils/
├── views/
└── Main.java
```

## Ejecución

1. Clonar el repositorio.
2. Abrir el proyecto en Apache NetBeans.
3. Ejecutar `Main.java`.
4. Utilizar el menú interactivo para gestionar los registros.

Los datos se almacenan en archivos `.txt` y se conservan entre ejecuciones.

## Autor

Proyecto académico desarrollado para la materia **Programación II**.