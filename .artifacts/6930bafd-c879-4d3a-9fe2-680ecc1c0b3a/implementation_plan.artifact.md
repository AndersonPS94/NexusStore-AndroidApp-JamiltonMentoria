# Implementação da Splash Screen com o Logo do App

Este plano descreve as etapas para adicionar a Splash Screen da API do Android 12+ ao aplicativo Nexus Store, utilizando o logo `iconnexusstore.png` recentemente adicionado.

## User Review Required

> [!NOTE]
> A implementação utiliza a biblioteca `androidx.core:core-splashscreen` para garantir compatibilidade entre diferentes versões do Android.

## Proposed Changes

### Dependências

#### [MODIFY] [libs.versions.toml](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/gradle/libs.versions.toml)
Adicionar a versão e a biblioteca `androidx.core:core-splashscreen`.

#### [MODIFY] [app/build.gradle.kts](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/app/build.gradle.kts)
Adicionar a dependência da Splash Screen.

---

### Recursos e Temas

#### [MODIFY] [themes.xml (values)](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/app/src/main/res/values/themes.xml)
Criar o tema `Theme.App.Starting` herdando de `Theme.SplashScreen` e definir o logo `iconnexusstore` como ícone da splash.

#### [MODIFY] [themes.xml (values-night)](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/app/src/main/res/values-night/themes.xml)
Configurar o tema de splash para o modo noturno.

---

### Configuração do Manifesto e Código

#### [MODIFY] [AndroidManifest.xml](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/app/src/main/AndroidManifest.xml)
Alterar o tema da `MainActivity` para `Theme.App.Starting`.

#### [MODIFY] [MainActivity.kt](file:///Users/andersonpereiradossantos/AndroidStudioProjects/NexusStore-AndroidApp-JamiltonMentoria/app/src/main/java/com/jamiltonmentoria/nexusstore/presentation/view/MainActivity.kt)
Chamar `installSplashScreen()` antes de `setContentView`.

## Verification Plan

### Manual Verification
- Executar o app no emulador/dispositivo.
- Verificar se o logo `iconnexusstore` aparece centralizado ao iniciar o app.
- Confirmar se o app transita corretamente para a `MainActivity`.
