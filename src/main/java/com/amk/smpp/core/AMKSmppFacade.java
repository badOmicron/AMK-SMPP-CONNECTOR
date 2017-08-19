/*
 *      File: AMKSmppFacade.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.ServerPDUEvent;
import org.smpp.Session;
import org.smpp.SmppException;
import org.smpp.pdu.CancelSM;
import org.smpp.pdu.CancelSMResp;
import org.smpp.pdu.DataSM;
import org.smpp.pdu.DataSMResp;
import org.smpp.pdu.EnquireLink;
import org.smpp.pdu.EnquireLinkResp;
import org.smpp.pdu.PDU;
import org.smpp.pdu.QuerySM;
import org.smpp.pdu.QuerySMResp;
import org.smpp.pdu.ReplaceSM;
import org.smpp.pdu.ReplaceSMResp;
import org.smpp.pdu.Request;
import org.smpp.pdu.Response;
import org.smpp.pdu.SubmitMultiSM;
import org.smpp.pdu.SubmitMultiSMResp;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.smpp.util.ByteBuffer;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationTypes;
import com.amk.smpp.rules.PDUOperationsValidator;
import com.amk.smpp.util.OperationPropertiesUtil;

/**
 * Internal implementation of the {@link SmppWrapperFacade}.<br/>
 * This class contains the logic necessary to execute the operations that are requested. It is necessary to inject
 * the connection object.
 * <b>Pattern: </b> <code>WrapperFacade.</code>
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class AMKSmppFacade implements SmppWrapperFacade {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER             = LogManager.getLogger(AMKSmppFacade.class.getName());
    /**
     * Error msg.
     */
    private static final String INVALID_CONNECTION = "[X] error, Connection null";
    /**
     * SMCS Connection.
     */
    private Connection     connection;
    /**
     * Session for SMPP communication.
     */
    private Session        session;
    /**
     * Provides us the link with the SMSC.
     */
    private BindingManager bindingManager;

    /**
     * Creates an instance of AMKSmppFacade.
     *
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param connection SMCS Connection.
     */
    public AMKSmppFacade(final Connection connection) {
        if (Objects.isNull(connection)) {
            LOGGER.warn(INVALID_CONNECTION);
            throw new IllegalStateException(INVALID_CONNECTION);
        }
        this.connection = connection;
    }

    /**
     * init the session.
     */
    private void init() {
        LOGGER.debug("init: ");
        if (Objects.isNull(session)) {
            LOGGER.debug("init: Session");
            this.session = new Session(this.connection);
        }
    }

    /**
     * Executes the requested operation.
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation requested Operation.
     * @return the SMCS response.
     * @see Response
     */
    @Override
    public < E extends Response > E executeOperation(final PDUOperation pduOperation) throws SmppException {
        init();
        PDUOperationsValidator.validNotNull(pduOperation);
        PDUOperationsValidator.validNotEmpty(pduOperation);
        PDUOperationsValidator.validNotNull(bindingManager);
        bind(pduOperation);
        LOGGER.debug("executeOperation: " + pduOperation.getOperationType());
        switch (pduOperation.getOperationType()) {
            case SUBMIT_SMS:
                return submit(pduOperation);
            case SUBMIT_SMS_MULTI:
                return submitMulti(pduOperation);
            case DATA:
                return data(pduOperation);
            case QUERY:
                return query(pduOperation);
            case REPLACE:
                return replace(pduOperation);
            case CANCEL:
                return cancel(pduOperation);
            case ENQUIRE:
                return (E) enquireLink(pduOperation);
            default:
                throw new IllegalArgumentException("Esa operacion no es permitida, usa el metodo receiveOperation(final PDUOperation pduOperation)");
        }
    }

    /**
     * Executes the requested operation.
     * @param <E> Classes that inherit from {@link Request}.
     * @param pduOperation requested Operation.
     * @return The SMCS response.
     * @throws SmppException If an error occurs when performing the operation.
     * @see Request
     * @see PDUOperation
     */
    @Override
    public < E extends Request > E receiveOperation(final PDUOperation pduOperation) throws SmppException {
        final Request request;
        init();
        PDUOperationsValidator.validNotNull(pduOperation);
        PDUOperationsValidator.validNotEmpty(pduOperation);
        PDUOperationsValidator.validNotNull(bindingManager);
        bind(pduOperation);

        LOGGER.debug("executeOperation: " + pduOperation.getOperationType());
        if (PDUOperationTypes.RECEIVE.equals(pduOperation.getOperationType())) {
            request = receive(pduOperation);
        } else {
            throw new IllegalArgumentException("Esa operacion no es permitida, usa el metodo executeOperation(final PDUOperation pduOperation)");
        }
        return (E) request;
    }

    /**
     * Search for link with SMSC.
     * @param pduOperation You need to know the type of operation and the bind type.
     * @throws SmppException If there is an error.
     */
    @CheckForNull
    private void bind(@NotNull final PDUOperation pduOperation) throws SmppException {
        this.session = this.bindingManager.bind(pduOperation);
    }

    /**
     * Creates a new instance of <code>SubmitSM</code> class, lets you set
     * subset of fields of it. This PDU is used to send SMS message to a device.
     * See "SMPP Protocol Specification 3.4, 4.4 SUBMIT_SM Operation."
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @throws SmppException If there is an error.
     * @see PDUOperation
     * @see Session#submit(SubmitSM)
     * @see SubmitSM
     * @see SubmitSMResp
     * @return response of the SMCS -> {@link SubmitSMResp}.
     * */
    @CheckForNull
    private < E extends Response > E submit(@NotNull final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug("executeOperation: submit");
        SubmitSMResp response = null;
        try {
            final SubmitSM request = OperationPropertiesUtil.setRequestProps(new SubmitSM(), pduOperation.getOperationProps());
            request.setShortMessage(pduOperation.getSmsMessage().getBody());
            request.assignSequenceNumber(true);
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
                LOGGER.debug("submit: async");
                session.submit(request);
            } else {
                response = session.submit(request);
            }
        } catch (final Exception e) {
            throw new SmppException(e);
        }
        return (E) response;
    }

    /**
     * Creates a new instance of <code>SubmitMultiSM</code> class, lets you set
     * subset of fields of it. This PDU is used to send SMS message to multiple devices.
     * See "SMPP Protocol Specification 3.4, 4.5 SUBMIT_MULTI Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link SubmitMultiSMResp}.
     * @throws SmppException If there is an error.
     * @see PDUOperation
     * @see Session#submitMulti(SubmitMultiSM)
     * @see SubmitMultiSM
     * @see SubmitMultiSMResp
     * */
    private < E extends Response > E submitMulti(final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug(this.getClass().getName() + ".submitMulti()");
        SubmitMultiSMResp response = null;
        try {
            final SubmitMultiSM requestMulti = OperationPropertiesUtil.setRequestProps(new SubmitMultiSM(), pduOperation
                    .getOperationProps());
            assert requestMulti != null;
            requestMulti.setShortMessage(pduOperation.getSmsMessage().getBody());
            LOGGER.debug("submitMulti: DestAdresses: " + requestMulti.getNumberOfDests());
            // send the request
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
                session.submitMulti(requestMulti);
            } else {
                response = session.submitMulti(requestMulti);
            }
        } catch (final Exception e) {
            throw new SmppException("Submit Multi operation failed. ", e);
        }
        return (E) response;
    }

    /**
     * Creates a new instance of <code>DataSM</code> class, lets you set subset of fields of it. This PDU is an
     * alternative to the <code>SubmitSM</code> and <code>DeliverSM</code>. It delivers the data to the specified
     * device.
     * See "SMPP Protocol Specification 3.4, 4.7 DATA_SM Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link DataSMResp}.
     * @throws SmppException If there is an error.
     * @see PDUOperation
     * @see Session#data(DataSM)
     * @see DataSM
     * @see DataSMResp
     */
    private < E extends Response > E data(final PDUOperation pduOperation) throws SmppException {
        DataSMResp response = null;
        try {
            final DataSM requestData = OperationPropertiesUtil.setRequestProps(new DataSM(), pduOperation.getOperationProps());
            // send the request
            assert requestData != null;
            requestData.setAlertOnMsgDelivery(true);
            requestData.setMessagePayload(
                    new ByteBuffer(pduOperation.getSmsMessage()
                            .getBody()
                            .getBytes(Charset.forName(StandardCharsets.UTF_8.name())))
            );
            // send the request
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
                session.data(requestData);
            } else {
                response = session.data(requestData);
            }
        } catch (final Exception e) {
            throw new SmppException("Submit Data operation failed. ", e);
        }
        return (E) response;
    }

    /**
     * Creates a new instance of <code>QuerySM</code> class, lets you set
     * subset of fields of it. This PDU is used to fetch information
     * about status of already submitted message providing that you 'remember'
     * message id of the submitted message. The message id is assigned
     * by SMSC and is returned to you with the response to the submision
     * PDU (SubmitSM, DataSM etc.).
     * See "SMPP Protocol Specification 3.4, 4.8 QUERY_SM Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link QuerySMResp}.
     * @throws SmppException If there is an error.
     * @see Session#query(QuerySM)
     * @see QuerySM
     * @see QuerySMResp
     */
    private < E extends Response > E query(final PDUOperation pduOperation) throws SmppException {
        QuerySMResp response = null;
        try {
            final QuerySM requestQuery = OperationPropertiesUtil.setRequestProps(new QuerySM(), pduOperation.getOperationProps());
            // set values
            assert requestQuery != null;
            requestQuery.setMessageId(pduOperation.getSmsMessage().getId());
            System.out.println(requestQuery.debugString());
            // send the request
            if (pduOperation.isAsynchronous()) {
                session.query(requestQuery);
            } else {
                response = session.query(requestQuery);
            }
        } catch (final Exception e) {
            throw new SmppException("Query operation failed. ", e);
        }
        return (E) response;
    }

    /**
     * * Creates a new instance of <code>ReplaceSM</code> class, lets you set
     * subset of fields of it. This PDU is used to replace certain
     * attributes of already submitted message providing that you 'remember'
     * message id of the submitted message. The message id is assigned
     * by SMSC and is returned to you with the response to the submision
     * PDU (SubmitSM, DataSM etc.).
     * See "SMPP Protocol Specification 3.4, 4.10 REPLACE_SM Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link ReplaceSMResp}.
     * @throws SmppException If there is an error.
     * @see Session#replace(ReplaceSM)
     * @see ReplaceSM
     * @see ReplaceSMResp
     */
    private < E extends Response > E replace(final PDUOperation pduOperation) throws SmppException {
        ReplaceSMResp response = null;
        try {
            final ReplaceSM request = OperationPropertiesUtil.setRequestProps(new ReplaceSM(), pduOperation.getOperationProps());
            assert request != null;
            request.setMessageId(pduOperation.getSmsMessage().getId());
            request.setShortMessage(pduOperation.getSmsMessage().getBody());
            // send the request
            if (pduOperation.isAsynchronous()) {
                session.replace(request);
            } else {
                response = session.replace(request);
            }
        } catch (final Exception e) {
            throw new SmppException("Replace operation failed. ", e);
        }
        return (E) response;
    }

    /**
     * * Creates a new instance of <code>CancelSM</code> class, lets you set
     * subset of fields of it. This PDU is used to cancel an already
     * submitted message. You can only cancel a message which haven't been
     * delivered to the device yet.
     * See "SMPP Protocol Specification 3.4, 4.9 CANCEL_SM Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link CancelSMResp}.
     * @throws SmppException If there is an error.
     * @see Session#cancel(CancelSM)
     * @see CancelSM
     * @see CancelSMResp
     */
    private < E extends Response > E cancel(final PDUOperation pduOperation) throws SmppException {
        CancelSMResp response = null;
        try {
            final CancelSM request = OperationPropertiesUtil.setRequestProps(new CancelSM(), pduOperation.getOperationProps());
            request.setMessageId(pduOperation.getSmsMessage().getId());
            // send the request
            if (pduOperation.isAsynchronous()) {
                LOGGER.debug("cancel: ASYNC");
                session.cancel(request);
            } else {
                response = session.cancel(request);
            }
        } catch (final Exception e) {
            throw new SmppException("Cancel operation failed. ", e);
        }
        return (E) response;
    }

    /**
     * Receives one PDU of any type from SMSC.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Request}.
     * @param pduOperation Object containing the details of the operation.
     * @return request of the SMCS -> {@link Request}.
     * @throws SmppException If there is an error.
     * @see Session#receive()
     * @see PDU
     */
    @CheckForNull
    private < E extends Request > E receive(@Nonnull final PDUOperation pduOperation) throws SmppException {
        final Response response;
        PDU pdu = null;
        long receiveTimeout = (Objects.isNull(pduOperation.getListener())) ? Data.RECEIVE_BLOCKING : pduOperation.getListener().getIntervalTime();
        if (receiveTimeout < 0) {
            receiveTimeout = Data.RECEIVE_BLOCKING;
        }
        LOGGER.debug("receive: receiveTimeout : " + receiveTimeout + " miliseconds");
        try {
            final ServerPDUEvent pduEvent;
            LOGGER.debug("Going to receive a PDU. ");
            if (receiveTimeout == Data.RECEIVE_BLOCKING) {
                LOGGER.warn("The receive is blocking, i.e. the application will stop until a PDU will be received.");
            } else {
                LOGGER.info("The receive timeout is " + receiveTimeout / 1000 + " sec.");
            }
            if (pduOperation.isAsynchronous()) {
                LOGGER.debug("receive: ASYNC");
                pduEvent = pduOperation.getListener().getRequestEvent();
                if (pduEvent != null) {
                    pdu = pduEvent.getPDU();
                }
            } else {
                pdu = session.receive(receiveTimeout);
            }
            if (pdu != null) {
                LOGGER.debug("Received PDU " + pdu.debugString());
                if (pdu.isRequest()) {
                    response = ((Request) pdu).getResponse();
                    // respond with default response
                    LOGGER.debug("Going to send default response to request " + response.debugString());
                    session.respond(response);
                    response.setBody(pdu.getBody());
                    response.setData(pdu.getData());
                }
            } else {
                LOGGER.debug("No PDU received this time.");
            }
        } catch (final Exception e) {
            LOGGER.error("Receiving failed. " + e);
            throw new SmppException("Receiving failed. " + e);
        }
        return (E) pdu;
    }

    /**
     * Creates a new instance of <code>EnquireSM</code> class.
     * This PDU is used to check that application level of the other party
     * is alive. It can be sent both by SMSC and ESME.
     * See "SMPP Protocol Specification 3.4, 4.11 ENQUIRE_LINK Operation."
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param <E> Classes that inherit from {@link Response}.
     * @param pduOperation Object containing the details of the operation.
     * @return response of the SMCS -> {@link EnquireLinkResp}.
     * @throws SmppException If there is an error.
     * @see Session#enquireLink(EnquireLink)
     * @see EnquireLink
     * @see EnquireLinkResp
     */
    private < E extends Response > E enquireLink(final PDUOperation pduOperation) throws SmppException {
        EnquireLinkResp response;
        try {
            final EnquireLink request = new EnquireLink();
            request.setData(
                    new ByteBuffer(pduOperation
                            .getSmsMessage()
                            .getBody()
                            .getBytes(Charset.forName(StandardCharsets.UTF_8.name())))
            );
            response = new EnquireLinkResp();
            if (pduOperation.isAsynchronous()) {
                session.enquireLink(request);
            } else {
                response = session.enquireLink(request);
                LOGGER.debug("Enquire Link response " + response.debugString());
            }
        } catch (final Exception e) {
            LOGGER.error("Enquire Link operation failed. " + e);
            throw new SmppException(e);
        }
        return (E) response;
    }

    /**
     * Getter for bindingManager.
     * @return bindingManager.
     **/
    public BindingManager getBindingManager() {
        return bindingManager;
    }

    /**
     * Setter for bindingManager.
     * @param bindingManager expected.
     **/
    public void setBindingManager(final BindingManager bindingManager) {
        this.bindingManager = bindingManager;
    }

}
