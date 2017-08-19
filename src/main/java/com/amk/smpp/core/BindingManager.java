/*
 *      File: BindingManager.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Ago 18, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.core;

import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.SmppException;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.UnbindResp;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.rules.PDUOperationsValidator;
import com.amk.smpp.util.SMPPUtil;

/**
 * Internal implementation of the {@link Binder}.<br/>
 * This class contains the logic necessary to bind connection to the SMCS.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class BindingManager implements Binder {
    /**
     * Logger for class.
     */
    private static final Logger      LOGGER             = LogManager.getLogger(AMKSmppFacade.class.getName());
    /**
     * Error msg.
     */
    private static final String      INVALID_CONNECTION = "[X] error, Connection null";
    /**
     * Indicates if there is a link whit the SMSC.
     */
    private              boolean     bound              = false;
    /**
     * Bind Request type.
     */
    private              BindRequest bindRequest        = null;
    /**
     * {@link Session} containing the connection to the SMSC.
     */
    private Session session;
    /**
     * Login information to the SMSC - User.
     */
    private String  systemId;
    /**
     * Login information to the SMSC - Pass.
     */
    private String  password;
    /**
     * The 'Address Range' parameter is used in the bind_receiver and bind_transceiver command to specify a set of SME
     * addresses serviced by the ESME client.
     */
    private String defaultAdressRange = Data.DFLT_ADDR_RANGE;
    /**
     * Identifies the type of ESME system requesting to bind as a transmitter with the MC.
     */
    private String systemType         = Data.DFLT_SYSTYPE;
    /**
     * Protocol version, 3.4 .
     */
    private byte   interfaceVersion   = (byte) 0x34;

    /**
     * Creates an instance of BindingManager.
     * Create an {@link Session} instance.
     * @param systemId User to log in with the SMSC.
     * @param password Password to log in with the SMSC.
     * @param connection Object with connection to SMSC.
     */
    public BindingManager(final String systemId, final String password, final Connection connection) {
        super();
        LOGGER.debug("BindingManager:");
        if (Objects.isNull(connection)) {
            throw new IllegalStateException(INVALID_CONNECTION);
        }
        this.systemId = systemId;
        this.password = password;
        this.session = new Session(connection);
    }

    /**
     * Validates the {@link PDUOperation} and defines the according {@link BindRequest} to perform the operation.
     * @param pduOperation Object containing the details of the operation.
     * @throws SmppException If there is an error.
     * @see PDUOperation
     */
    private void init(final PDUOperation pduOperation) throws SmppException {
        PDUOperationsValidator.validNotNull(pduOperation);
        bindRequest = SMPPUtil.defineBindingType(pduOperation);
        bindRequest.setSystemId(systemId);
        bindRequest.setPassword(password);
        bindRequest.setSystemType(systemType);
        bindRequest.setInterfaceVersion(interfaceVersion);
        bindRequest.setAddressRange(defaultAdressRange);
    }

    /**
     * Create the link.
     * @param pduOperation Object containing the details of the operation.
     * @return The {@link Session} containing the connection to the SMSC.
     * @throws SmppException If there is an error.
     */
    @CheckForNull
    @Override
    public Session bind(@NotNull final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug("bind:");
        final BindResponse response;
        final boolean asynchronous = pduOperation.isAsynchronous();
        init(pduOperation);

        if (bound) {
            LOGGER.warn(" Already bound, unbind first.");
            return session;
        }

        try {
            if (asynchronous) {
                LOGGER.debug("bind: ASYNC");
                if (Objects.isNull(pduOperation.getListener())) {
                    LOGGER.warn("[!] There is no Listener who receives the response asynchronously");
                }
                response = session.bind(bindRequest, pduOperation.getListener());
            } else {
                LOGGER.debug("bind: SYNC");
                response = session.bind(bindRequest);
            }
            LOGGER.debug("Bind response " + response.debugString());
            if (response.getCommandStatus() == Data.ESME_ROK) {
                bound = true;
            }
        } catch (final Exception e) {
            throw new SmppException(e);
        }

        if (response.getCommandStatus() == Data.ESME_ROK) {
            bound = true;
        }

        return session;
    }

    /**
     * Break the link with the SMSC.
     * @throws SmppException If there is an error.
     */
    @Override
    public void unBind() throws SmppException {
        try {
            if (!bound) {
                LOGGER.warn("Not bound, cannot unbind.");
                return;
            }
            // send the request
            LOGGER.debug("Going to unbind.");
            if (session.getReceiver().isReceiver()) {
                LOGGER.warn("It can take a while to stop the receiver.");
            }
            final UnbindResp response = session.unbind();
            LOGGER.info("Unbind response " + response.debugString());
            bound = false;
        } catch (final Exception e) {
            LOGGER.error("Unbind operation failed. " + e.getMessage());
            throw new SmppException("Unbind operation failed. " + e);
        }
    }

    /**
     * Getter for systemId.
     * @return systemId.
     **/
    public String getSystemId() {
        return systemId;
    }

    /**
     * Setter for systemId.
     * @param systemId expected.
     **/
    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    /**
     * Getter for password.
     * @return password.
     **/
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password.
     * @param password expected.
     **/
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Getter for session.
     * @return session.
     **/
    public Session getSession() {
        return session;
    }

    /**
     * Getter for bound.
     * @return bound.
     **/
    public boolean isBound() {
        return bound;
    }

}
