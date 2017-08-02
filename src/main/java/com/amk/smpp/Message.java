package com.amk.smpp;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
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
     * TODO alguna descripción
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @version 1.0.0
     * @param
     */
    public Message() {
        super();
    }

    /**
     * Creates an instance of Message.
     * TODO alguna descripción
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @version 1.0.0
     * @param
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
