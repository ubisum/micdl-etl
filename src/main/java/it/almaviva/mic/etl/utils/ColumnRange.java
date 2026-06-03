package it.almaviva.mic.etl.utils;

import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.exceptions.MicdlETLException;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ColumnRange {

    private final int startInclusive;
    private final int endInclusive;

    public ColumnRange(int startInclusive, int endInclusive) 
    {
        if (startInclusive > endInclusive) 
            throw new MicdlETLException("L'indice di partenza di un ColumnRange dev'essere <= dell'indice finale", 
            		                    HttpStatus.INTERNAL_SERVER_ERROR);
        
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
    }
}