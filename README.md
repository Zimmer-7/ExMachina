# 🧠 ExMachina — Compilador em Latim

## 📌 Descrição

O **ExMachina** é um compilador desenvolvido como projeto acadêmico, utilizando a ferramenta **JavaCC** em ambiente Java. Sua principal característica é a definição de uma linguagem baseada em termos do **LATIM**, explorando conceitos fundamentais da construção de compiladores.

O projeto tem como objetivo aplicar, na prática, conceitos como análise léxica, sintática e tratamento de estruturas formais de linguagem.

---

## 🎯 Objetivos

- Implementar um compilador funcional utilizando JavaCC  
- Criar uma linguagem com sintaxe baseada no latim  
- Aplicar conceitos teóricos de compiladores:
  - Análise léxica (tokens)
  - Análise sintática (parser)
  - Tratamento de erros  
- Desenvolver uma base para futuras extensões da linguagem  

---

## 🏗️ Estrutura do Projeto

```text
src/
 └── compilador/
     ├── ExMachina.java
     ├── ExMachinaConstants.java
     ├── ExMachinaTokenManager.java
     ├── ParserException.java
     ├── SimpleCharStream.java
     ├── Token.java
     ├── TokenMgrError.java
     └── ExMachina.jj
```


## ⚙️ Tecnologias Utilizadas
-Java (JDK 17)
-JavaCC
-Eclipse IDE


## 🚀 Como Executar
1. Pré-requisitos
-Java JDK 17 instalado
-JavaCC configurado
-IDE (recomendado: Eclipse)
2. Compilar
-javac compilador/*.java
3. Executar
-java compilador.ExMachina < entrada.txt


## 🧩 Exemplo da Linguagem
```text
initium
    numerus a = 10;
    numerus b = 20;
    scribere(a + b);
finis
```

## ⚠️ Tratamento de Erros

O compilador realiza:

Detecção de erros léxicos (tokens inválidos)
Detecção de erros sintáticos (estrutura incorreta)

Mensagens de erro são exibidas para auxiliar na correção.

## 📚 Conceitos Aplicados
Gramáticas livres de contexto
Parser LL (JavaCC)
Expressões regulares para tokens
Estrutura básica de compiladores

## 🔮 Melhorias Futuras
Análise semântica
Geração de código intermediário
Interface gráfica
Expansão da linguagem (loops, funções, etc.)


## 👥 Colaboradores

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/ErikMazzuco">
        <img src="https://github.com/ErikMazzuco.png" width="100px;" alt="Erik"/><br>
        <sub><b>Erik Mazzuco</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/LuizFelipeBastiao">
        <img src="https://github.com/LuizFelipeBastiao.png" width="100px;" alt="Luiz"/><br>
        <sub><b>Luiz Felipe</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Yasmin-YY">
        <img src="https://github.com/Yasmin-YY.png" width="100px;" alt="Yasmin"/><br>
        <sub><b>Yasmin Yamamoto</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Zimmer-7">
        <img src="https://github.com/Zimmer-7.png" width="100px;" alt="Zimmer-7"/><br>
        <sub><b>Gabriel Zimer</b></sub>
      </a>
    </td>
  </tr>
</table>


📄 Licença
Este projeto é de caráter educacional e não possui fins comerciais.




