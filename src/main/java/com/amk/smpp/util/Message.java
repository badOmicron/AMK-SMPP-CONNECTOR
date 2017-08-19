/*
 *      File: Message.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Ago 18, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.util;

/**
 * Bean that represents an SMS.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class Message {

    /**
     * ID of the submitted message. It may be used at a later stage to perform subsequent operations on the message.
     */
    private String id;
    /**
     * Message body.
     */
    private String body;

    /**
     * Creates an instance of Message.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     */
    public Message() {
        super();
    }

    /**
     * Creates an instance of Message.
     * Sets the id and body message.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param id message Id.
     * @param body message body.
     */
    public Message(final String id, final String body) {
        this.id = id;
        this.body = body;
    }

    /**
     * Getter for id.
     * @return id.
     **/
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     * @param id expected.
     **/
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Getter for body.
     * @return body.
     **/
    public String getBody() {
        return body;
    }

    /**
     * Setter for body.
     * @param body expected.
     **/
    public void setBody(final String body) {
        this.body = body;
    }

}
