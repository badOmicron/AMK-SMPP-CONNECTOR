/*
 *      File: PDUListener.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.ServerPDUEvent;
import org.smpp.ServerPDUEventListener;
import org.smpp.Session;
import org.smpp.SmppObject;

import com.amk.smpp.rules.PDUOperationsValidator;

/**
 * Implements simple PDU listener which handles PDUs received from SMSC.
 * Abstract class that serves as a listener for the events that the Session receives linked to an SMCS.
 * This class is used when it is decided to make asynchronous requests to the SMCS and the immediate response is not expected.
 * You need to extend it and inject the session that you want to observe.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class PDUListener extends SmppObject implements ServerPDUEventListener {
    /**
     * Logger for class.
     */
    Logger LOGGER = LogManager.getLogger(PDUOperationsValidator.class.getName());
    /**
     * Error msg.
     */
    private static final String INVALID_CONNECTION = "[X] error, Connection or Session invalid, may be null";
    /**
     * Session for sMPP communication.
     */
    private Session session;

    /**
     * Creates an instance of PDUListener.
     *
     * @param session  provides all methods necessary for communication with SMSC using SMPP protocol.
     * */
    protected PDUListener(final Session session) {
        if (Objects.isNull(session) || Objects.isNull(session.getConnection())) {
            LOGGER.error(INVALID_CONNECTION);
            throw new IllegalStateException(INVALID_CONNECTION);
        }
        this.session = session;
    }

    /**
     * Returns received pdu.
     * Optional. It depends on the control given to the event, e.g. Store events in a queue.
     */
    public abstract ServerPDUEvent getRequestEvent();
}
