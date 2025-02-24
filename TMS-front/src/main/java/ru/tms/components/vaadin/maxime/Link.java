package ru.tms.components.vaadin.maxime;

import com.vaadin.flow.component.html.Anchor;

/**
 * Created by se on 26/06/2017.
 */
public class Link extends Anchor {
    public Link(String text, String href) {
        super(href,text);
        setTitle(text);
    }
}