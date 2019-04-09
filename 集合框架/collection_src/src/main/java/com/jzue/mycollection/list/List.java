package com.jzue.mycollection.map.list;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public interface List<E> extends Collection<E>{

        @Override
        int size();

        @Override
        boolean isEmpty();

        @Override
        boolean contains(Object var1);

        @Override
        @NotNull Iterator<E> iterator();

        @Override
        @NotNull Object[] toArray();

        @Override
        boolean add(E var1);
        @Override
        boolean remove(Object var1);

        @Override
        boolean containsAll(Collection<?> var1);

        @Override
        boolean addAll(Collection<? extends E> var1);


        boolean addAll(int var1, Collection<? extends E> var2);

        @Override
        boolean removeAll(Collection<?> var1);

        @Override
        boolean retainAll(Collection<?> var1);

}
