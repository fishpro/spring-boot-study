package com.fishpro.xmldom4j.util;

import com.fishpro.xmldom4j.domain.Author;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class AuthorConverter implements SingleValueConverter {

    public String toString(Object obj) {
        return ((Author) obj).getName();
    }

    public Object fromString(String name) {
        return new Author(name);
    }

    public boolean canConvert(Class type) {
        return type.equals(Author.class);
    }

}