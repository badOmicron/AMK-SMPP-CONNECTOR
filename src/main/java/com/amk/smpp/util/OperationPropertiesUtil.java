/*
 *      File: OperationPropertiesUtil.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Ago 18, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */
package com.amk.smpp.util;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.pdu.CancelSM;
import org.smpp.pdu.DataSM;
import org.smpp.pdu.DestinationAddress;
import org.smpp.pdu.QuerySM;
import org.smpp.pdu.ReplaceSM;
import org.smpp.pdu.SubmitMultiSM;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.TooManyValuesException;
import org.smpp.pdu.WrongDateFormatException;
import org.smpp.pdu.WrongLengthOfStringException;

import com.amk.smpp.operation.PDUOperationProperties;

/**
 * Utility class for the construction of the properties of the operation.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class OperationPropertiesUtil {
    /**
     * Logger for class.
     */
    private static final Logger LOGGER = LogManager.getLogger(OperationPropertiesUtil.class);

    /**
     * Build the request.
     * @param request {@link SubmitSM}.
     * @param props Object with the properties of the message.
     * @return {@link SubmitSM} object With their assigned values.
     * @throws WrongLengthOfStringException If any value has exceeds the length.
     * @throws WrongDateFormatException If any value does not comply with the format.
     */
    public static SubmitSM setRequestProps(final SubmitSM request, final PDUOperationProperties props)
            throws WrongLengthOfStringException, WrongDateFormatException {
        // set values
        request.setServiceType(props.getServiceType());
        request.setSourceAddr(props.getSourceAddress());
        Arrays.asList(props.getDestAddress()).forEach(request::setDestAddr);
        request.setReplaceIfPresentFlag(props.getReplaceIfPresentFlag());
        request.setValidityPeriod(props.getValidityPeriod());
        request.setEsmClass(props.getEsmClass());
        request.setProtocolId(props.getProtocolId());
        request.setPriorityFlag(props.getPriorityFlag());
        request.setRegisteredDelivery(props.getRegisteredDelivery());
        request.setDataCoding(props.getDataCoding());
        request.setSmDefaultMsgId(props.getSmDefaultMsgId());
        return request;
    }

    /**
     * Build the request.
     * @param request {@link SubmitMultiSM}.
     * @param props Object with the properties of the message.
     * @return {@link SubmitMultiSM} object With their assigned values.
     * @throws WrongLengthOfStringException If any value has exceeds the length.
     * @throws WrongDateFormatException If any value does not comply with the format.
     */
    public static SubmitMultiSM setRequestProps(final SubmitMultiSM request, final PDUOperationProperties props)
            throws WrongLengthOfStringException, WrongDateFormatException {
        // set other values
        request.setServiceType(props.getServiceType());
        request.setSourceAddr(props.getSourceAddress());
        Arrays.asList(props.getDestAddress()).forEach(adress -> {
            try {
                request.addDestAddress(new DestinationAddress(adress));
            } catch (final TooManyValuesException e) {
                LOGGER.error(" [X] error adding destination adresses  ", e);
            }
        });
        request.setReplaceIfPresentFlag(props.getReplaceIfPresentFlag());
        request.setValidityPeriod(props.getValidityPeriod());
        request.setEsmClass(props.getEsmClass());
        request.setProtocolId(props.getProtocolId());
        request.setPriorityFlag(props.getPriorityFlag());
        request.setRegisteredDelivery(props.getRegisteredDelivery());
        request.setDataCoding(props.getDataCoding());
        request.setSmDefaultMsgId(props.getSmDefaultMsgId());
        return request;

    }

    /**
     * Build the request.
     * @param request {@link DataSM}.
     * @param props Object with the properties of the message.
     * @return {@link DataSM} instance.
     * @throws WrongLengthOfStringException If any value has exceeds the length.
     * */
    public static DataSM setRequestProps(final DataSM request, final PDUOperationProperties props) throws WrongLengthOfStringException {
        // set values
        request.setServiceType(props.getServiceType());
        request.setSourceAddr(props.getSourceAddress());
        Arrays.asList(props.getDestAddress()).forEach(request::setDestAddr);
        request.setEsmClass(props.getEsmClass());
        request.setRegisteredDelivery(props.getRegisteredDelivery());
        request.setDataCoding(props.getDataCoding());
        return request;
    }

    /**
     * Build the request.
     * @param request {@link QuerySM}.
     * @param props Object with the properties of the message.
     * @return {@link QuerySM} object With their assigned values.
     */
    public static QuerySM setRequestProps(final QuerySM request, final PDUOperationProperties props) {
        // set values
        request.setSourceAddr(props.getSourceAddress());
        return request;
    }

    /**
     * Build the request.
     * @param request {@link ReplaceSM}.
     * @param props Object with the properties of the message.
     * @return {@link ReplaceSM} object With their assigned values.
     * @throws WrongDateFormatException If any value does not comply with the format.
     */
    public static ReplaceSM setRequestProps(final ReplaceSM request, final PDUOperationProperties props) throws WrongDateFormatException {
        // set values
        request.setSourceAddr(props.getSourceAddress());
        request.setValidityPeriod(props.getValidityPeriod());
        request.setRegisteredDelivery(props.getRegisteredDelivery());
        request.setSmDefaultMsgId(props.getSmDefaultMsgId());
        return request;
    }

    /**
     * Build the request.
     * @param request {@link CancelSM}.
     * @param props Object with the properties of the message.
     * @return {@link CancelSM} object With their assigned values.
     * @throws WrongLengthOfStringException If any value has exceeds the length.
     * */
    public static CancelSM setRequestProps(final CancelSM request, final PDUOperationProperties props) throws WrongLengthOfStringException {
        // set values
        request.setServiceType(props.getServiceType());
        request.setSourceAddr(props.getSourceAddress());
        Arrays.asList(props.getDestAddress()).forEach(request::setDestAddr);
        return request;
    }
}
