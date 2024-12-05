package utils;

import java.util.Objects;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        // Construtor que inicializa linha e coluna com valores fornecidos
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        // Retorna o valor de linha
        return row;
    }

    public int getColumn() {
        // Retorna o valor de coluna
        return column;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

}