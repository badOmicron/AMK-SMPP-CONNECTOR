/*
 *      File: PDUListener.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.util;

import static org.apache.logging.log4j.LogManager.getLogger;

import org.apache.logging.log4j.Logger;
import org.smpp.ServerPDUEvent;
import org.smpp.ServerPDUEventListener;
import org.smpp.SmppObject;

import com.amk.smpp.core.AMKSmppFacade;

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
    static final Logger LOGGER = getLogger(AMKSmppFacade.class.getName());

    /**
     * Error msg.
     */
    private static final String INVALID_CONNECTION = "[X] error, Connection or Session invalid, may be null";

    /**
     * Waiting time used by the observer before turning to see if there are any events.
     */
    private long intervalTime = 0;


    /**
     * Returns received request pdu.
     * Optional. It depends on the control given to the event, e.g. Store events in a queue.
     * @return some {@link ServerPDUEvent}.
     */
    public abstract ServerPDUEvent getRequestEvent();

    /**
     * Returns received response pdu.
     * Optional. It depends on the control given to the event, e.g. Store events in a queue.
     * @return some {@link ServerPDUEvent}.
     */
    public abstract ServerPDUEvent getResponseEvent();

    /**
     * Getter for intervalTime.
     * @return intervalTime.
     **/
    public long getIntervalTime() {
        return intervalTime;
    }

    /**
     * Setter for intervalTime.
     * @param intervalTime expected.
     **/
    public void setIntervalTime(final long intervalTime) {
        this.intervalTime = intervalTime;
    }
}
