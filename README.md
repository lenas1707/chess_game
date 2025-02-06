# Chess Game
>Um jogo de xadrez desenvolvido em Java, combinando gráficos AWT e Swing com elementos em Pixel Art.
- Lógica de Programação, código e artes de menu feito por <https://github.com/lenas1707>
- Requisitos e casos de uso feito por <> 
## 🎮 Características
 - Interface intuitiva
 - Implementação de Orientação a Objeto
- Sistema de login para jogadores
- Marcação visual de movimentos legais
- Indicadores de xeque e xeque-mate
- Camada de persistência de dados
  
## 📸 Screenshots
### Menu Principal
![Menu](/Screenshots/menuscreen.png)
### Tela de Login
![Login](/Screenshots/playerloginscreen.png)
### Tabuleiro do Jogo
![Menu](/Screenshots/gamescreen.png)
### Sistema de Movimentos
![Movimentos](/Screenshots/chesstablemark.png)
### Partida em Andamento
![Partida em Andamento](/Screenshots/chessgameprogress.png)
## 🎨 Detalhes Técnicos
- Tabuleiro: Desenvolvido com Java AWT
- Interface e Elementos: Criados em Pixel Art (Aseprite)
- Persistencia de Dados: Devido as limitações na plataforma que o código foi apresentado o uso de um Banco de Dados foi desconsiderado devido a falta de permissão de acesso, mas para simular uma coleta de dados foi usado a criação e atualização de um arquivo .txt que salva o confronto e seu vencedor em ordem cronológica 

# Lógica

## Lógica Matemática usada na implementação do código

Um tabuleiro de xadrez é representado por uma matriz quadrada A<sub>8x8</sub> , contendo 16 peças para cada um dos dois jogadores. As peças são distribuídas da seguinte forma: 1 rei, 1 rainha, 2 torres, 2 cavalos, 2 bispos e 8 peões. Tradicionalmente, um jogador utiliza as peças de cor branca e o outro, as de cor preta. O objetivo do jogo é forçar o rei do adversário a uma posição de xeque-mate, onde ele não possui movimentos legais disponíveis para escapar da ameaça.

Representação do tabuleiro:

$$ 
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{18}\\     
a_{21} & a_{22} & \cdots & a_{28}\\     
\vdots & \vdots & \ddots & \vdots\\     
a_{81} & a_{82} & \cdots & a_{88}\\
\end{bmatrix}
$$

## Cada tipo de peça tem um padrão de movimentação distinto:

- **Rei**: pode mover-se em qualquer direção, mas apenas um quadrado de cada vez.
- **Rainha**: pode mover-se em qualquer direção, sem restrições quanto à distância.
- **Torre**: pode mover-se em linha reta, horizontal ou verticalmente, sem restrições de distância.
- **Bispo**: pode mover-se diagonalmente, também sem restrições de distância.
- **Cavalo**: movimenta-se em um padrão em ‘L’, podendo saltar sobre outras peças.
- **Peão**: avança em linha reta, geralmente um quadrado de cada vez (pode avançar dois quadrados no primeiro movimento).

## Condições de jogo:

- **Xeque**: o rei está ameaçado, mas ainda possui movimentos legais.
- **Xeque-Mate**: o rei está ameaçado e não possui movimentos legais que o salvem.
- **Impasse**: o jogador não está em xeque, mas não possui movimentos legais disponíveis.

## Posicionando as peças na matriz \(A<sub>8x8</sub>):

> Usando como base a indexação de Array Bidimensional:

<div align="center">

| Tipo de Peça | Posições                             |
|--------------|-------------------------------------|
| **Torre**    | t<sub>00</sub>, t<sub>07</sub>, t<sub>70</sub>, t<sub>77</sub> |
| **Rainha**   | ra<sub>03</sub>, ra<sub>73</sub>   |
| **Rei**      | r<sub>04</sub>, r<sub>74</sub>     |
| **Cavalo**   | c<sub>01</sub>, c<sub>06</sub>, c<sub>71</sub>, c<sub>76</sub> |
| **Bispo**    | b<sub>02</sub>, b<sub>05</sub>, b<sub>72</sub>, b<sub>75</sub> |
| **Peão**     | p<sub>10</sub> ... p<sub>17</sub><br>p<sub>60</sub> ... p<sub>67</sub> |  

</div>

<br>

> Representação do tabuleiro no estado inicial do jogo

$$ 
\begin{bmatrix}
t_{00} & c_{01} & b_{02} & ra_{03} & r_{04} & b_{05} & c_{06} & t_{07} \\
p_{10} & p_{11} & p_{12} & p_{13} & p_{14} & p_{15} & p_{16} & p_{17} \\
a_1 & a_2 & a_3 & a_4 & a_5 & a_6 & a_7 & c_8 \\
a_1 & a_2 & a_3 & a_4 & a_5 & a_6 & a_7 & a_8 \\
a_1 & a_2 & a_3 & a_4 & a_5 & a_6 & a_7 & a_8 \\
a_1 & a_2 & a_3 & a_4 & a_5 & a_6 & a_7 & a_8 \\
p_{60} & p_{61} & p_{62} & p_{63} & p_{64} & p_{65} & p_{66} & p_{67} \\
t_{70} & c_{71} & b_{72} & ra_{73} & r_{74} & b_{75} & c_{76} & t_{77} \\
\end{bmatrix}
$$



