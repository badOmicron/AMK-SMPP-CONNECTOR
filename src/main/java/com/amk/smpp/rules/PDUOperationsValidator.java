/*
 *      File: PDUOperationsValidator.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.rules;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationTypes;
import com.amk.smpp.util.SMPPUtil;

/**
 * Contract that defines the behavior for the classes of type Rule that help to validate some characteristics of the
 * operations SMPP before being executed.
 * This interface contains the logic necessary to validate the {@link PDUOperation}.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PDUOperationsValidator extends Ruleable {

    /**
     * Valid that the object is not null. Throws an {@link IllegalArgumentException} If validation is not met.
     * @param object to validate.
     */
    static void validNotNull(final Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("[X] error, Object cannot be null");
        }
    }

    /**
     * Valid that the properties of an object are not null or empty.
     * @param pduOperation .
     */
    static void validNotEmpty(final PDUOperation pduOperation) {
        final Map< String, Object > mapAux = SMPPUtil.beanProperties(pduOperation);
        if (pduOperation.getOperationType().equals(PDUOperationTypes.RECEIVE)) {
            if (mapAux.entrySet()
                    .stream()
                    .filter(map -> !map.getKey().equals("smsMessage"))
                    .filter(map -> !map.getKey().equals("listener"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().parallelStream()
                    .anyMatch(SMPPUtil.NULL_VALUE::equals)) {
                throw new IllegalArgumentException("[X] error, The following properties can not be null or empty -> " + mapAux.toString());
            }
        } else {
            if (mapAux.entrySet()
                    .stream()
                    .filter(map -> !map.getKey().equals("listener"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().parallelStream()
                    .anyMatch(SMPPUtil.NULL_VALUE::equals)) {
                throw new IllegalArgumentException("[X] error, The following properties can not be null or empty -> " + mapAux.toString());
            }
        }
    }
}
