/*
 *      File: SessionFacade.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import org.smpp.SmppException;
import org.smpp.pdu.Response;

/**
 * Interface that defines the methods necessary to execute SMPP protocol operations. The idea is to encapsulate all
 * the methods of the Open API SMPP.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SmppWrapperFacade {
    /**
     * Executes the requested operation.
     * @param pduOperation requested Operation.
     * @return The SMCS response.
     * @throws SmppException If an error occurs when performing the operation.
     * @see Response
     * @see PDUOperation
     */
    Response executeOperation(PDUOperation pduOperation) throws SmppException;
}
