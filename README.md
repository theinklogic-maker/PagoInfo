# PagoInfo — App de datos de pago para Android Auto

App para mostrar información de transferencia bancaria en la pantalla de Android Auto.  
Desarrollada para uso con Uber en Fiat Cronos 2024 (Uconnect 7", conexión USB).

---

## Qué hace

**En el teléfono (configuración):**
- Editás alias, CBU/CVU, banco y titular
- Cargás tu QR desde la galería
- Personalizás el mensaje de voz
- Ajustás el tamaño de fuente

**En la pantalla del auto (Android Auto):**
- Muestra "Información de transferencia" con todos tus datos
- Botón para leer en voz alta por los parlantes del auto
- Botón para actualizar si cambiaste algo en el teléfono

---

## Cómo obtener el APK (GitHub Actions — sin instalar Android Studio)

### Paso 1: Crear el repositorio

1. Andá a [github.com](https://github.com) y creá una cuenta si no tenés
2. Hacé clic en **New repository**
3. Nombre: `PagoInfo`
4. Visibilidad: **Private** (recomendado)
5. Hacé clic en **Create repository**

### Paso 2: Subir los archivos

En la página del repo recién creado:

1. Hacé clic en **uploading an existing file**
2. Arrastrá toda la carpeta `PagoInfo` descomprimida
3. Escribí el mensaje de commit: `Initial commit`
4. Hacé clic en **Commit changes**

### Paso 3: Ejecutar el workflow

1. En tu repo, andá a la pestaña **Actions**
2. Si aparece un aviso de seguridad, hacé clic en **I understand my workflows, go ahead and enable them**
3. En el panel izquierdo seleccioná **Build PagoInfo APK**
4. Hacé clic en **Run workflow** → **Run workflow** (botón verde)
5. Esperá ~3-5 minutos mientras compila

### Paso 4: Descargar el APK

1. Cuando el workflow termine (ícono verde ✓), hacé clic en él
2. Al final de la página, en la sección **Artifacts**, vas a ver **PagoInfo-APK**
3. Hacé clic para descargarlo — obtenés un ZIP con el APK adentro

---

## Cómo instalar el APK en tu Redmi Note 8

### Habilitar instalación de fuentes desconocidas (una sola vez)

1. **Ajustes** → **Privacidad** → **Acceso especial de apps** → **Instalar apps desconocidas**
2. Elegí **Archivos** (o el gestor de archivos que uses)
3. Activá **Permitir desde esta fuente**

### Instalar

1. Pasá el APK al teléfono (por Google Drive, WhatsApp, cable USB, lo que prefieras)
2. Abrí el APK con el gestor de archivos
3. Tocá **Instalar**
4. Abrí PagoInfo, configurá tus datos y guardá

---

## Cómo activar la app en Android Auto

### Activar modo desarrollador de Android Auto

1. Abrí la app **Android Auto** en el teléfono
2. Tocá el ícono de **tres puntos** (arriba a la derecha) → **Acerca de**
3. Tocá varias veces sobre el número de versión hasta que aparezca el mensaje **"Eres un desarrollador"**
4. Volvé al menú → ahora aparece **Ajustes de desarrollador**
5. En **Ajustes de desarrollador**, activá **Fuentes desconocidas**

### Instalar como "Google Play Store" con King Installer (necesario para Android Auto)

Android Auto solo muestra apps instaladas como si vinieran de Play Store.  
King Installer resuelve esto:

1. Descargá **King Installer** desde: https://github.com/Rikj000/KingInstaller/releases
2. Instalalo en tu teléfono (igual que arriba, fuentes desconocidas)
3. Abrí King Installer
4. Tocá **Select file...** y elegí el APK de PagoInfo
5. Tocá **Install as king**
6. Esperá a que termine

### Verificar que funciona

1. Conectá el teléfono al auto con el cable USB
2. En la pantalla del auto, en el menú de apps de Android Auto, buscá **PagoInfo**
3. Si no aparece: en el teléfono, abrí Android Auto → Personalizar el launcher → buscá PagoInfo y activalo

---

## Resolución de problemas

**La app no aparece en Android Auto:**
- Asegurate de haber instalado con King Installer (no directo)
- Verificá que "Fuentes desconocidas" esté activo en el Developer Mode de Android Auto
- Desconectá y volvé a conectar el cable USB
- Reiniciá el teléfono

**El audio no funciona en el auto:**
- Tocá el botón "Leer en voz alta" en la pantalla del auto
- El volumen de los parlantes debe estar subido

**Los datos no se actualizan en el auto:**
- Guardá primero en el teléfono
- Tocá "Actualizar" en la pantalla del auto

---

## Datos técnicos

- Package: `com.stuch.pagoinfo`
- minSdk: 26 (Android 8.0+)
- targetSdk: 34 (Android 14)
- Categoría Android Auto: IoT (Internet of Things)
- Sin permisos de red — todo local
- Compatible: Fiat Cronos 2024, Uconnect 7", USB

---

*PagoInfo — desarrollado con Espina Asesores*
