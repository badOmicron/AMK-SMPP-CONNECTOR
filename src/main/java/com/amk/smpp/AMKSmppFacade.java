/*
 *      File: AMKSmppFacade.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.ServerPDUEvent;
import org.smpp.Session;
import org.smpp.SmppException;
import org.smpp.pdu.BindReceiver;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransciever;
import org.smpp.pdu.BindTransmitter;
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
import org.smpp.pdu.WrongDateFormatException;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.util.ByteBuffer;

import com.amk.smpp.rules.PDUOperationsValidator;
import com.amk.smpp.util.SmppUtil;

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
    private static final String INVALID_CONNECTION = "[X] error, Connection or Session invalid may be null";
    /**
     * Error msg.
     */
    private static final String INVALID_BIND_MODE  = "[X] Invalid bind mode, may is null. Operation canceled.";
    /**
     * SMCS Connection.
     */
    private Connection connection;
    /**
     * Session for SMPP communication.
     */
    private Session    session;

    private boolean bound = false;

    /**
     * Creates an instance of AMKSmppFacade.
     *
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param connection SMCS Connection.
     */
    public AMKSmppFacade(final Connection connection) {
        if (Objects.isNull(connection)) {
            LOGGER.error(INVALID_CONNECTION);
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
     * @param pduOperation requested Operation.
     * @return the SMCS response.
     * @see Response
     */
    @Override
    public Response executeOperation(final PDUOperation pduOperation) throws SmppException {
        init();
        PDUOperationsValidator.validNotNull(pduOperation);
        PDUOperationsValidator.validNotEmpty(pduOperation);
        bind(pduOperation);
        switch (pduOperation.getOperationType()) {
            case SUBMIT_SMS:
                LOGGER.debug("executeOperation: SUBMIT_SMS");
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
            case RECEIVE:
                receive(pduOperation, null);
                break;
            case ENQUIRE:
                break;
            default:
        }
        return null;
    }

    /**
     * Returns the request type according to the required bind type.
     * @param pduOperation Operation to be performed.
     * @return Correct request object.
     * @throws SmppException if the {@link PDUOperation} is null.
     */
    private BindRequest defineBindType(final PDUOperation pduOperation) throws SmppException {
        if (Objects.isNull(pduOperation.getBindType())) {
            throw new SmppException(INVALID_BIND_MODE);
        }
        switch (pduOperation.getBindType()) {
            case TX:
                return new BindTransmitter();
            case RX:
                return new BindReceiver();
            case TRX:
                return new BindTransciever();
            default:
                throw new SmppException(INVALID_BIND_MODE);
        }
    }

    //    public static void main(String[] args) {
    //        PDUOperation pduOperation = new PDUOperation();
    //        pduOperation.setMessageId(UUID.randomUUID().toString());
    //        pduOperation.setBindType(SmppBindTypes.RX);
    //        pduOperation.setSmsMessage("mi mensaje");
    //        pduOperation.setOperationType(PDUOperationTypes.SUBMIT_SMS);
    //        final PDUOperationProperties props = new PDUOperationProperties();
    ////        new PDUOperationPropertiesBuilder().build()
    //        props.setValidityPeriod("10");
    //        pduOperation.setOperationProps(props);
    //        PDUOperationsValidator.validSubmit(pduOperation);
    //    }

    private void bind(final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug("bind: ");
        if (bound) {
            System.out.println("Already bound, unbind first.");
            return;
        }
        BindResponse response = null;
        BindRequest bindRequest = defineBindType(pduOperation);
        bindRequest.setSystemId("hugo");
        bindRequest.setPassword("ggoohu");
        bindRequest.setSystemType(Data.DFLT_SYSTYPE);
        bindRequest.setInterfaceVersion((byte) 0x34);
        bindRequest.setAddressRange("2");
        // send the request
        try {
            response = session.bind(bindRequest);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        System.out.println("Bind response " + response.debugString());
        if (response.getCommandStatus() == Data.ESME_ROK) {
            bound = true;
        }

    }


    /**
     * Creates a new instance of <code>SubmitSM</code> class, lets you set
     * subset of fields of it. This PDU is used to send SMS message
     * to a device.
     *
     * See "SMPP Protocol Specification 3.4, 4.4 SUBMIT_SM Operation."
     * @param pduOperation - .
     * @throws SmppException - .
     * @see Session#submit(SubmitSM)
     * @see SubmitSM
     * @see SubmitSMResp
     * @return response of the SMCS.
     * */
    private < E extends Response > E submit(final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug("executeOperation: submit");
        SubmitSMResp response = new SubmitSMResp();
        final SubmitSM request;
        try {
            request = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            assert request != null;
            request.setShortMessage(pduOperation.getSmsMessage());
            request.assignSequenceNumber(true);
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
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
     * subset of fields of it. This PDU is used to send SMS message
     * to multiple devices.
     *
     * See "SMPP Protocol Specification 3.4, 4.5 SUBMIT_MULTI Operation."
     * @see Session#submitMulti(SubmitMultiSM)
     * @see SubmitMultiSM
     * @see SubmitMultiSMResp
     */
    private < E extends Response > E submitMulti(final PDUOperation pduOperation) throws SmppException {
        LOGGER.debug(this.getClass().getName() + ".submitMulti()");
        SubmitMultiSMResp response = null;
        final SubmitMultiSM requestMulti;
        try {
            requestMulti = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            assert requestMulti != null;
            requestMulti.setShortMessage(pduOperation.getSmsMessage());
            // send the request
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
                session.submitMulti(requestMulti);
            } else {
                response = session.submitMulti(requestMulti);
                System.out.println("Submit Multi response " + response.debugString());
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
     * @param pduOperation
     * @param <E>
     * @return
     * @throws SmppException .
     * @see Session#data(DataSM)
     * @see DataSM
     * @see DataSMResp
     */
    private < E extends Response > E data(final PDUOperation pduOperation) throws SmppException {
        DataSMResp response = null;
        try {
            final DataSM requestData = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            // send the request
            assert requestData != null;
            requestData.setAlertOnMsgDelivery(true);
            requestData.setMessagePayload(new ByteBuffer(pduOperation.getSmsMessage().getBytes()));
            // send the request
            final boolean asynchronous = pduOperation.isAsynchronous();
            if (asynchronous) {
                session.data(requestData);
            } else {
                response = session.data(requestData);
            }
        } catch (Exception e) {
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
     *
     * @param pduOperation -
     * @param <E> -
     * @return
     * @throws SmppException
     * See "SMPP Protocol Specification 3.4, 4.8 QUERY_SM Operation."
     * @see Session#query(QuerySM)
     * @see QuerySM
     * @see QuerySMResp
     */
    private < E extends Response > E query(final PDUOperation pduOperation) throws SmppException {
        QuerySMResp response = null;
        try {
            final QuerySM requestQuery = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            // set values
            assert requestQuery != null;
            requestQuery.setMessageId(pduOperation.getMessageId());
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
     *
     * @param pduOperation
     * @param <E>
     * @return
     * @throws SmppException
     */
    private < E extends Response > E replace(final PDUOperation pduOperation) throws SmppException {
        final ReplaceSM request;
        ReplaceSMResp response = null;
        try {
            request = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            assert request != null;
            request.setMessageId(pduOperation.getMessageId());
            request.setShortMessage(pduOperation.getSmsMessage());
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
     *
     * @param pduOperation
     * @param <E>
     * @return
     * @throws SmppException
     */
    private < E extends Response > E cancel(final PDUOperation pduOperation) throws SmppException {
        final CancelSM request;
        CancelSMResp response = null;
        try {
            request = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            assert request != null;
            request.setMessageId(pduOperation.getMessageId());
            // send the request
            if (pduOperation.isAsynchronous()) {
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
     * TODO -- Es necesario revisar la lógica de este procedimiento.
     * @param pduOperation
     * @param pduListener
     * @throws SmppException
     */
    private void receive(final PDUOperation pduOperation, final PDUListener pduListener) throws SmppException {
        long receiveTimeout = pduOperation.getOperationProps().getReceiveTimeout();
        if (receiveTimeout < 0) {
            receiveTimeout = Data.RECEIVE_BLOCKING;
        }
        try {
            PDU pdu = null;
            final ServerPDUEvent pduEvent;
            LOGGER.debug("Going to receive a PDU. ");
            if (receiveTimeout == Data.RECEIVE_BLOCKING) {
                LOGGER.debug("The receive is blocking, i.e. the application " + "will stop until a PDU will be received.");
            } else {
                LOGGER.info("The receive timeout is " + receiveTimeout / 1000 + " sec.");
            }
            if (pduOperation.isAsynchronous()) {
                pduEvent = pduListener.getRequestEvent();
                if (pduEvent != null) {
                    pdu = pduEvent.getPDU();
                }
            } else {
                pdu = session.receive(receiveTimeout);
            }
            if (pdu != null) {
                LOGGER.debug("Received PDU " + pdu.debugString());
                if (pdu.isRequest()) {
                    final Response response = ((Request) pdu).getResponse();
                    // respond with default response
                    LOGGER.debug("Going to send default response to request " + response.debugString());
                    session.respond(response);
                }
            } else {
                LOGGER.debug("No PDU received this time.");
            }
        } catch (Exception e) {
            LOGGER.error("Receiving failed. " + e);
        }
    }

    /**
     *
     * @param pduOperation
     * @param pduListener
     * @return
     * @throws SmppException
     */
    private Response enquireLink(final PDUOperation pduOperation, final PDUListener pduListener) throws SmppException {
        final EnquireLink request;
        EnquireLinkResp response = null;
        try {
            request = setRequestProps(pduOperation.getOperationType(), pduOperation.getOperationProps());
            assert request != null;
            request.setData(new ByteBuffer(pduOperation.getSmsMessage().getBytes()));
            response = new EnquireLinkResp();
            if (pduOperation.isAsynchronous()) {
                session.enquireLink(request);
            } else {
                response = session.enquireLink(request);
                LOGGER.debug("Enquire Link response " + response.debugString());
            }
        } catch (final Exception e) {
            LOGGER.error("Enquire Link operation failed. " + e);
        }
        return response;
    }

    /**
     *  Build the request.
     * @param pPDUOperationType - TODO
     * @param props Object with the properties of the message.
     * @return The request with its properties.S
     * @throws WrongLengthOfStringException If any value has exceeds the length.
     * @throws WrongDateFormatException If any value does not comply with the format.
     */
    private < E extends Request > E setRequestProps(final PDUOperationTypes pPDUOperationType, final
    PDUOperationProperties props) throws WrongLengthOfStringException, WrongDateFormatException {
        LOGGER.debug("setRequestProps: ");
        switch (pPDUOperationType) {
            case SUBMIT_SMS:
                LOGGER.debug(" setRequestProps: SUBMIT_SMS ");
                final SubmitSM request = new SubmitSM();
                // set values
                request.setServiceType(props.getServiceType());
                request.setSourceAddr(props.getSourceAddress());
                Arrays.asList(props.getDestAddress()).forEach(request::setDestAddr);
                request.setReplaceIfPresentFlag(props.getReplaceIfPresentFlag());
//                request.setScheduleDeliveryTime(SmppUtil.transformDate(props.getScheduleDeliveryTime()));
                request.setValidityPeriod(props.getValidityPeriod());
                request.setEsmClass(props.getEsmClass());
                request.setProtocolId(props.getProtocolId());
                request.setPriorityFlag(props.getPriorityFlag());
                request.setRegisteredDelivery(props.getRegisteredDelivery());
                request.setDataCoding(props.getDataCoding());
                request.setSmDefaultMsgId(props.getSmDefaultMsgId());
                return (E) request;
            case SUBMIT_SMS_MULTI:
                final SubmitMultiSM requestMulti = new SubmitMultiSM();
                // set other values
                requestMulti.setServiceType(props.getServiceType());
                requestMulti.setSourceAddr(props.getSourceAddress());
                requestMulti.setReplaceIfPresentFlag(props.getReplaceIfPresentFlag());
                requestMulti.setScheduleDeliveryTime(SmppUtil.transformDate(props.getScheduleDeliveryTime()));
                requestMulti.setValidityPeriod(props.getValidityPeriod());
                requestMulti.setEsmClass(props.getEsmClass());
                requestMulti.setProtocolId(props.getProtocolId());
                requestMulti.setPriorityFlag(props.getPriorityFlag());
                requestMulti.setRegisteredDelivery(props.getRegisteredDelivery());
                requestMulti.setDataCoding(props.getDataCoding());
                requestMulti.setSmDefaultMsgId(props.getSmDefaultMsgId());
                return (E) requestMulti;
            case DATA:
                final DataSM requestData = new DataSM();
                // set values
                requestData.setServiceType(props.getServiceType());
                requestData.setSourceAddr(props.getSourceAddress());
                Arrays.asList(props.getDestAddress()).forEach(requestData::setDestAddr);
                requestData.setEsmClass(props.getEsmClass());
                requestData.setRegisteredDelivery(props.getRegisteredDelivery());
                requestData.setDataCoding(props.getDataCoding());
                return (E) requestData;
            case QUERY:
                final QuerySM querySM = new QuerySM();
                querySM.setSourceAddr(props.getSourceAddress());
                return (E) querySM;
            case REPLACE:
                final ReplaceSM requestReplace = new ReplaceSM();
                // set values
                requestReplace.setSourceAddr(props.getSourceAddress());
                requestReplace.setScheduleDeliveryTime(SmppUtil.transformDate(props.getScheduleDeliveryTime()));
                requestReplace.setValidityPeriod(props.getValidityPeriod());
                requestReplace.setRegisteredDelivery(props.getRegisteredDelivery());
                requestReplace.setSmDefaultMsgId(props.getSmDefaultMsgId());
                return (E) requestReplace;
            case CANCEL:
                CancelSM requestCancel = new CancelSM();
                requestCancel.setServiceType(props.getServiceType());
                requestCancel.setSourceAddr(props.getSourceAddress());
                Arrays.asList(props.getDestAddress()).forEach(requestCancel::setDestAddr);
                return (E) requestCancel;
        }
        return null;
    }


}
