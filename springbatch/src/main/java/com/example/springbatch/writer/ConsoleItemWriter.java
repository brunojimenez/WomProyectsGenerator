package com.example.springbatch.writer;

import com.example.springbatch.model.Csv;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import javax.batch.api.chunk.AbstractItemWriter;

import java.util.List;

public class ConsoleItemWriter<T> implements ItemWriter<T>{


    @Override
    public void write(List<? extends T> list) throws Exception {
        for (T item : list) {
            Csv v = (Csv)item;
            System.out.println("id=" + ((Csv) item).getId());
            System.out.println("nombre=" + ((Csv) item).getNombre());
            System.out.println("**************************************");
        }
    }

}
