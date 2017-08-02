package com.amk.smpp;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.SmppException;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.UnbindResp;

import com.amk.smpp.rules.PDUOperationsValidator;
import com.amk.smpp.util.SMPPUtil;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class BindingManager {
    /**
     * Logger for class.
     */
    private static final Logger  LOGGER             = LogManager.getLogger(AMKSmppFacade.class.getName());
    /**
     * Error msg.
     */
    private static final String  INVALID_CONNECTION = "[X] error, Connection null";
    /**
     * todo
     */
    private              boolean bound              = false;


    private BindRequest bindRequest = null;
    private Session session;


    private String systemId;
    private String password;
    private String defaultAdressRange = Data.DFLT_ADDR_RANGE;
    private String systemType         = Data.DFLT_SYSTYPE;
    /**
     * Protocol version, 3.4 .
     */
    private byte   interfaceVersion   = (byte) 0x34;

    /**
     * Creates an instance of BindingManager.
     * TODO alguna descripción
     * @param systemId
     * @param password
     * @param pduOperation .
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

    private void init(final PDUOperation pduOperation) throws SmppException {
        PDUOperationsValidator.validNotNull(pduOperation);
        bindRequest = SMPPUtil.defineBindingType(pduOperation);
        bindRequest.setSystemId(systemId);
        bindRequest.setPassword(password);
        bindRequest.setSystemType(systemType);
        bindRequest.setInterfaceVersion(interfaceVersion);
        bindRequest.setAddressRange(defaultAdressRange);
    }

    public Session bind(final PDUOperation pduOperation) throws SmppException {
        final BindResponse response;
        init(pduOperation);
        if (bound) {
            LOGGER.warn(" Already bound, unbind first.");
            return session;
        }
        System.out.println("======================================>");
        // send the request
        try {
            response = session.bind(bindRequest);
        } catch (final Exception e) {
            throw new SmppException(e);
        }
        System.out.println("Bind response " + response.debugString());
        if (response.getCommandStatus() == Data.ESME_ROK) {
            bound = true;
        }
        return session;
    }

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
            UnbindResp response = session.unbind();
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
