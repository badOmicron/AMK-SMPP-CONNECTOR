/*
 *      File: MyPDUListener.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 20, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import org.smpp.ServerPDUEvent;
import org.smpp.Session;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class MyPDUListener extends PDUListener {

    /**W
     * Creates an instance of MyPDUListener.
     * Set the session to the abstract listener.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param session SMPP Session.
     */
    public MyPDUListener(final Session session) {
        super(session);
    }

    /**
     * Meant to process PDUs received from the SMSC.
     * This method is called by the <code>Receiver</code> whenever a
     * PDU is received from the SMSC.
     * @param event
    the event received from the SMSC
     */
    @Override
    public void handleEvent(final ServerPDUEvent event) {
        //
    }

    /**
     * Returns received pdu from the queue. If the queue is empty,
     * the method blocks for the specified timeout.
     */
    @Override
    public ServerPDUEvent getRequestEvent() {
        return null;
    }
}
