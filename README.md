Proyecto de Laboratorio #7
Integrantes: 
Rafael Castillo - 22541188
Bella Serrano - 22541173
Gabriel Gutierrez - 22551046

ESTRUCTURA BINARIA:
(CABECERA)
  6 bytes  - ID "EDT1"
  4 bytes  - int version
  4 bytes  - int cantidad de bloques

(POR CADA BLOQUE)
  1 byte   - tipo (0 = Fragmento, 1 = Tabla)

  SI ES Fragmento:
    UTF - texto
    4 bytes - color RGB (int)
    1 byte  - negrita (boolean)
    1 byte  - cursiva (boolean)
    1 byte  - subrayado (boolean)
    1 byte  - tachado (boolean)
    UTF    - fuente
    4 bytes - tamaño (int)

  SI ES Tabla:
    4 bytes - filas
    4 bytes - columnas
    POR CADA CELDA (fila por fila):
      4 bytes - cantidad de fragmentos
