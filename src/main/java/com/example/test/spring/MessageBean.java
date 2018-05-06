package com.example.test.spring;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Data provider bean.
 */
@SpringComponent
@UIScope
public class MessageBean {

    /**
     * Gets message data.
     *
     * @return a message
     */
    public String getMessage() {
        return "Not Clicked";
    }
}
