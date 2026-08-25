# Migração de Jetpack Compose para XML (View System)

Este plano descreve a refatoração completa do projeto Nexus Store para utilizar o sistema de Views tradicional (XML), mantendo a arquitetura limpa e as boas práticas de desenvolvimento Android Sênior.

## Mudanças Propostas

### 1. Build e Dependências

#### [MODIFY] [libs.versions.toml](file:///D:/Projetos Android/NexusStore/gradle/libs.versions.toml)
* Remover dependências do Compose.
* Adicionar `navigation-fragment-ktx`, `navigation-ui-ktx`, `constraintlayout`, `recyclerview` e `material`.
* Manter Retrofit e Koin (versão core/android).

#### [MODIFY] [build.gradle.kts](file:///D:/Projetos Android/NexusStore/app/build.gradle.kts)
* Desabilitar `buildFeatures { compose = true }`.
* Habilitar `buildFeatures { viewBinding = true }`.
* Atualizar a lista de dependências.

### 2. Recursos e Layouts (XML)

#### [NEW] [activity_main.xml](file:///D:/Projetos Android/NexusStore/app/src/main/res/layout/activity_main.xml)
* Conterá o `FragmentContainerView` para o `NavHostFragment`.

#### [NEW] [fragment_home.xml](file:///D:/Projetos Android/NexusStore/app/src/main/res/layout/fragment_home.xml)
* Layout da tela principal com uma `Toolbar` e um `GridLayout` ou `ConstraintLayout` configurado como Grid 2x2 com 4 botões de proporção igual.

#### [NEW] [nav_graph.xml](file:///D:/Projetos Android/NexusStore/app/src/main/res/navigation/nav_graph.xml)
* Definição do grafo de navegação entre os fragmentos.

### 3. Apresentação (Kotlin)

#### [MODIFY] [MainActivity.kt](file:///D:/Projetos Android/NexusStore/app/src/main/java/com/jamiltonmentoria/nexusstore/MainActivity.kt)
* Migrar para utilizar ViewBinding e configurar a `Toolbar` com o `NavController`.

#### [NEW] [HomeFragment.kt](file:///D:/Projetos Android/NexusStore/app/src/main/java/com/jamiltonmentoria/nexusstore/presentation/home/HomeFragment.kt)
* Implementar a lógica de clique dos botões do grid para navegação.

#### [DELETE] [HomeScreen.kt](file:///D:/Projetos Android/NexusStore/app/src/main/java/com/jamiltonmentoria/nexusstore/presentation/home/HomeScreen.kt)
#### [DELETE] [AppNavigation.kt](file:///D:/Projetos Android/NexusStore/app/src/main/java/com/jamiltonmentoria/nexusstore/presentation/navigation/AppNavigation.kt)

## Plano de Verificação

### Testes Manuais
1. Compilar o projeto e verificar se a tela inicial em XML é carregada.
2. Validar se o grid 2x2 está com os botões estilizados e proporcionais.
3. Testar os cliques nos botões para garantir que a navegação via NavController está ocorrendo.
