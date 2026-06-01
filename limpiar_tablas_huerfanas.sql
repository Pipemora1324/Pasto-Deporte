-- Script: limpiar_tablas_huerfanas.sql
-- Elimina las tablas de deportistas y certificaciones que ya no tienen
-- entidad Java ni controlador asociado en el sistema.
-- EJECUTAR SOLO UNA VEZ en la base de datos pasto_deporte.

DROP TABLE IF EXISTS certificaciones CASCADE;
DROP TABLE IF EXISTS deportistas CASCADE;
