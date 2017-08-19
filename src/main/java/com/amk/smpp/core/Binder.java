/*
 *      File: Binder.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Ago 18, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.core;

import javax.validation.constraints.NotNull;

import org.smpp.Session;
import org.smpp.SmppException;

import com.amk.smpp.operation.PDUOperation;

/**
 * Contract that defines the methods necessary to Bind the connection with SMSC.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Binder {

    /**
     * Create the link.
     * @param pduOperation Object containing the details of the operation.
     * @return The {@link Session} containing the connection to the SMSC.
     * @throws SmppException If there is an error.
     */
    Session bind(@NotNull PDUOperation pduOperation) throws SmppException;

    /**
     * Break the link with the SMSC.
     * @throws SmppException If there is an error.
     */
    void unBind() throws SmppException;
}
