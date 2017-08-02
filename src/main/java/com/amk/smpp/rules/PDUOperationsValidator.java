/*
 *      File: PDUOperationsValidator.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.rules;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amk.smpp.PDUOperation;
import com.amk.smpp.util.SMPPUtil;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0h
 */
public interface PDUOperationsValidator extends Ruleable {
    /**
     * Logger for class.
     */
    Logger LOGGER = LogManager.getLogger(PDUOperationsValidator.class.getName());

    /**
     * TODO
     * @param object .
     */
    static void validNotNull(final Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("[X] error, Object cannot be null");
        }
    }

    /**
     *
     * @param pduOperation .
     */
    static void validNotEmpty(final PDUOperation pduOperation) {
        final Map< String, Object > mapAux = SMPPUtil.beanProperties(pduOperation);
        if (mapAux.values().parallelStream().anyMatch(SMPPUtil.NONE_VALUE::equals)) {
            throw new IllegalArgumentException("[X] error, The following properties can not be null or empty -> " + mapAux.toString());
        }
    }

    /**
     *
     * @param pduOperation
     * @return
     */

    static boolean validSubmit(final PDUOperation pduOperation) {
        boolean valid = Objects.nonNull(pduOperation);
        valid = valid && Objects.nonNull(pduOperation.getOperationProps());
        LOGGER.debug("validSubmit: " + valid);
        SMPPUtil.beanProperties(pduOperation.getOperationProps()).entrySet().forEach(
                e -> LOGGER.debug(new ReflectionToStringBuilder(e, ToStringStyle.MULTI_LINE_STYLE).build()));

        return true;
    }


}
