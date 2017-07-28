/*
 *      File: PDUOperationsValidator.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.rules;

import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amk.smpp.PDUOperation;
import com.amk.smpp.util.SmppUtil;

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
     *
     * @return
     */
    static boolean validSubmit(final PDUOperation pduOperation) {
        boolean valid = Objects.nonNull(pduOperation);
        valid = valid && Objects.nonNull(pduOperation.getOperationProps());
        LOGGER.debug("validSubmit: " + valid);
        SmppUtil.beanProperties(pduOperation.getOperationProps()).entrySet().forEach(
                e -> LOGGER.debug(new ReflectionToStringBuilder(e, ToStringStyle.MULTI_LINE_STYLE).build()));

        return true;
    }


}
