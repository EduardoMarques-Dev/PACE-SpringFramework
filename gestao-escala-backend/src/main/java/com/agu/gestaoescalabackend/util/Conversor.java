package com.agu.gestaoescalabackend.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Conversor {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T converter(S origem, Class<T> destino) {
        return modelMapper.map(origem, destino);
    }

    public static <S, T> List<T> converterLista(List<S> origem, Class<T> destino) {
        return origem
                .stream()
                .map(element -> modelMapper.map(origem, destino))
                .collect(Collectors.toList());
    }

}
