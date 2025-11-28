# 🎮 ADIVINHAE — Jogo estilo Termo em JavaFX

Este projeto é uma implementação do jogo **Termo/Wordle** utilizando **JavaFX**, seguindo a arquitetura **MVC (Model–View–Controller)**.  
O objetivo é adivinhar uma palavra de 5 letras em até 6 tentativas, recebendo feedback visual para cada letra digitada.

---

## 📌 Funcionalidades

- Interface gráfica inspirada no layout do Termo original
- Entrada de letras **uma por vez**, diretamente pelo teclado
- Validação automática da palavra ao clicar **Enviar**
- Feedback visual com cores:
  - 🟩 **Verde** — letra correta na posição correta
  - 🟨 **Amarelo** — letra existe, mas em outra posição
  - ⬛ **Cinza** — letra não existe na palavra
- Animação visual de foco por célula
- Reinício de partida com botão "Reiniciar"
- Seleção automática de palavra do dicionário interno
- Sistema de tentativas com controle de linhas e colunas

---

## 🧱 Arquitetura MVC

### **Model — GameModel**
Responsável pela lógica central:
- Carregar dicionário
- Selecionar palavra aleatória
- Validar tentativas (`verificar()`)
- Controlar número de tentativas
- Comparar letra a letra

### **View — TermoView**
Responsável pela interface:
- Criação do Grid 5x6
- Exibição das letras
- Cores das células conforme o estado
- Botões (Enviar e Reiniciar)
- Mensagens ao usuário

### **Controller — GameController**
Responsável pela interação:
- Captura de teclado
- Preenchimento das células
- Controle da linha e coluna atual
- Chamadas ao Model e atualização da View
- Verificação da tentativa ao enviar

---

## 📂 Estrutura do Projeto

src/
└─ com.termo.termogame/
├── controllers/
│ └── GameController.java
├── views/
│ └── TermoView.java
├── models/
│ └── GameModel.java
├── enums/
│ └── EstadoDaLetra.java
└── resources/
└── dicionario.txt


---

## 🎮 Como Jogar

1. A aplicação inicia com um grid 5×6 vazio.
2. Digite letras do teclado — elas preenchem automaticamente a linha atual.
3. Ao completar 5 letras, clique **ENVIAR**.
4. O jogo fornece o resultado da tentativa por cor.
5. Se acertar → mensagem de vitória 🎉  
   Se errar 6 vezes → fim do jogo ❌
6. Clique **REINICIAR** para jogar novamente.

---

🖥️ Requisitos

Java 17+

JavaFX 17+

IDE recomendada: IntelliJ ou VSCode com plugin JavaFX

🚀 Execução

1. Clone o repositório:
git clone https://github.com/matheus-crisostomo/termoGame.git

2. Execute a classe principal:
mvn clean javafx:run
ou configure via IDE.

👨‍💻 Autor

Projeto desenvolvido para fins didáticos por Matheus Crisóstomo.
