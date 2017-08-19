/*
 *      File: MyPDUListener.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 20, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.util;

import org.smpp.ServerPDUEvent;
import org.smpp.pdu.PDU;
import org.smpp.util.Queue;

/**
 * Basic Observer to receive the responses of the asynchronous operations made in the protocol.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class BasicPDUListener extends PDUListener {

    /**
     * Data structure (Queue) that stores Request events.
     */
    private final Queue requestEvents   = new Queue();
    /**
     * Data structure (Queue) that stores Response events.
     */
    private final Queue responsetEvents = new Queue();

    /**
     * Creates an instance of BasicPDUListener.
     * Set the session to the abstract listener.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     */
    public BasicPDUListener() {
        // just create an instance
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
        final PDU pdu = event.getPDU();
        if (pdu.isRequest()) {
            LOGGER.debug("async request received, enqueuing " + pdu.debugString());
            synchronized (requestEvents) {
                requestEvents.enqueue(event);
            }
        } else if (pdu.isResponse()) {
            LOGGER.debug("async response received, enqueuing " + pdu.debugString());
            synchronized (responsetEvents) {
                responsetEvents.enqueue(event);
            }
        } else {
            LOGGER.warn("[!] pdu of unknown class (not request nor response) received, discarding " + pdu.debugString());
        }
    }

    /**
     * Returns received pdu from the queue. If the queue is empty,
     * the method blocks for the specified timeout.
     */
    @Override
    public ServerPDUEvent getRequestEvent() {
        ServerPDUEvent pduEvent = null;
        synchronized (requestEvents) {
            LOGGER.debug("getRequestEvent: Waiting for some event " + super.getIntervalTime() / 1000 + " seconds.");
            waitEvent(requestEvents);
            if (!requestEvents.isEmpty()) {
                pduEvent = (ServerPDUEvent) requestEvents.dequeue();
            }
        }
        return pduEvent;
    }

    @Override
    public ServerPDUEvent getResponseEvent() {
        ServerPDUEvent pduEvent = null;
        synchronized (responsetEvents) {
            LOGGER.debug("getResponseEvent: Waiting for some event " + super.getIntervalTime() / 1000 + " seconds.");
            waitEvent(responsetEvents);
            if (!responsetEvents.isEmpty()) {
                pduEvent = (ServerPDUEvent) responsetEvents.dequeue();
            }
        }
        return pduEvent;
    }

    /**
     * Expect events.
     * @param eventQueue Data structure (Queue) that expects events.
     */
    private synchronized void waitEvent(final Queue eventQueue) {
        while (eventQueue.isEmpty()) {
            try {
                eventQueue.wait(super.getIntervalTime());
            } catch (final InterruptedException e) {
                LOGGER.error("[X] error " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
